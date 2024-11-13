package com.github.edufeedai.javafx;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import com.github.edufeedai.javafx.annotations.FileIOTest;
import com.github.edufeedai.javafx.annotations.IntegrationTest;
import com.github.edufeedai.javafx.annotations.PrivacyTest;
import com.github.edufeedai.javafx.annotations.UtilityTest;
import com.github.edufeedai.javafx.model.DigestImplementation;
import com.github.edufeedai.javafx.model.SubmissionIdMap;

import io.github.cdimascio.dotenv.Dotenv;


class GenerateSubmissionIDMapTest {

    String assessmentFolder = Dotenv.load().get("ASSESSMENT_TEST_DIR");
    String assessmmentIDMapFile = Dotenv.load().get("ASSESSMENT_ID_MAP_FILE");

    @Test
    @IntegrationTest
    @UtilityTest
    @FileIOTest
    @PrivacyTest
    @DisplayName("Genera y guarda el mapa de IDs de envío")
    void saveSubmissionIDMaps() {
        
        GenerateSubmissionIDMap generateSubmissionIDMap = new GenerateSubmissionIDMap(assessmentFolder,new DigestImplementation());
        SubmissionIdMap[] submissionIdMaps = generateSubmissionIDMap.generateSubmissionIDMaps();

        assertNotNull(submissionIdMaps, "El array de SubmissionIdMap no debe ser nulo");
        assertTrue(submissionIdMaps.length > 0, "El array de SubmissionIdMap debe contener al menos un elemento");

        String filename = generateSubmissionIDMap.saveSubmissionIDMaps(submissionIdMaps, assessmmentIDMapFile);
        assertNotNull(filename, "El nombre del archivo generado no debe ser nulo");

    }
}