package com.github.amiguetes.edufeedai.edufeedaijavafx.openaiapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIBatchProcessTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");
    String OpenAIBatchFileID = Dotenv.load().get("OPENAI_BATCH_FILE_ID");

    @Test
    void launchBatchProcess() {

        OpenAIBatchProcess oabp = new OpenAIBatchProcess(OpenAI_KEY);

        try {

            String response = oabp.launchBatchProcess(OpenAIBatchFileID);
            assertNotNull(response);

        } catch (Exception e) {
            fail(e);
        }
    }
}