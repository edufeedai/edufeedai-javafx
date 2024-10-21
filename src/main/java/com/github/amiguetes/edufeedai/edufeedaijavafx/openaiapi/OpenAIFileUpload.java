package com.github.amiguetes.edufeedai.edufeedaijavafx.openaiapi;

import com.github.amiguetes.edufeedai.edufeedaijavafx.openaiapi.exceptions.OpenAIAPIException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.json.JSONObject;

import java.io.File;

public class OpenAIFileUpload {

    String apiKey;
    String uploadUrl;
    File jsonlFile;

    public OpenAIFileUpload(String apiKey,String jsonlFilePath){
        this(apiKey,"https://api.openai.com/v1/files",jsonlFilePath);
    }

    public OpenAIFileUpload(String apiKey,String uploadUrl,String jsonlFilePath){

        this.apiKey = apiKey;
        this.uploadUrl = uploadUrl;
        jsonlFile = new File(jsonlFilePath);

    }

    public String uploadFile() throws OpenAIAPIException{

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadRequest = new HttpPost(uploadUrl);
            uploadRequest.setHeader("Authorization", "Bearer " + apiKey);

            // Usamos MultipartEntityBuilder para construir la solicitud multipart
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addBinaryBody("file", jsonlFile, ContentType.APPLICATION_OCTET_STREAM, jsonlFile.getName());
            entityBuilder.addTextBody("purpose", "fine-tune"); // O el propósito que estés usando

            uploadRequest.setEntity(entityBuilder.build());

            try (CloseableHttpResponse response = httpClient.execute(uploadRequest)) {

                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getCode() == 200) {
                    // Procesa la respuesta y obtiene el ID del archivo
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String fileId = jsonResponse.getString("id");
                    return fileId;
                } else {
                    throw new OpenAIAPIException("Respuesta del Servidor: " + response.toString());
                }

            }
        } catch (Exception e) {
            throw new OpenAIAPIException();
        }


    }

}
