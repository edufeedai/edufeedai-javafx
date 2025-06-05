package com.github.edufeedai;

import java.io.File;
import java.security.DigestException;
import static java.util.Arrays.stream;
import java.util.Comparator;
import java.util.List;

import com.github.edufeedai.model.Digest;
import com.github.edufeedai.model.SubmissionIdMap;
import com.google.gson.Gson;

/**
 * Clase responsable de generar y guardar mapas de IDs de entregas de estudiantes
 * a partir de los nombres de carpeta en un directorio de evaluación.
 */
public class GenerateSubmissionIDMap {
    /** Ruta al directorio de la evaluación. */
    private String assessmentFolder;
    /** Objeto Digest para generar IDs únicos. */
    private Digest digest;

    /**
     * Constructor.
     * @param assessmentFolder Ruta al directorio de la evaluación
     * @param digest Objeto Digest para generar IDs únicos
     */
    public GenerateSubmissionIDMap(String assessmentFolder, Digest digest) {
        this.assessmentFolder = assessmentFolder;
        this.digest = digest;
    }

    /**
     * Genera un arreglo de SubmissionIdMap a partir de las carpetas de estudiantes.
     * @return Arreglo de SubmissionIdMap ordenado por custom_id
     */
    public SubmissionIdMap[] generateSubmissionIDMaps() {
        File folder = new File(assessmentFolder);
        File[] files = folder.listFiles((f) -> f.isDirectory() && !f.isHidden());
        if (files == null) return new SubmissionIdMap[0];
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
        List<SubmissionIdMap> lista = stream(submissionIdMaps)
            .sorted(Comparator.comparing(SubmissionIdMap::getCustom_id))
            .toList();
        return lista.toArray(new SubmissionIdMap[0]);
    }

    /**
     * Guarda el arreglo de SubmissionIdMap en un archivo JSON dentro del directorio de evaluación.
     * @param submissionIdMaps Arreglo de mapas a guardar
     * @param assessmentIdMapFile Nombre del archivo de salida
     * @return Nombre del archivo guardado, o null si hubo error
     */
    public String saveSubmissionIDMaps(SubmissionIdMap[] submissionIdMaps, String assessmentIdMapFile) {
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
