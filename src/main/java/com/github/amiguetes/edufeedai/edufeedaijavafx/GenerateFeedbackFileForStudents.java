package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.SubmissionIdMap;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GenerateFeedbackFileForStudents {

    private final String assessmentDirectory;

    private String moodleAssessmentFeedbackDir;

    private final String assessmentExtension;

    public GenerateFeedbackFileForStudents(String assessmentDirectory, String moodleAssessmentFeedbackDir){
        this(assessmentDirectory, moodleAssessmentFeedbackDir, "md");
    }

    public GenerateFeedbackFileForStudents(String assessmentDirectory,String moodleAssessmentFeedbackDir, String assessmentExtension){
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

            File assessmentFeedBackForStudent = new File(assessmentDirectoryFile,relFeedbackForStudent);
            File moodleFeedbackForStudent = new File(moodleFeedback,relFeedbackForStudent);

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
