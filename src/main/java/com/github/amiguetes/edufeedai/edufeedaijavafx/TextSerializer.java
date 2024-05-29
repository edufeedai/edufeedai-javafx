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

    private List<File> listAllLevel1Dirs() throws IOException {
        return Files.walk(Paths.get(inputDirectory),1)
                .filter(Files::isDirectory)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    private String concatenateFiles(List<File> files) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (File file : files) {
            sb.append(">>>{")
                    .append(file.getName())
                    .append("} ");
            sb.append(new String(Files.readAllBytes(file.toPath())))
                    .append(" <<<{")
                    .append(file.getName())
                    .append("}\n");
        }

        return sb.toString();
    }

    private static void writeJsonl(String content, String outputFilePath) throws IOException {
        List<String> jsonlLines = Arrays.asList(content.split("\n")).stream()
                .map(line -> "{\"user_message\": \"" + escapeJson(line) + "\"}")
                .collect(Collectors.toList());

        Files.write(Paths.get(outputFilePath), jsonlLines);
    }

    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
/*
    public void serialize() throws IOException {
        List<File> files = listAllFiles();
        String content = concatenateFiles(files);
        writeJsonl(content, outputFilePath);
    }*/

    public void packageFiles() throws IOException {
        List<File> files = listAllLevel1Dirs();

        for (File file : files) {
            FileConcatenation fileConcatenation = new FileConcatenation(file.getAbsolutePath());
            fileConcatenation.serialize();
        }

    }
}
