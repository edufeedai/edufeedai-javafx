package com.github.amiguetes.edufeedai.edufeedaijavafx.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckResultsTest {

    @Test
    void createNewAssessment() {

        CheckResults checkResults = CheckResults.getInstance();

        Assessment assessment = null;

        try {
             assessment = checkResults.createNewAssessment(
                    "bla","bla"
            );
        } catch (Exception e) {
            fail(e);
        }
        assertNotNull(assessment, "The assessment should not be null");
        assertNotNull(assessment.getId(), "The id of the assessment should not be null");

    }
}