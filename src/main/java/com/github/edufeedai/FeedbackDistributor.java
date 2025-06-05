package com.github.edufeedai;

import com.github.edufeedai.model.SubmissionIdMap;
import com.github.edufeedai.model.openai.platform.response.BatchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(FeedbackDistributor.class);

    /**
     * Reads the assessment map and assessment responses, then writes feedback into each student's folder.
     *
     * @param assessmentsRootPath         the root directory where student folders are located
     * @param assessmentMapFilePath       the path to the JSON file mapping submission IDs to custom IDs
     * @param assessmentResponsesFilePath the path to the JSONL file containing assessment responses
     */
    public static void distributeFeedback(String assessmentsRootPath, String assessmentMapFilePath, String assessmentResponsesFilePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        logger.info("Starting feedback distribution. Root: {}, Map: {}, Responses: {}", assessmentsRootPath, assessmentMapFilePath, assessmentResponsesFilePath);

        // Read the assessment map file into a string
        StringBuilder assessmentMapContent = new StringBuilder();
        SubmissionIdMap[] submissionIdMappings;
        List<BatchRequest> assessmentResponses = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileReader(assessmentMapFilePath))) {
            while (scanner.hasNextLine()) {
                assessmentMapContent.append(scanner.nextLine());
            }
        } catch (Exception e) {
            logger.error("Error reading assessment map file: {}", assessmentMapFilePath, e);
            return;
        }

        // Parse the assessment map JSON
        try {
            submissionIdMappings = gson.fromJson(assessmentMapContent.toString(), SubmissionIdMap[].class);
        } catch (Exception e) {
            logger.error("Error parsing assessment map JSON.", e);
            return;
        }

        // Read and parse the assessment responses (JSONL)
        try (Scanner scanner = new Scanner(new FileReader(assessmentResponsesFilePath))) {
            while (scanner.hasNextLine()) {
                BatchRequest batchRequest = gson.fromJson(scanner.nextLine(), BatchRequest.class);
                assessmentResponses.add(batchRequest);
            }
        } catch (Exception e) {
            logger.error("Error reading assessment responses file: {}", assessmentResponsesFilePath, e);
            return;
        }

        // Build a map from custom_id to student folder name
        Map<String, String> customIdToFolderMap = new HashMap<>();
        for (SubmissionIdMap mapping : submissionIdMappings) {
            customIdToFolderMap.put(mapping.getCustom_id(), mapping.getSubmission_id());
        }
        logger.debug("Built customIdToFolderMap with {} entries.", customIdToFolderMap.size());

        // Write feedback to each student's folder
        for (BatchRequest response : assessmentResponses) {
            String customId = response.getCustom_id();
            String studentFolder = customIdToFolderMap.get(customId);
            if (studentFolder == null) {
                logger.warn("No folder found for custom_id: {}", customId);
                continue;
            }
            String fileName = studentFolder + ".md";
            String feedbackContent = response.getResponse().getBody().getChoices().get(0).getMessage().getContent();
            String outputPath = assessmentsRootPath + File.separator + studentFolder + File.separator + fileName;
            try {
                Files.writeString(Paths.get(outputPath), feedbackContent);
                logger.info("Feedback written to {}", outputPath);
            } catch (Exception e) {
                logger.error("Error writing feedback to {}", outputPath, e);
            }
        }
        logger.info("Feedback distribution completed.");
    }
}
