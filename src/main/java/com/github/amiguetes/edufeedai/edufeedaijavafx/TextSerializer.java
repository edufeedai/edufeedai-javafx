package com.github.amiguetes.edufeedai.edufeedaijavafx;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextSerializer {

    private final String inputDirectory;

    private final String outputFilePath;

    private FileFilter filter;

    public TextSerializer(String taskDirectory){
        this.inputDirectory = taskDirectory;

        Path path = Paths.get(taskDirectory);
        this.outputFilePath = inputDirectory + File.separator + path.getFileName().toString() + ".jsonl";
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
        String content = concatenateFiles(files);
        writeJsonl(content, outputFilePath);
    }

    public void packageFiles() throws IOException {
        List<File> files = listAllLevel1Dirs();

        for (File file : files) {
            FileConcatenation fileConcatenation = new FileConcatenation(file.getAbsolutePath());
            fileConcatenation.serialize();
        }

    }
}
