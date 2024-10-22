package com.github.edufeedai.javafx.model;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtractTextAndImagesTest {

    String assessmenetPDFFile = Dotenv.load().get("ASSESSMENT_PDF_FILES");

    @Test
    void extractImagesAndTextFromPDFFile() {

        ExtractTextAndImages.extractImagesAndTextFromPDFFile("/home/arturo/Documentos/desarrollo/edufeedai/edufeedai-javafx/src/test/resources/SXI-UD1-A1-JVS_cliente-dhcp.pdf");

    }
}