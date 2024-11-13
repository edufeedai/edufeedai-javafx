package com.github.edufeedai.javafx;

import java.io.File;

import com.github.edufeedai.javafx.model.SubmissionIdMap;

public class GenerateMoodleZipFeedbackAssessmentFile {

    private final String assessmentDirectory;

    private String moodleAssessmentFeedbackDir;

    private final String assessmentExtension;

    public GenerateMoodleZipFeedbackAssessmentFile(String assessmentDirectory, String moodleAssessmentFeedbackDir){
        this(assessmentDirectory, moodleAssessmentFeedbackDir, "md");
    }

    public GenerateMoodleZipFeedbackAssessmentFile(String assessmentDirectory, String moodleAssessmentFeedbackDir, String assessmentExtension){
        this.assessmentDirectory = assessmentDirectory;
        this.moodleAssessmentFeedbackDir = moodleAssessmentFeedbackDir;
        this.assessmentExtension = assessmentExtension;
    }

    public void generateFeedbackFile(SubmissionIdMap[] submissionIdMaps){

        File assessmentDirectoryFile = new File(assessmentDirectory);
        File moodleFeedback = new File(moodleAssessmentFeedbackDir);

        for (SubmissionIdMap submissionIdMap : submissionIdMaps){

            String customId = submissionIdMap.getCustom_id();

            String relFeedbackForStudent = customId + File.separator + customId + "." + assessmentExtension;
            String moodleFile = customId + "." + assessmentExtension;

            File assessmentFeedBackForStudent = new File(assessmentDirectoryFile,relFeedbackForStudent);
            File moodleFeedbackForStudent = new File(moodleFeedback,moodleFile);

            try {
                if (assessmentFeedBackForStudent.exists()) {
                    if (!moodleFeedbackForStudent.getParentFile().exists()) {
                        moodleFeedbackForStudent.getParentFile().mkdirs();
                    }
                    java.nio.file.Files.copy(assessmentFeedBackForStudent.toPath(), moodleFeedbackForStudent.toPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        }






    }

}
