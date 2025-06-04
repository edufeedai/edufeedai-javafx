package com.github.edufeedai;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.github.edufeedai.model.SubmissionIdMap;

/**
 * Utility class to generate feedback files for Moodle assessments by copying feedback files
 * from a source directory to a Moodle-compatible feedback directory structure.
 *
 * <p>This class does not alter the logic of file generation, but improves code readability,
 * maintainability, and provides documentation for future developers.</p>
 *
 * @author EduFeedAI
 */
public class MoodleFeedbackFileGenerator {

    /**
     * Path to the directory containing assessment feedback files.
     */
    private final String sourceAssessmentDir;

    /**
     * Path to the directory where Moodle-compatible feedback files will be placed.
     */
    private String moodleFeedbackDir;

    /**
     * File extension for assessment feedback files (e.g., "md").
     */
    private final String feedbackFileExtension;

    /**
     * Constructs a MoodleFeedbackFileGenerator with the default feedback file extension ("md").
     *
     * @param sourceAssessmentDir Path to the source assessment feedback directory
     * @param moodleFeedbackDir   Path to the Moodle feedback directory
     */
    public MoodleFeedbackFileGenerator(String sourceAssessmentDir, String moodleFeedbackDir) {
        this(sourceAssessmentDir, moodleFeedbackDir, "md");
    }

    /**
     * Constructs a MoodleFeedbackFileGenerator with a custom feedback file extension.
     *
     * @param sourceAssessmentDir    Path to the source assessment feedback directory
     * @param moodleFeedbackDir      Path to the Moodle feedback directory
     * @param feedbackFileExtension  File extension for feedback files (e.g., "md", "txt")
     */
    public MoodleFeedbackFileGenerator(String sourceAssessmentDir, String moodleFeedbackDir, String feedbackFileExtension) {
        this.sourceAssessmentDir = sourceAssessmentDir;
        this.moodleFeedbackDir = moodleFeedbackDir;
        this.feedbackFileExtension = feedbackFileExtension;
    }

    /**
     * Generates Moodle-compatible feedback files for each submission in the provided array.
     *
     * <p>For each {@link SubmissionIdMap}, this method copies the feedback file from the source
     * directory to the Moodle feedback directory, preserving the required naming convention.</p>
     *
     * @param submissionIdMaps Array of {@link SubmissionIdMap} objects representing student submissions
     */
    public void generateFeedbackFiles(SubmissionIdMap[] submissionIdMaps) {
        File sourceDir = new File(sourceAssessmentDir);
        File moodleDir = new File(moodleFeedbackDir);

        for (SubmissionIdMap submission : submissionIdMaps) {
            String customId = submission.getCustom_id();

            String relativeFeedbackPath = customId + File.separator + customId + "." + feedbackFileExtension;
            String moodleFeedbackFileName = customId + "." + feedbackFileExtension;

            File sourceFeedbackFile = new File(sourceDir, relativeFeedbackPath);
            File moodleFeedbackFile = new File(moodleDir, moodleFeedbackFileName);

            try {
                if (sourceFeedbackFile.exists()) {
                    File parentDir = moodleFeedbackFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    Files.copy(sourceFeedbackFile.toPath(), moodleFeedbackFile.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
