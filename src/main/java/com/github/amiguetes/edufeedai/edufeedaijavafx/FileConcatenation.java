package com.github.amiguetes.edufeedai.edufeedaijavafx;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileConcatenation {

    private String inputDirectory;

    private String outputFilePath;

    private FileFilter filter;

    public FileConcatenation(String inputDirectory){
        this.inputDirectory = inputDirectory        ;
        this.outputFilePath = inputDirectory + "/output.txt";
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
                .sorted()
                .collect(Collectors.toList());
    }

    private String concatenateFiles(List<File> files) throws IOException {
        StringBuilder sb = new StringBuilder();

        String lineSeparator = System.getProperties().get("line.separator").toString();

        for (File file : files) {
            sb.append(">>>{")
                    .append(file.getName())
                    .append("} ")
                    .append(lineSeparator);
            sb.append(new String(Files.readAllBytes(file.toPath())))
                    .append(lineSeparator)
                    .append(" <<<{")
                    .append(file.getName())
                    .append("}")
                    .append(lineSeparator);
        }

        return sb.toString();
    }

    private static void writeTxT(String content, String outputFilePath) throws IOException {

        Files.write(Paths.get(outputFilePath), content.getBytes());
    }

    public void serialize() throws IOException {
        List<File> files = listAllFiles();
        String content = concatenateFiles(files);
        writeTxT(content, outputFilePath);
    }

}
