package com.github.edufeedai.unit;

import org.junit.jupiter.api.Test;

import com.github.edufeedai.model.Assessment;
import com.github.edufeedai.model.CheckResults;

import static org.junit.jupiter.api.Assertions.*;

class CheckResultsTest {

    @Test
    void createNewAssessment() {

        CheckResults checkResults = CheckResults.getInstance();

        Assessment assessment = null;

        try {
             assessment = checkResults.createNewAssessment(
                    "Sumale 1 a lo que te pase como argumento","1"
            );
        } catch (Exception e) {
            fail(e);
        }
        assertNotNull(assessment, "The assessment should not be null");
        assertNotNull(assessment.getId(), "The id of the assessment should not be null");

    }
}