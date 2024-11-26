package com.github.edufeedai;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestException;
import java.util.ArrayList;
import java.util.List;

import com.github.edufeedai.model.Digest;
import com.github.edufeedai.model.openai.platform.Body;
import com.github.edufeedai.model.openai.platform.JSONLine;
import com.github.edufeedai.model.openai.platform.Message;
import com.google.gson.Gson;

public class FileConcatenation implements FilenameFilter {

    private final String inputDirectory;

    private final String outputFileName;

    private final String sha1;

    public FileConcatenation(String inputDirectory, Digest digest){
        String sha11;

        this.inputDirectory = inputDirectory;

        Path path = Paths.get(inputDirectory);


        try {
            sha11 = digest.digest(path.getFileName().toString());
        } catch (DigestException e) {
            sha11 = "errorgerneratingname";
        }

        sha1 = sha11;

        this.outputFileName = inputDirectory + File.separator + sha1 + ".json";
    }

    private List<File> listAllFiles(File dir) {
        List<File> files = new ArrayList<>();

        for (File file : dir.listFiles(this)) {

            if (file.isDirectory()) {
                files.addAll(listAllFiles(file)); // llamada recursiva para subdirectorios
            } else if (file.isFile()) {
                files.add(file);
            }

        }

        return files;
    }

    private List<File> listAllFiles() {

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

    @Override
    public boolean accept(File dir, String name) {

        File hijo = new File(dir,name);

        if (hijo.isHidden()){
            return false;
        }

        if (name.equals("git-book")){
            return false;
        }

        String ext = name.substring(name.lastIndexOf(".")+1).toLowerCase();

        switch (name){
            case "package.json",
                 "package-lock.json",
                 "node_modules",
                 "tsconfig.json",
                 "app.js",
                 "LICENSE",
                 "Dockerfile":

            return false;
        }

        if (!ext.isEmpty()){

            switch (ext){

                case "jpg",
                    "jpeg",
                    "png",
                    "gif",
                    "bmp",
                    "tiff",
                    "svg",
                    "webp",
                    "ico",
                     "mp4",
                     "webm",
                     "ogg",
                     "mp3",
                     "wav",
                     "flac",
                     "aac",
                     "wma",
                     "m4a",
                     "opus",
                     "pdf",
                     "doc",
                     "docx",
                     "xls",
                     "xlsx",
                     "ppt",
                     "pptx",
                     "zip",
                        "rar",
                        "tar",
                        "gz",
                        "7z",
                        "bz2",
                        "xz":
                case "xml":


                    return false;

            }

        }

        return true;
    }

}
