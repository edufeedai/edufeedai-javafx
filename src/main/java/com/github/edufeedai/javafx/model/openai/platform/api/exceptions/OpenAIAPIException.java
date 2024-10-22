package com.github.edufeedai.javafx.model.openai.platform.api.exceptions;

public class OpenAIAPIException extends Exception{

    public OpenAIAPIException(){
        super();
    }

    public OpenAIAPIException(String s){
        super(s);
    }

    public OpenAIAPIException(Exception e){
        super(e);
    }

}
