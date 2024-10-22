package com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.api;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.api.exceptions.OpenAIAPIException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.io.File;

public class OpenAIFileUpload {

    String apiKey;
    String uploadUrl;

    public OpenAIFileUpload(String apiKey){
        this(apiKey,"https://api.openai.com/v1/files");
    }

    public OpenAIFileUpload(String apiKey,String uploadUrl){

        this.apiKey = apiKey;
        this.uploadUrl = uploadUrl;

    }

    public String uploadFile(String filePath) throws OpenAIAPIException{
        File jsonlFile = new File(filePath);
        return uploadFile(jsonlFile);
    }

    public String uploadFile(File jsonlFile) throws OpenAIAPIException{

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadRequest = new HttpPost(uploadUrl);
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
                    return jsonResponse.getString("id");

                } else {
                    throw new OpenAIAPIException("Respuesta del Servidor: " + response.toString());
                }

            }
        } catch (Exception e) {
            throw new OpenAIAPIException();
        }

    }

}
