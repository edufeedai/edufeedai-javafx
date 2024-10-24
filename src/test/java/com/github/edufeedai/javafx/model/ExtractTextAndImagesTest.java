package com.github.edufeedai.javafx.model;

import com.github.edufeedai.javafx.model.exceptions.PDFExtractTextAndImageException;
import com.github.edufeedai.javafx.model.ocrlib.OCRProcessor;
import com.github.edufeedai.javafx.model.ocrlib.OCRProcessorAzure;
import com.github.edufeedai.javafx.model.ocrlib.OCRTesseract;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

class ExtractTextAndImagesTest {

    String assessmenetPDFFile = Dotenv.load().get("ASSESSMENT_PDF_FILE");

    String azureCognitiveServicesKey = Dotenv.load().get("AZURE_COGNITIVE_SERVICES_KEY");
    String azureCognitiveServicesEndpoint = Dotenv.load().get("AZURE_COGNITIVE_SERVICES_ENDPOINT");

    @Test
    @DisplayName("Extraer imágenes y texto de un archivo PDF con Tesseract")
    void extractImagesAndTextFromPDFFileTesseract() {

        try {

            OCRProcessor ocrProcessor = new OCRProcessorAzure(azureCognitiveServicesKey, azureCognitiveServicesEndpoint);

            PDFExtractTextAndImagesOrdered extractTextAndImagesOrdered = new PDFExtractTextAndImagesOrdered(ocrProcessor);
            extractTextAndImagesOrdered.extractImagesAndTextFromPDFFile(assessmenetPDFFile);

        } catch (PDFExtractTextAndImageException e) {
            fail(e);
        }

    }

    @Test
    @DisplayName("Extraer imágenes y texto de un archivo PDF con Azure Cognitive Services")
    void extractImagesAndTextFromPDFFileAzure() {

        try {

            OCRProcessor ocrProcessor = new OCRTesseract();

            PDFExtractTextAndImagesOrdered extractTextAndImagesOrdered = new PDFExtractTextAndImagesOrdered(ocrProcessor);
            extractTextAndImagesOrdered.extractImagesAndTextFromPDFFile(assessmenetPDFFile);

        } catch (PDFExtractTextAndImageException e) {
            fail(e);
        }

    }
}