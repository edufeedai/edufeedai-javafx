package com.github.amiguetes.edufeedai.edufeedaijavafx.openaiapi;

import com.github.amiguetes.edufeedai.edufeedaijavafx.openaiapi.exceptions.OpenAIAPIException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIFileUploadTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");
    String AssessmentFile = Dotenv.load().get("ASSESSMENT_FILE");

    @Test
    void uploadFileString() {

        OpenAIFileUpload oaif = new OpenAIFileUpload(OpenAI_KEY);

        try {

            String fileId = oaif.uploadFile(AssessmentFile);
            assertNotNull(fileId);

        } catch (OpenAIAPIException e) {
            fail(e);
        }


    }
}