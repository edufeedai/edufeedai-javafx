package com.github.edufeedai.integration;

import com.github.edufeedai.model.openai.platform.api.OpenAIBatchProcess;
import com.github.edufeedai.model.openai.platform.api.OpenAIFileUpload;
import com.github.edufeedai.model.openai.platform.api.exceptions.OpenAIAPIException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIBatchProcessUploadAndStartTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");
    String AssessmentFile = Dotenv.load().get("ASSESSMENT_JSONL_FILE");

    
    private String uploadFileString() throws OpenAIAPIException {

        OpenAIFileUpload oaif = new OpenAIFileUpload(OpenAI_KEY);

        try {

            String fileId = oaif.uploadFile(AssessmentFile);
            assertNotNull(fileId);
            return fileId;

        } catch (OpenAIAPIException e) {
            fail(e.getMessage());
        }
        return null;

    }

    private void startBatchProcess(String fileId) throws OpenAIAPIException {
        
        OpenAIBatchProcess oabp = new OpenAIBatchProcess(OpenAI_KEY);
        try {

            var job = oabp.enqueueBatchProcess(fileId);
            assertNotNull(job);
            assertNotNull(job.getId());

        } catch (OpenAIAPIException e) {
            fail(e.getMessage());
        }

    }


    @Test
    void uploadAndStartBatchProcess() {

        try {
            String fileId = uploadFileString();
            startBatchProcess(fileId);
        } catch (OpenAIAPIException e) {
            fail(e.getMessage());
        }

    }
}