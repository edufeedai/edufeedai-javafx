package com.github.edufeedai.javafx.model.openai.platform.api.exceptions;

import com.github.edufeedai.javafx.model.openai.platform.api.interfaces.exceptions.APIException;

public class OpenAIAPIException extends APIException{

    public OpenAIAPIException(){
        super();
    }

    public OpenAIAPIException(String s){
        super(s);
    }

    public OpenAIAPIException(Exception e){
        super(e);
    }

    public OpenAIAPIException(String s, Exception e){
        super(s, e);
    }

}
