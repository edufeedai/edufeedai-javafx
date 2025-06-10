package com.github.edufeedai.integration;

import static org.junit.jupiter.api.Assertions.fail;

import com.github.edufeedai.utils.ZipUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.edufeedai.MoodleFeedbackFileGenerator;
import com.github.edufeedai.model.SubmissionIdMap;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;

@DisplayName("TESTS - MoodleFeedbackFileGeneratorTest")
class MoodleFeedbackFileGeneratorTest {

    String assessmentPath = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmentMapFilePath = assessmentPath + java.io.File.separator + Dotenv.load().get("ASSESSMENT_ID_MAP_FILE");

    String moodleAssessmentFeedbackDir = Dotenv.load().get("MOODLE_ASSESSMENT_FEEDBACK_DIR");

    @Test
    @DisplayName("Genera el archivo de feedback de la evaluación para Moodle")
    void generateFeedbackFiles() {

        File archivo = new File(moodleAssessmentFeedbackDir);
        archivo.mkdirs();

        MoodleFeedbackFileGenerator generateFeedbackFileForStudents = new MoodleFeedbackFileGenerator(assessmentPath,moodleAssessmentFeedbackDir);

        StringBuilder assessmentMap = new StringBuilder();

        try ( java.util.Scanner sc = new java.util.Scanner(new java.io.FileReader(assessmentMapFilePath)) ) {
            while (sc.hasNextLine()) {
                assessmentMap.append(sc.nextLine());
            }
        } catch (Exception e) {
            fail(e);
        }

        com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();

        SubmissionIdMap[] submissionIdArray = gson.fromJson(assessmentMap.toString(), SubmissionIdMap[].class);

        try {
            generateFeedbackFileForStudents.generateFeedbackFiles(submissionIdArray);
        } catch (Exception e) {
            fail(e);
        }

        try {
            ZipUtils.compressFileAndRemoveDirectories((new File(moodleAssessmentFeedbackDir).toPath()), "md");
        } catch (IOException e) {
            fail(e);
        }

    }
}