package com.github.edufeedai.javafx.model.exceptions;

public class PDFExtractTextAndImageException extends Exception{

    public PDFExtractTextAndImageException(){
        super();
    }

    public PDFExtractTextAndImageException(String message){
        super(message);
    }

    public PDFExtractTextAndImageException(Exception e){
        super(e);
    }

}
