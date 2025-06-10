package com.github.edufeedai.model.ocrlib;

import java.io.File;

public interface OCRProcessor {

    String performOCR(File imageFile) throws OCRProcessorException;

}
