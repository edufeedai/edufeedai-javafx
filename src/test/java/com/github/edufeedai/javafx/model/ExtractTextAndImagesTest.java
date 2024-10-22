package com.github.edufeedai.javafx.model;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtractTextAndImagesTest {

    String assessmenetPDFFile = Dotenv.load().get("ASSESSMENT_PDF_FILE");

    @Test
    void extractImagesAndTextFromPDFFile() {

        ExtractTextAndImages.extractImagesAndTextFromPDFFile(assessmenetPDFFile);

    }
}