package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.Body;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.JSONLine;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.Message;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class FileConcatenation {

    private final String inputDirectory;

    private final String outputFileName;

    private final String sha1;

    private FileFilter filter;

    public FileConcatenation(String inputDirectory){

        this.inputDirectory = inputDirectory;

        Path path = Paths.get(inputDirectory);
        sha1 = sha1(path.getFileName().toString());

         this.outputFileName = inputDirectory + File.separator + sha1 + ".txt";
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

    private String sha1(String input)  {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = messageDigest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "No SHA1 found";
        }

    }

    public void serialize() throws IOException {
        List<File> files = listAllFiles();
        String content = concatenateFiles(files);

        JSONLine jsonLine = new JSONLine();
        jsonLine.setCustom_id(sha1);
        jsonLine.setMethod("POST");
        jsonLine.setUrl("v1/chat/completions");

        Body body = new Body();

        body.setModel("gpt-3.5-turbo");

        Message[] messages = new Message[2];
        messages[0] = new Message();
        messages[0].setRole("system");
        messages[0].setContent("Eres el profesor de lenguaje de marcas y has de hacer comentarios acerca de las entregas" +
                "de los alumnos de las siguientes tareas");
        messages[1] = new Message();
        messages[1].setRole("user");
        messages[1].setContent(content);

        body.setMessages(messages);

        jsonLine.setBody(body);
        jsonLine.setMax_tokens(1000);

        Gson gson = new Gson();
        String s = gson.toJson(jsonLine);

        Files.write(Paths.get(outputFileName + ".json"), s.getBytes());

        writeTxT(content, outputFileName);
    }

}
