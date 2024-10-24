package com.github.edufeedai.javafx.model.ocrlib;

import com.github.edufeedai.javafx.utils.ImageBinarization;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;

public class OCRTesseract implements OCRProcessor {

    final private ITesseract tesseract;

    public OCRTesseract() {

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

            ImageBinarization.Binarize(imageFile.getAbsolutePath());
            return tesseract.doOCR(imageFile);

        } catch (Exception e) {

            throw new OCRProcessorException(e);

        }
    }

}
