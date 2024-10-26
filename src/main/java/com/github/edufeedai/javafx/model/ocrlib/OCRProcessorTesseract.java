package com.github.edufeedai.javafx.model.ocrlib;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;

public class OCRProcessorTesseract implements OCRProcessor {

    final private ITesseract tesseract;

    public OCRProcessorTesseract() {

        this.tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        tesseract.setLanguage("eng"); // Cambia según el idioma que desees usar
        tesseract.setTessVariable("preserve_interword_spaces", "1");
        tesseract.setPageSegMode(6);
        tesseract.setTessVariable("user_defined_dpi", "300"); //PDFBox no proporciona DPI, por lo que es necesario definirlo
        //tesseract.setTessVariable("tessedit_char_whitelist", "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,:/-_@()[]{}#$%&*=+><|!?~");

    }

    @Override
    public String performOCR(File imageFile) throws OCRProcessorException {

        try {

            OCROpenCVImagePreprocess.Binarize(imageFile.getAbsolutePath());
            return tesseract.doOCR(imageFile);

        } catch (Exception e) {

            throw new OCRProcessorException(e);

        }
    }

}
