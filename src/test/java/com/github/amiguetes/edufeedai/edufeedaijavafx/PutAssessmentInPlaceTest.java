package com.github.amiguetes.edufeedai.edufeedaijavafx;

import java.io.File;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;

class PutAssessmentInPlaceTest {

    String assessmentPath = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmentMapFilePath = assessmentPath + File.separator + Dotenv.load().get("ASSESSMENT_ID_MAP_FILE");
    String assessmentResponsesFilePath = assessmentPath + File.separator + Dotenv.load().get("ASSESSMENT_RESPONSES_FILE");

    private static final Logger LOGGER = Logger.getLogger(PutAssessmentInPlaceTest.class.getName());

    @Test
    void putAssessmentInPlaceWorker() {

        try {
            PutAssessmentInPlace.putAssessmentInPlaceWorker(assessmentPath,assessmentMapFilePath, assessmentResponsesFilePath);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            fail(e);
        }

    }
}