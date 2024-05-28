package com.github.amiguetes.edufeedai.edufeedaijavafx.model;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.exceptions.AssessmentErrorException;

public class CheckResults {

    private static CheckResults _instance;
    private static int AssesmentId = 0;

    public static synchronized CheckResults getInstance(){
        if (null == _instance){
            _instance = new CheckResults();
        }
        return _instance;
    }

    private CheckResults(){

    }

    public Assessment createNewAssessment(String gradingCriteria,String taskSubmitted){

       return new Assessment(String.valueOf(++CheckResults.AssesmentId),gradingCriteria,taskSubmitted);

    }

    public void gradeAssessment(Assessment assessment) throws AssessmentErrorException {

        throw new UnsupportedOperationException("Not supported yet.");

    }

}
