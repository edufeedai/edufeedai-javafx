package com.github.amiguetes.edufeedai.edufeedaijavafx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.Digest;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.Body;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.JSONLine;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.Message;
import com.google.gson.Gson;

public class FileConcatenation {

    private final String inputDirectory;

    private final String outputFileName;

    private final String sha1;

    public FileConcatenation(String inputDirectory, Digest digest){
        String sha11;

        this.inputDirectory = inputDirectory;

        Path path = Paths.get(inputDirectory);


        try {
            sha11 = digest.digest(path.getFileName().toString());
        } catch (NoSuchAlgorithmException e) {
            sha11 = "errorgerneratingname";
        }

        sha1 = sha11;

        this.outputFileName = inputDirectory + File.separator + sha1 + ".json";
    }

    private List<File> listAllFiles(File dir) {
        List<File> files = new ArrayList<>();

        for (File file : dir.listFiles()) {
            if (file.isDirectory() && !file.isHidden()) {
                files.addAll(listAllFiles(file)); // llamada recursiva para subdirectorios
            } else if (file.isFile()) {
                files.add(file);
            }
        }

        return files;
    }

    private List<File> listAllFiles() throws IOException {

        File input = new File(inputDirectory);
        return listAllFiles(input);

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

    protected void serialize(String instructions) throws IOException {
        List<File> files = listAllFiles();
        String content = concatenateFiles(files);

        JSONLine jsonLine = new JSONLine();
        jsonLine.setCustom_id(sha1);
        jsonLine.setMethod("POST");
        jsonLine.setUrl("/v1/chat/completions");

        Body body = new Body();

        body.setModel("gpt-4o");

        Message[] messages = new Message[2];
        messages[0] = new Message();
        messages[0].setRole("system");
        messages[0].setContent(instructions);
        messages[0].setMax_tokens(1000);
        messages[1] = new Message();
        messages[1].setRole("user");
        messages[1].setContent(content);
        messages[1].setMax_tokens(1000);

        body.setMessages(messages);

        jsonLine.setBody(body);


        Gson gson = new Gson();
        String s = gson.toJson(jsonLine);

        Files.write(Paths.get(outputFileName ), s.getBytes());
    }

}
