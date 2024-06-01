package com.github.amiguetes.edufeedai.edufeedaijavafx;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PutAssessmentInPlaceTest {

    String assessmentPath = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmentMapFilePath = assessmentPath + File.separator + Dotenv.load().get("ASSESSMENT_ID_MAP_FILE");
    String assessmentResponsesFilePath = assessmentPath + File.separator + Dotenv.load().get("ASSESSMENT_RESPONSES_FILE");

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