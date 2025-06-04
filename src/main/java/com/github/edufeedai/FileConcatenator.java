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

/**
 * Utility class for recursively concatenating the contents of files in a directory,
 * filtering out unwanted files, and serializing the result as a JSON line for OpenAI platform usage.
 *
 * <p>This class implements {@link FilenameFilter} to control which files are included.
 * It generates a unique output file name based on a digest of the input directory name.</p>
 */
public class FileConcatenator implements FilenameFilter {

    /** Directory to scan for input files. */
    private final String inputDirectory;
    /** Output file name (full path) for the concatenated result. */
    private final String outputFilePath;
    /** SHA-1 digest string used for output file naming. */
    private final String sha1Digest;

    /**
     * Constructs a FileConcatenator for the given directory and digest generator.
     *
     * @param inputDirectory the directory to scan for files
     * @param digest         the digest generator to create a unique output file name
     */
    public FileConcatenator(String inputDirectory, Digest digest) {
        this.inputDirectory = inputDirectory;
        String sha1;
        Path path = Paths.get(inputDirectory);
        try {
            sha1 = digest.digest(path.getFileName().toString());
        } catch (DigestException e) {
            sha1 = "errorgeneratingname";
        }
        this.sha1Digest = sha1;
        this.outputFilePath = inputDirectory + File.separator + sha1Digest + ".json";
    }

    /**
     * Recursively lists all files in the given directory that are accepted by this filter.
     *
     * @param directory the directory to scan
     * @return a list of accepted files (recursively)
     */
    private List<File> listAllAcceptedFiles(File directory) {
        List<File> files = new ArrayList<>();
        File[] children = directory.listFiles(this);
        if (children != null) {
            for (File file : children) {
                if (file.isDirectory()) {
                    files.addAll(listAllAcceptedFiles(file));
                } else if (file.isFile()) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * Lists all accepted files in the input directory (recursively).
     *
     * @return a list of accepted files
     */
    private List<File> listAllAcceptedFiles() {
        File inputDir = new File(inputDirectory);
        return listAllAcceptedFiles(inputDir);
    }

    /**
     * Concatenates the contents of the given files, wrapping each file's content with markers.
     *
     * @param files the list of files to concatenate
     * @return a string with all file contents concatenated and marked
     * @throws IOException if reading any file fails
     */
    private String concatenateFileContents(List<File> files) throws IOException {
        StringBuilder sb = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        for (File file : files) {
            sb.append(">>>{")
              .append(file.getName())
              .append("} ")
              .append(lineSeparator);
            sb.append(new String(Files.readAllBytes(file.toPath())))
              .append(lineSeparator)
              .append("<<<{")
              .append(file.getName())
              .append("}")
              .append(lineSeparator);
        }
        return sb.toString();
    }

    /**
     * Serializes the concatenated file contents and instructions into a JSON line file for OpenAI platform.
     *
     * @param instructions the system instructions to include as the first message
     * @throws IOException if file writing fails
     */
    protected void serializeToJson(String instructions) throws IOException {
        List<File> files = listAllAcceptedFiles();
        String content = concatenateFileContents(files);

        JSONLine jsonLine = new JSONLine();
        jsonLine.setCustom_id(sha1Digest);
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
        String json = gson.toJson(jsonLine);
        Files.write(Paths.get(outputFilePath), json.getBytes());
    }

    /**
     * Determines whether a file should be accepted for concatenation based on its name and extension.
     *
     * <p>Excludes hidden files, certain system files, and files with specific extensions.</p>
     *
     * @param dir  the directory in which the file was found
     * @param name the name of the file
     * @return true if the file should be included; false otherwise
     */
    @Override
    public boolean accept(File dir, String name) {
        File candidate = new File(dir, name);
        if (candidate.isHidden()) {
            return false;
        }
        if (name.equals("git-book")) {
            return false;
        }
        // Exclude specific file names
        switch (name) {
            case "package.json":
            case "package-lock.json":
            case "node_modules":
            case "tsconfig.json":
            case "app.js":
            case "LICENSE":
            case "Dockerfile":
                return false;
            default:
                break;
        }
        // Exclude files by extension
        int lastDot = name.lastIndexOf('.');
        if (lastDot > 0 && lastDot < name.length() - 1) {
            String ext = name.substring(lastDot + 1).toLowerCase();
            switch (ext) {
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                case "bmp":
                case "tiff":
                case "svg":
                case "webp":
                case "ico":
                case "mp4":
                case "webm":
                case "ogg":
                case "mp3":
                case "wav":
                case "flac":
                case "aac":
                case "wma":
                case "m4a":
                case "opus":
                case "pdf":
                case "doc":
                case "docx":
                case "xls":
                case "xlsx":
                case "ppt":
                case "pptx":
                case "zip":
                case "rar":
                case "tar":
                case "gz":
                case "7z":
                case "bz2":
                case "xz":
                case "xml":
                    return false;
                default:
                    break;
            }
        }
        return true;
    }
}
