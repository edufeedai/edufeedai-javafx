package com.github.edufeedai.javafx.model.ocrlib;


import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.ai.documentintelligence.models.AnalyzeDocumentRequest;
import com.azure.ai.documentintelligence.models.AnalyzeResult;
import com.azure.ai.documentintelligence.models.AnalyzeResultOperation;
import com.azure.ai.documentintelligence.models.Document;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.polling.SyncPoller;

import java.io.*;
import java.util.Base64;

public class OCRProcessorAzure implements OCRProcessor {

    String apikey;
    String endpoint;
    DocumentIntelligenceClient client;

    public OCRProcessorAzure(String apikey, String endpoint) {

        this.apikey = apikey;
        this.endpoint = endpoint;

        // Instantiate a client that will be used to call the service.
         client = new DocumentIntelligenceClientBuilder()
                .credential(new AzureKeyCredential(apikey))
                .endpoint(endpoint)
                .buildClient();

    }

    public String performOCR(File imageFile) throws OCRProcessorException {

        // Modelo personalizado (puedes usar el modelo general de OCR si no tienes un modelo específico)
        String modelId = "prebuilt-read";  // Usar el modelo 'prebuilt-read' para solo extraer texto




        // Cargar el archivo PNG local

        String base64 = null;

        try (FileInputStream fis = new FileInputStream(imageFile)){

            byte[] imageBytes = new byte[(int) imageFile.length()];
            fis.read(imageBytes);
            base64 = Base64.getEncoder().encodeToString(imageBytes);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder ocrResult = new StringBuilder();

        try {



            AnalyzeDocumentRequest request = new AnalyzeDocumentRequest().setBase64Source(base64.getBytes());

            SyncPoller<AnalyzeResultOperation, AnalyzeResult > analyzePoller =
                    client.beginAnalyzeDocument(
                            modelId,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            request);

            AnalyzeResult analyzeResult = analyzePoller.getFinalResult();

            // Solo obtener el texto
            for (Document document : analyzeResult.getDocuments()) {

                ocrResult.append(document.toJsonString());  // Muestra el contenido completo del documento (texto)
            }
        } catch (Exception e) {
            throw new OCRProcessorException(e);
        }

        return ocrResult.toString();
    }

}
