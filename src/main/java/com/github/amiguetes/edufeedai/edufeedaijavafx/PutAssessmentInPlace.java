package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.response.BatchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PutAssessmentInPlace {

    public PutAssessmentInPlace(String assessmentPath, String assessmentMapFilePath, String assessmentResponsesFilePath){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        StringBuilder assessmentMap = new StringBuilder();
        SubmissionIdMap[] submissionIdMap;

        List<BatchRequest> batchRequests = new ArrayList<>();

        try ( Scanner sc = new Scanner(new FileReader(assessmentMapFilePath)) ) {
            while (sc.hasNextLine()) {
                assessmentMap.append(sc.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        submissionIdMap = gson.fromJson(assessmentMap.toString(), SubmissionIdMap[].class);

        try ( Scanner sc = new Scanner(new FileReader(assessmentResponsesFilePath)) ){
            while (sc.hasNextLine()) {
                BatchRequest batchRequest = gson.fromJson(sc.nextLine(), BatchRequest.class);
                batchRequests.add(batchRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
