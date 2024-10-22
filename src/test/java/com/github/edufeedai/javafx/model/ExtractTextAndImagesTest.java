package com.github.edufeedai.javafx.model;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

class ExtractTextAndImagesTest {

    String assessmenetPDFFile = Dotenv.load().get("ASSESSMENT_PDF_FILE");

    @Test
    void extractImagesAndTextFromPDFFile() {

        try {

            PDFExtractTextAndImagesOrdered extractTextAndImagesOrdered = new PDFExtractTextAndImagesOrdered();
            extractTextAndImagesOrdered.extractImagesAndTextFromPDFFile(assessmenetPDFFile);

        } catch (IOException e) {
            fail(e);
        }


    }
}