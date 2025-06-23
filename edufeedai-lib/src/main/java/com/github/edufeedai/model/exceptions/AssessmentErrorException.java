package com.github.edufeedai.model.exceptions;

public class AssessmentErrorException extends Exception {

    public AssessmentErrorException(){
        super();
    }

    public AssessmentErrorException(Exception e) {
        super(e);
    }
}
