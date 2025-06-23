package com.github.edufeedai.model.ocrlib;

public class OCRProcessorException extends Exception {

    public OCRProcessorException(Exception e) {
        super(e);
    }

    public OCRProcessorException(String message) {
        super(message);
    }

    public OCRProcessorException(String message, Exception e) {
        super(message, e);
    }

    public OCRProcessorException() {
        super();
    }

}
