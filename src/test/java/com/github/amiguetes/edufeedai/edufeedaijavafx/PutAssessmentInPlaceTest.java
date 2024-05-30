package com.github.amiguetes.edufeedai.edufeedaijavafx;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PutAssessmentInPlaceTest {

    String assessmentPath = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmentMapFilePath = Dotenv.load().get("ASSESSMENT_FILE_MAP");
    String assessmentResponsesFilePath = Dotenv.load().get("ASSESSMENT_RESPONSES_FILE_PATH");

    @Test
    void putAssessmentInPlaceWorker() {

        try {
            PutAssessmentInPlace.putAssessmentInPlaceWorker(assessmentPath,assessmentMapFilePath, assessmentResponsesFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }

    }
}