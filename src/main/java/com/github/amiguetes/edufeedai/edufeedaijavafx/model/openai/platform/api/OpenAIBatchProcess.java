package com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.api;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.api.batches.BatchJob;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.api.helpers.GsonResponseHandler;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.api.exceptions.OpenAIAPIException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

public class OpenAIBatchProcess {

    String apiKey;
    String batchUrl;
    HttpPost batchRequest;

    public OpenAIBatchProcess(String apiKey){
        this(apiKey,"https://api.openai.com/v1/batches");
    }

    public OpenAIBatchProcess(String apiKey,String batchUrl){

        this.apiKey = apiKey;
        this.batchUrl = batchUrl;

        batchRequest = new HttpPost(batchUrl);
        batchRequest.setHeader("Authorization", "Bearer " + apiKey);
        batchRequest.setHeader("Content-Type", "application/json");

    }

    public BatchJob enqueueBatchProcess(String fileId) throws OpenAIAPIException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // Crea el JSON con los parámetros de batch processing
            JSONObject json = new JSONObject();
            json.put("input_file_id", fileId);
            json.put("endpoint", "/v1/chat/completions");
            json.put("completion_window","24h");

            StringEntity requestEntity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
            batchRequest.setEntity(requestEntity);

            try (CloseableHttpResponse response = httpClient.execute(batchRequest)) {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode == 200) {

                    return GsonResponseHandler.convertJsonToObject(responseBody,BatchJob.class);

                } else {
                    throw new OpenAIAPIException(responseBody);
                }
            }
        } catch (Exception e) {
            throw new OpenAIAPIException(e);
        }

    }
}
