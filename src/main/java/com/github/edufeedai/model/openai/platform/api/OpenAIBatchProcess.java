package com.github.edufeedai.model.openai.platform.api;

import com.github.edufeedai.model.openai.platform.api.batches.BatchJob;
import com.github.edufeedai.model.openai.platform.api.helpers.GsonResponseHandler;
import com.github.edufeedai.model.openai.platform.api.exceptions.OpenAIAPIException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

/**
 * Clase para gestionar el procesamiento por lotes (batch) de archivos en la API de OpenAI.
 * Permite enviar un archivo para su procesamiento masivo usando la API REST de OpenAI.
 */
public class OpenAIBatchProcess {

    /** Clave de API de OpenAI */
    String apiKey;
    /** URL del endpoint batch de OpenAI */
    String batchUrl;
    /** Petición HTTP POST configurada para el batch */
    HttpPost batchRequest;

    /**
     * Crea un proceso batch usando la URL por defecto de la API de OpenAI.
     * @param apiKey Clave de API de OpenAI
     */
    public OpenAIBatchProcess(String apiKey){
        this(apiKey,"https://api.openai.com/v1/batches");
    }

    /**
     * Crea un proceso batch usando una URL personalizada para la API de OpenAI.
     * @param apiKey Clave de API de OpenAI
     * @param batchUrl URL del endpoint batch de OpenAI
     */
    public OpenAIBatchProcess(String apiKey,String batchUrl){
        this.apiKey = apiKey;
        this.batchUrl = batchUrl;
        batchRequest = new HttpPost(batchUrl);
        batchRequest.setHeader("Authorization", "Bearer " + apiKey);
        batchRequest.setHeader("Content-Type", "application/json");
    }

    /**
     * Encola un archivo para su procesamiento por lotes en OpenAI.
     *
     * @param fileId ID del archivo previamente subido a OpenAI
     * @return Un objeto BatchJob con la información del proceso encolado
     * @throws OpenAIAPIException Si ocurre un error en la petición o respuesta de la API
     */
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