package com.github.edufeedai;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.edufeedai.model.DigestSHA1;

public class TextSerializer {

    private final String inputDirectory;

    private final String outputFilePath;

    public TextSerializer(String taskDirectory){
        this.inputDirectory = taskDirectory;

        Path path = Paths.get(taskDirectory);
        this.outputFilePath = inputDirectory + File.separator + path.getFileName().toString().split(" ")[0] + ".jsonl";
    }

    public void deleteAllStudentsJSONOpenAIJob(){

        File studentsSubmissions = new File(inputDirectory);

        for (File studentSubmission : studentsSubmissions.listFiles()) {
            if (studentSubmission.isDirectory() && !studentSubmission.isHidden()){
                File[] submissions = studentSubmission.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
                for (File submission : submissions) {
                    submission.delete();

                }
            }

        }

    }

    private List<File> listAllLevel1Dirs() throws IOException{

        File input = new File(inputDirectory);

        return Arrays.stream(input.listFiles())
                .filter(File::isDirectory)
                .collect(Collectors.toList());


    }

    private List<File> findAllJsons() throws IOException {
        return Files.walk(Paths.get(inputDirectory),2)
                .filter(Files::isRegularFile)
                .filter((f)->{
                    String ext = f.getFileName().toString().substring(f.getFileName().toString().toLowerCase().lastIndexOf(".")+1);

                    return switch (ext) {
                        case "json" -> true;
                        default -> false;
                    };

                })
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    private String concatenateFiles(List<File> files) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (File file : files) {
            sb.append(new String(Files.readAllBytes(file.toPath())))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static void writeJsonl(String content, String outputFilePath) throws IOException {

        Files.write(Paths.get(outputFilePath), content.getBytes());
    }

    public void generateJSONL() throws IOException {
        List<File> files = findAllJsons();
        generateJSONL(files.size());
    }

    public void generateJSONL(int jsonsPerVolume) throws IOException{
        List<File> files = findAllJsons();

        for (int i = 0; i < files.size(); i+=jsonsPerVolume){

            List<File> currentVolume = files.subList(i, Math.min(i + jsonsPerVolume,files.size()));
            String currentContent = concatenateFiles(currentVolume);


            File outputFilePathFile = new File(outputFilePath);
            File outputParentFilePath = outputFilePathFile.getParentFile();

            String file = removeExtension(outputFilePathFile.getName());
            String ext = getExtension(outputFilePathFile.getName());

            int totalFiles = files.size()/jsonsPerVolume + (files.size()%jsonsPerVolume == 0 ? 0 : 1);

            writeJsonl(currentContent,new File(
                    outputParentFilePath,
                    String.format("%svol%dde%d.%s",file,(i+jsonsPerVolume)/jsonsPerVolume,totalFiles,ext).toString()
            ).toString());

        }

    }

    private static String removeExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName; // No extension found
        }

        return fileName.substring(0, lastDotIndex);
    }

    private static String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName; // No extension found
        }

        return fileName.substring(lastDotIndex + 1);
    }

    public void packageFiles(String instructions) throws IOException {
        List<File> files = listAllLevel1Dirs();

        for (File file : files) {
            FileConcatenation fileConcatenation = new FileConcatenation(file.getAbsolutePath(), new DigestSHA1());
            fileConcatenation.serialize(instructions);
        }

    }
}
