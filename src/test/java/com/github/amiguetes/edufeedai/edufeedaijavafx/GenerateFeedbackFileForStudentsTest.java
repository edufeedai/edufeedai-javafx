package com.github.amiguetes.edufeedai.edufeedaijavafx;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;

import io.github.cdimascio.dotenv.Dotenv;

class GenerateFeedbackFileForStudentsTest {

    String assessmentPath = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmentMapFilePath = assessmentPath + java.io.File.separator + Dotenv.load().get("ASSESSMENT_ID_MAP_FILE");

    String moodleAssessmentFeedbackDir = Dotenv.load().get("MOODLE_ASSESSMENT_FEEDBACK_DIR");

    @Test
    void generateFeedbackFile() {

        GenerateFeedbackFileForStudents generateFeedbackFileForStudents = new GenerateFeedbackFileForStudents(assessmentPath,moodleAssessmentFeedbackDir);

        StringBuilder assessmentMap = new StringBuilder();

        try ( java.util.Scanner sc = new java.util.Scanner(new java.io.FileReader(assessmentMapFilePath)) ) {
            while (sc.hasNextLine()) {
                assessmentMap.append(sc.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();

        SubmissionIdMap[] submissionIdArray = gson.fromJson(assessmentMap.toString(), SubmissionIdMap[].class);

        try {
            generateFeedbackFileForStudents.generateFeedbackFile(submissionIdArray);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}