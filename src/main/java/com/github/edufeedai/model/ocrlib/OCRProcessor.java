package com.github.edufeedai.javafx.model.ocrlib;

import java.io.File;

public interface OCRProcessor {

    String performOCR(File imageFile) throws OCRProcessorException;

}
