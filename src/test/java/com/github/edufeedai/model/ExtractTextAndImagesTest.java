package com.github.edufeedai.model;

import com.github.edufeedai.model.exceptions.PDFExtractTextAndImageException;
import com.github.edufeedai.model.ocrlib.OCRProcessor;
import com.github.edufeedai.model.ocrlib.OCRProcessorTesseract;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class ExtractTextAndImagesTest {

    String assessmenetPDFFile = Dotenv.load().get("ASSESSMENT_PDF_FILE");

    @Test
    @DisplayName("Extraer imágenes y texto de un archivo PDF con Tesseract")
    void extractImagesAndTextFromPDFFileTesseract() {

        try {

            OCRProcessor ocrProcessor = new OCRProcessorTesseract();

            PDFExtractTextAndImagesOrdered extractTextAndImagesOrdered = new PDFExtractTextAndImagesOrdered(ocrProcessor);
            extractTextAndImagesOrdered.extractImagesAndTextFromPDFFile(assessmenetPDFFile);

        } catch (PDFExtractTextAndImageException e) {
            fail(e);
        }

    }
}