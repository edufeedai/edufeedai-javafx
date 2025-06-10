package com.github.edufeedai.model.exceptions;

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
