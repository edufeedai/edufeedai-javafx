package com.github.amiguetes.edufeedai.edufeedaijavafx;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.DigestImplementation;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;

import io.github.cdimascio.dotenv.Dotenv;


class GenerateSubmissionIDMapTest {

    String assessmentFolder = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmmentIDMapFile = Dotenv.load().get("ASSESSMENT_ID_MAP_FILE");

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
        String filename = generateSubmissionIDMap.saveSubmissionIDMaps(submissionIdMaps,assessmmentIDMapFile);
        assertNotNull(filename);
    }
}