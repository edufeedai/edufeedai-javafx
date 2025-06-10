package com.github.edufeedai.model.openai.platform.api;

import com.github.edufeedai.model.openai.platform.api.exceptions.OpenAIAPIException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class OpenAIFileManagement {

    final String apiKey;
    final String url;

    private static final Logger LOGGER = Logger.getLogger(OpenAIFileManagement.class.getName());

    public OpenAIFileManagement(String apiKey){
        this(apiKey,"https://api.openai.com/v1/files");
    }

    public OpenAIFileManagement(String apiKey,String url){

        this.apiKey = apiKey;
        this.url = url;

    }

    public String uploadFile(String filePath) throws OpenAIAPIException{
        File jsonlFile = new File(filePath);
        return uploadFile(jsonlFile);
    }

    public String uploadFile(File jsonlFile) throws OpenAIAPIException{

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadRequest = new HttpPost(url);
            uploadRequest.setHeader("Authorization", "Bearer " + apiKey);

            // Usamos MultipartEntityBuilder para construir la solicitud multipart
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addBinaryBody("file", jsonlFile);
            entityBuilder.addTextBody("purpose", "batch");

            uploadRequest.setEntity(entityBuilder.build());

            try (CloseableHttpResponse response = httpClient.execute(uploadRequest)) {

                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getCode() == 200) {
                    // Procesa la respuesta y obtiene el ID del archivo
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String id = jsonResponse.getString("id");
                    LOGGER.info("Archivo subido exitosamente a OpenAI. ID del archivo: " + id);
                    return id;

                } else {
                    throw new OpenAIAPIException("Respuesta del Servidor: " + response.toString());
                }

            }
        } catch (Exception e) {
            throw new OpenAIAPIException();
        }

    }

    public void downloadFile(String fileId, String outputFilePath) throws OpenAIAPIException {
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String downloadUrl = url + "/" + fileId + "/content"; ;
            HttpGet downloadRequest = new HttpGet(downloadUrl);
            downloadRequest.setHeader("Authorization", "Bearer " + apiKey);

            try (CloseableHttpResponse response = httpClient.execute(downloadRequest)) {
                if (response.getCode() == 200) {
                    // Guardar el contenido del archivo en el archivo de salida
                    File outputFile = new File(outputFilePath);

                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        // Escribir el contenido del archivo descargado en el archivo de salida
                        response.getEntity().writeTo(fos);
                    } catch (IOException e) {
                        throw new OpenAIAPIException("Error al escribir el archivo descargado: " + e.getMessage());
                    }
                    
                    LOGGER.info("Archivo descargado exitosamente a: " + outputFilePath);
                } else {
                    throw new OpenAIAPIException("Error al descargar el archivo: " + response.toString());
                }
            }
        } catch (Exception e) {
            throw new OpenAIAPIException(e.getMessage());
        }

    }

}
