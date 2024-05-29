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

    private String inputDirectory;

    private String outputFilePath;

    private FileFilter filter;

    public TextSerializer(String inputDirectory, String outputFilePath){
        this.inputDirectory = inputDirectory        ;
        this.outputFilePath = outputFilePath;
    }

    private List<File> listAllFiles() throws IOException {
        return Files.walk(Paths.get(inputDirectory))
                .filter(Files::isRegularFile).filter((f)->{
                    String ext = f.getFileName().toString().substring(f.getFileName().toString().lastIndexOf(".")+1);

                    return switch (ext) {
                        case "zip", "rar", "7z", "tar", "gz", "bz2", "xz" -> false;
                        default -> true;
                    };

                })
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

    public void serialize() throws IOException {
        List<File> files = listAllFiles();
        String content = concatenateFiles(files);
        writeJsonl(content, outputFilePath);
    }
}
