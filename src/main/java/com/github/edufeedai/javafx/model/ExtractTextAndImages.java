package com.github.edufeedai.javafx.model;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExtractTextAndImages extends PDFStreamEngine {

    // Método para procesar imágenes
    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        if ("Do".equals(operator.getName())) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xobject = getResources().getXObject(objectName);

            if (xobject instanceof PDImageXObject) {
                PDImageXObject image = (PDImageXObject) xobject;

                // Extraer imagen
                BufferedImage bImage = image.getImage();
                String imagePath = "image_" + System.currentTimeMillis() + ".png";
                ImageIO.write(bImage, "png", new File(imagePath));
                System.out.println("Imagen extraída: " + imagePath);
            }
        } else {
            super.processOperator(operator, operands);
        }
    }

    // Método para procesar texto
    public static void extractText(PDDocument document) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(document);
        System.out.println("Texto extraído: \n" + text);
    }

    public static void extractImagesAndTextFromPDFFile(String stringFile){

        try {
            File file = new File(stringFile);
            PDDocument document = Loader.loadPDF(file);

            // Extraer texto
            extractText(document);

            // Extraer imágenes
            ExtractTextAndImages extractor = new ExtractTextAndImages();
            for (PDPage page : document.getPages()) {
                extractor.processPage(page);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
