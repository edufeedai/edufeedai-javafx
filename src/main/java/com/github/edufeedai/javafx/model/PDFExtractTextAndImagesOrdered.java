package com.github.edufeedai.javafx.model;

import com.github.edufeedai.javafx.utils.ImageBinarization;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFExtractTextAndImagesOrdered extends PDFStreamEngine {

    private ITesseract tesseract;
    private List<ContentBlock> contentBlocks; // Lista para almacenar los bloques de contenido en orden

    public PDFExtractTextAndImagesOrdered() throws IOException {
        this.contentBlocks = new ArrayList<>(); // Inicializar la lista de bloques
        this.tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        tesseract.setLanguage("eng"); // Cambia según el idioma que desees usar
        tesseract.setTessVariable("preserve_interword_spaces", "1");
        tesseract.setPageSegMode(6);
    }

    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        if ("Do".equals(operator.getName())) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xobject = getResources().getXObject(objectName);

            if (xobject instanceof PDImageXObject) {
                PDImageXObject image = (PDImageXObject) xobject;

                // Extraer imagen
                File tempImageFile = new File("image_" + System.currentTimeMillis() + ".png");
                ImageIO.write(image.getImage(), "png", tempImageFile);
                ImageBinarization.Binarize(tempImageFile.getAbsolutePath());
                System.out.println("Imagen extraída: " + tempImageFile.getAbsolutePath());

                // Realizar OCR en la imagen y añadirlo como un bloque de contenido
                try {
                    String ocrResult = tesseract.doOCR(tempImageFile);
                    contentBlocks.add(new ContentBlock("image", ocrResult)); // Añadimos el resultado OCR
                    System.out.println("Texto extraído de la imagen con OCR: \n" + ocrResult);
                } catch (TesseractException e) {
                    System.err.println("Error durante el procesamiento OCR: " + e.getMessage());
                }
            }
        } else {
            super.processOperator(operator, operands);
        }
    }

    // Método para procesar texto y agregarlo como bloque
    public void extractTextFromPage(PDPage page, PDDocument document) throws IOException {
        PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
        textStripper.setSortByPosition(true); // Ordenar por la posición

        // Configurar el área de texto a lo largo de la página
        Rectangle rect = new Rectangle(0, 0, (int) page.getMediaBox().getWidth(), (int) page.getMediaBox().getHeight());
        textStripper.addRegion("pageRegion", rect);

        textStripper.extractRegions(page);
        String pageText = textStripper.getTextForRegion("pageRegion");
        contentBlocks.add(new ContentBlock("text", pageText)); // Añadir texto
        System.out.println("Texto extraído: \n" + pageText);
    }

    public List<ContentBlock> getContentBlocks() {
        return contentBlocks; // Devuelve la lista de bloques de contenido
    }


    public void extractImagesAndTextFromPDFFile(String stringFile){

        File file = new File(stringFile);
        try (PDDocument document = Loader.loadPDF(file)) {


            // Procesar cada página del documento para extraer texto e imágenes
            for (PDPage page : document.getPages()) {
                extractTextFromPage(page, document); // Extraer texto de la página
                processPage(page); // Procesar imágenes en la página
            }

            // Imprimir los bloques de contenido en orden
            List<ContentBlock> contentBlocks = getContentBlocks();
            for (ContentBlock block : contentBlocks) {
                if (block.getType().equals("text")) {
                    System.out.println("Texto: " + block.getContent());
                } else if (block.getType().equals("image")) {
                    System.out.println("Texto de la imagen (OCR): " + block.getContent());
                }
            }

        } catch (IOException e) {
            System.out.println("Error al cargar o procesar el archivo PDF: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
