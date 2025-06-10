package com.github.edufeedai.integration;

import com.github.edufeedai.model.openai.platform.api.OpenAIFileManagement;
import com.github.edufeedai.model.openai.platform.api.exceptions.OpenAIAPIException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIFileUploadTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");
    String AssessmentFile = Dotenv.load().get("ASSESSMENT_JSONL_FILE");

    @Test
    void uploadFileString() {

        OpenAIFileManagement oaifm = new OpenAIFileManagement(OpenAI_KEY);

        try {

            String fileId = oaifm.uploadFile(AssessmentFile);
            assertNotNull(fileId);

        } catch (OpenAIAPIException e) {
            fail(e.getMessage());
        }


    }
}