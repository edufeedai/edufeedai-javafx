package com.github.edufeedai.integration;

import com.github.edufeedai.model.openai.platform.api.OpenAIBatchProcess;
import com.github.edufeedai.model.openai.platform.api.OpenAIFileManagement;
import com.github.edufeedai.model.openai.platform.api.exceptions.OpenAIAPIException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;

class OpenAIBatchJobDownloadFinishedRightFileTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");
    String AssessmentJSONLFile = Dotenv.load().get("ASSESSMENT_JSONL_FILE");
    String OpenAIBatchJobID = Dotenv.load().get("OPENAI_BATCH_JOB_ID");

    @Test
    public void downloadFileJobFinished() throws OpenAIAPIException {

        OpenAIFileManagement oaif = new OpenAIFileManagement(OpenAI_KEY);
        OpenAIBatchProcess oabp = new OpenAIBatchProcess(OpenAI_KEY);

        try {

            String status = oabp.getBatchJob(OpenAIBatchJobID).getStatus();
            assertEquals("completed", status, "Batch job is not finished");

            String fileId = oabp.getBatchJob(OpenAIBatchJobID).getOutputFileId();

            File AssessmentFile = new File(AssessmentJSONLFile);

            Path filePath = AssessmentFile.toPath();
            String fileName = filePath.getFileName().toString();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

            String dirPath = filePath.getParent().toString();

            String outputFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_output." + extension;

            File outFile = new File(filePath.getParent().toString(), outputFileName);
            
            System.out.println("Downloading file: " + outFile.getAbsolutePath());


        } catch (OpenAIAPIException e) {
            fail(e.getMessage());
        }
        
    }

   
}