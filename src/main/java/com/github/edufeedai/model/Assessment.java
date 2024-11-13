package com.github.edufeedai.javafx.model;

public class Assessment {

    private final String id;

    private final String gradingCriteria;

    private final String taskSubmitted;

    private String assesmentFeedback;

    protected Assessment(AssessmentBase assessmentBase) {

        this(assessmentBase.getId(), assessmentBase.getGradingCriteria(), assessmentBase.getTaskSubmitted());

    }

    protected Assessment(String id, String gradingCriteria, String taskSubmitted) {
        this.id = id;
        this.gradingCriteria = gradingCriteria;
        this.taskSubmitted = taskSubmitted;
    }

    public String getId() {
        return id;
    }

    public String getGradingCriteria() {
        return gradingCriteria;
    }

    public String getTaskSubmitted() {
        return taskSubmitted;
    }

    public String getAssesmentFeedback() {
        return assesmentFeedback;
    }

    protected void setAssesmentFeedback(String assesmentFeedback) {
        this.assesmentFeedback = assesmentFeedback;
    }


}
