package com.github.edufeedai;

import java.io.File;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import static java.util.Arrays.stream;
import java.util.Comparator;
import java.util.List;

import com.github.edufeedai.model.Digest;
import com.github.edufeedai.model.SubmissionIdMap;
import com.google.gson.Gson;

public class GenerateSubmissionIDMap {

    String assessmentFolder;
    Digest digest;

    public GenerateSubmissionIDMap(String assessmentFolder, Digest digest){
        this.assessmentFolder = assessmentFolder;
        this.digest = digest;
    }

    public SubmissionIdMap[] generateSubmissionIDMaps(){

        File folder = new File(assessmentFolder);

        File[] files = folder.listFiles((f)->f.isDirectory() && !f.isHidden());
        SubmissionIdMap[] submissionIdMaps = new SubmissionIdMap[files.length];

        for (int i = 0; i < files.length; i++) {
            SubmissionIdMap submissionIdMap = new SubmissionIdMap();
            submissionIdMap.setCustom_id(files[i].getName());

            try {
                submissionIdMap.setSubmission_id(digest.digest(files[i].getName()));
            } catch (DigestException e) {
                submissionIdMap.setSubmission_id("errorgeneratingname");
            }

            submissionIdMaps[i] = submissionIdMap;
        }

        List<SubmissionIdMap> lista = stream(submissionIdMaps).sorted(Comparator.comparing(SubmissionIdMap::getCustom_id)).toList();

        return lista.toArray(submissionIdMaps);
    }

    public String saveSubmissionIDMaps(SubmissionIdMap[] submissionIdMaps, String assessmentIdMapFile){
        Gson gson = new Gson();

        String json = gson.toJson(submissionIdMaps);

        File file = new File(assessmentFolder + File.separator + assessmentIdMapFile);

        try {
            java.nio.file.Files.writeString(file.toPath(), json);
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
