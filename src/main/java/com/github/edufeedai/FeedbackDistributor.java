package com.github.edufeedai;

import com.github.edufeedai.model.SubmissionIdMap;
import com.github.edufeedai.model.openai.platform.response.BatchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

/**
 * Utility class for distributing assessment feedback into student folders.
 * <p>
 * This class reads a mapping of submission IDs to custom IDs and a list of assessment responses,
 * then writes the feedback into the corresponding student's folder as a Markdown file.
 * </p>
 *
 * <p>Typical usage:</p>
 * <pre>
 *     FeedbackDistributor.distributeFeedback(
 *         "/path/to/assessments",
 *         "/path/to/assessment_map.json",
 *         "/path/to/assessment_responses.jsonl"
 *     );
 * </pre>
 *
 * @author EduFeedAI
 */
public class FeedbackDistributor {

    /**
     * Reads the assessment map and assessment responses, then writes feedback into each student's folder.
     *
     * @param assessmentsRootPath         the root directory where student folders are located
     * @param assessmentMapFilePath       the path to the JSON file mapping submission IDs to custom IDs
     * @param assessmentResponsesFilePath the path to the JSONL file containing assessment responses
     */
    public static void distributeFeedback(String assessmentsRootPath, String assessmentMapFilePath, String assessmentResponsesFilePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Read the assessment map file into a string
        StringBuilder assessmentMapContent = new StringBuilder();
        SubmissionIdMap[] submissionIdMappings;
        List<BatchRequest> assessmentResponses = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileReader(assessmentMapFilePath))) {
            while (scanner.hasNextLine()) {
                assessmentMapContent.append(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Parse the assessment map JSON
        submissionIdMappings = gson.fromJson(assessmentMapContent.toString(), SubmissionIdMap[].class);

        // Read and parse the assessment responses (JSONL)
        try (Scanner scanner = new Scanner(new FileReader(assessmentResponsesFilePath))) {
            while (scanner.hasNextLine()) {
                BatchRequest batchRequest = gson.fromJson(scanner.nextLine(), BatchRequest.class);
                assessmentResponses.add(batchRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Build a map from custom_id to student folder name
        Map<String, String> customIdToFolderMap = new HashMap<>();
        for (SubmissionIdMap mapping : submissionIdMappings) {
            customIdToFolderMap.put(mapping.getCustom_id(), mapping.getSubmission_id());
        }

        // Write feedback to each student's folder
        for (BatchRequest response : assessmentResponses) {
            String customId = response.getCustom_id();
            String studentFolder = customIdToFolderMap.get(customId);
            if (studentFolder == null) {
                System.err.println("No folder found for custom_id: " + customId);
                continue;
            }
            String fileName = studentFolder + ".md";
            String feedbackContent = response.getResponse().getBody().getChoices().get(0).getMessage().getContent();
            String outputPath = assessmentsRootPath + File.separator + studentFolder + File.separator + fileName;
            try {
                Files.writeString(Paths.get(outputPath), feedbackContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
