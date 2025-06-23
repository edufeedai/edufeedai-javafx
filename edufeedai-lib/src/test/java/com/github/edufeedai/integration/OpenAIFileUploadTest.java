package com.github.edufeedai.integration;

import com.github.edufeedai.model.openai.platform.api.OpenAIFileManagement;
import com.github.edufeedai.model.openai.platform.api.exceptions.OpenAIAPIException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIFileUploadTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");
    String AssessmentTestDir = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String AssessmentFileName = Dotenv.load().get("ASSESSMENT_JSONL_FILE_NAME");

    @Test
    void uploadFileString() {

        OpenAIFileManagement oaifm = new OpenAIFileManagement(OpenAI_KEY);

        try {

            String fileId = oaifm.uploadFile(AssessmentTestDir + File.separator + AssessmentFileName);
            assertNotNull(fileId);

        } catch (OpenAIAPIException e) {
            fail(e.getMessage());
        }


    }
}