package com.github.edufeedai.model;

import com.github.edufeedai.model.exceptions.PDFExtractTextAndImageException;
import com.github.edufeedai.model.ocrlib.OCRProcessor;
import com.github.edufeedai.model.ocrlib.OCRProcessorException;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PDFExtractTextAndImagesOrdered extends PDFStreamEngine {

    private OCRProcessor ocrProcessor;
    private List<ContentBlock> contentBlocks; // Lista para almacenar los bloques de contenido en orden

    public PDFExtractTextAndImagesOrdered(OCRProcessor processor) throws PDFExtractTextAndImageException{

        ocrProcessor = processor;
        this.contentBlocks = new ArrayList<>(); // Inicializar la lista de bloques

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

                try {

                    String ocrResult = ocrProcessor.performOCR(tempImageFile); // Realizar OCR en la imagen
                    contentBlocks.add(new ContentBlock("image", ocrResult)); // Añadimos el resultado OCR

                } catch (OCRProcessorException e) {

                        throw new IOException(e);

                }

                Files.delete(tempImageFile.toPath());

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
        contentBlocks.add(new ContentBlock("text", pageText));

    }

    public List<ContentBlock> getContentBlocks() {
        return contentBlocks; // Devuelve la lista de bloques de contenido
    }


    public void extractImagesAndTextFromPDFFile(String stringFile) throws PDFExtractTextAndImageException{

        File file = new File(stringFile);

        String newFileName = file.getAbsolutePath().replaceFirst("[.][^.]+$", ".txt");
        File newFile = new File(newFileName);


        try (PDDocument document = Loader.loadPDF(file)) {


            // Procesar cada página del documento para extraer texto e imágenes
            for (PDPage page : document.getPages()) {
                extractTextFromPage(page, document); // Extraer texto de la página
                processPage(page); // Procesar imágenes en la página
            }

            //Guardar en un fichero los bloques de contenido en orden

            try (PrintWriter pw = new PrintWriter(new FileWriter(newFile),true)){

                getContentBlocks().stream().forEach( pw::println );

            } catch (IOException e) {
                throw new PDFExtractTextAndImageException(e);
            }

        } catch (IOException e) {

            throw new PDFExtractTextAndImageException(e);

        }

    }

}
