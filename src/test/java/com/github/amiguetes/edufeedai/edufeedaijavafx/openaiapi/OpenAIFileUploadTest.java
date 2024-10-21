package com.github.amiguetes.edufeedai.edufeedaijavafx.openaiapi;

import com.github.amiguetes.edufeedai.edufeedaijavafx.openaiapi.exceptions.OpenAIAPIException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIFileUploadTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");
    String AssessmentFile = Dotenv.load().get("ASSESSMENT_ID_FILE");

    @Test
    void uploadFile() {

        OpenAIFileUpload oaif = new OpenAIFileUpload(OpenAI_KEY,AssessmentFile);

        try {

            String response = oaif.uploadFile();
            assertNotNull(response);

        } catch (OpenAIAPIException e) {
            fail(e);
        }


    }
}