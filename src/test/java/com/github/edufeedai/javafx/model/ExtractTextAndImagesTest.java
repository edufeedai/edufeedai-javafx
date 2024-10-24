package com.github.edufeedai.javafx.model;

import com.github.edufeedai.javafx.model.exceptions.PDFExtractTextAndImageException;
import com.github.edufeedai.javafx.model.ocrlib.OCRProcessor;
import com.github.edufeedai.javafx.model.ocrlib.OCRTesseract;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

class ExtractTextAndImagesTest {

    String assessmenetPDFFile = Dotenv.load().get("ASSESSMENT_PDF_FILE");

    @Test
    void extractImagesAndTextFromPDFFile() {

        try {

            OCRProcessor ocrProcessor = new OCRTesseract();

            PDFExtractTextAndImagesOrdered extractTextAndImagesOrdered = new PDFExtractTextAndImagesOrdered(ocrProcessor);
            extractTextAndImagesOrdered.extractImagesAndTextFromPDFFile(assessmenetPDFFile);

        } catch (PDFExtractTextAndImageException e) {
            fail(e);
        }

    }
}