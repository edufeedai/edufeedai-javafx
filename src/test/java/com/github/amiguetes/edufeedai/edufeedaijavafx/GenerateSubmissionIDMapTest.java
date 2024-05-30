package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.DigestImplementation;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenerateSubmissionIDMapTest {

    String assessmentFolder = Dotenv.load().get("ASSESSMENT_TEST_DIR");

    @org.junit.jupiter.api.Test
    void generateSubmissionIDMap() {
        GenerateSubmissionIDMap generateSubmissionIDMap = new GenerateSubmissionIDMap(assessmentFolder,new DigestImplementation());
        SubmissionIdMap[] submissionIdMaps = generateSubmissionIDMap.generateSubmissionIDMaps();

        assertNotNull(submissionIdMaps);
        assertTrue(submissionIdMaps.length > 0);

        for (SubmissionIdMap submissionIdMap : submissionIdMaps) {
            assertNotNull(submissionIdMap.getCustom_id());
            assertNotNull(submissionIdMap.getSubmission_id());
        }

    }

    @Test
    void saveSubmissionIDMaps() {
        GenerateSubmissionIDMap generateSubmissionIDMap = new GenerateSubmissionIDMap(assessmentFolder,new DigestImplementation());
        SubmissionIdMap[] submissionIdMaps = generateSubmissionIDMap.generateSubmissionIDMaps();

        assertNotNull(submissionIdMaps);
        assertTrue(submissionIdMaps.length > 0);
        String filename = generateSubmissionIDMap.saveSubmissionIDMaps(submissionIdMaps);
        assertNotNull(filename);
    }
}