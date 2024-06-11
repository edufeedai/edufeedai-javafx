package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.response.BatchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.util.*;

public class PutFeedbackIntoStudentsFolders {

    /**
     * This method reads the assessment map and the assessment responses from OpenAI in jsonl format and puts the
     * feedback in the Student's folders
     *
     * @param assessmentPath the path where the assessment will be put
     * @param assessmentMapFilePath the path to the assessment map file
     * @param assessmentResponsesFilePath the path to the assessment responses file
     */
    public static void putAssessmentInPlaceWorker(String assessmentPath, String assessmentMapFilePath, String assessmentResponsesFilePath){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        StringBuilder assessmentMap = new StringBuilder();
        SubmissionIdMap[] submissionIdArray;

        List<BatchRequest> batchRequests = new ArrayList<>();

        try ( Scanner sc = new Scanner(new FileReader(assessmentMapFilePath)) ) {
            while (sc.hasNextLine()) {
                assessmentMap.append(sc.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        submissionIdArray = gson.fromJson(assessmentMap.toString(), SubmissionIdMap[].class);

        try ( Scanner sc = new Scanner(new FileReader(assessmentResponsesFilePath)) ){
            while (sc.hasNextLine()) {
                BatchRequest batchRequest = gson.fromJson(sc.nextLine(), BatchRequest.class);
                batchRequests.add(batchRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String,String> submissionIdMap = new HashMap<>();

        for (SubmissionIdMap submissionIdMap1 : submissionIdArray) {
            submissionIdMap.put(submissionIdMap1.getSubmission_id(),submissionIdMap1.getCustom_id());
        }

        for (BatchRequest batchRequest : batchRequests){

            String batchRequestCustomId = batchRequest.getCustom_id();

            String folder = submissionIdMap.get(batchRequestCustomId);
            String filename = folder + ".md";

            try {
                java.nio.file.Files.writeString(java.nio.file.Paths.get(assessmentPath + java.io.File.separator +
                        folder + java.io.File.separator
                        + filename),

                        batchRequest.getResponse().getBody().getChoices().get(0).getMessage().getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
