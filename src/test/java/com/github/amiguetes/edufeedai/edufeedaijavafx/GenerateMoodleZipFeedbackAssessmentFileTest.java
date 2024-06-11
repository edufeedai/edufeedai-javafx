package com.github.amiguetes.edufeedai.edufeedaijavafx;

import static org.junit.jupiter.api.Assertions.fail;

import com.github.amiguetes.edufeedai.edufeedaijavafx.utils.ZipUtils;
import org.junit.jupiter.api.Test;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;

class GenerateMoodleZipFeedbackAssessmentFileTest {

    String assessmentPath = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmentMapFilePath = assessmentPath + java.io.File.separator + Dotenv.load().get("ASSESSMENT_ID_MAP_FILE");

    String moodleAssessmentFeedbackDir = Dotenv.load().get("MOODLE_ASSESSMENT_FEEDBACK_DIR");

    @Test
    void generateFeedbackFile() {

        GenerateMoodleZipFeedbackAssessmentFile generateFeedbackFileForStudents = new GenerateMoodleZipFeedbackAssessmentFile(assessmentPath,moodleAssessmentFeedbackDir);

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
            generateFeedbackFileForStudents.generateFeedbackFile(submissionIdArray);
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