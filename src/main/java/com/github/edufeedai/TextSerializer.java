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

/**
 * Utility class for serializing student submissions and packaging files into JSONL format.
 * <p>
 * This class provides methods to delete JSON files, list directories, find JSON files,
 * concatenate file contents, write JSONL files, and package files for further processing.
 * </p>
 *
 * @author EduFeedAI
 */
public class TextSerializer {

    /**
     * Path to the input directory containing student submissions.
     */
    private final String inputDirectory;

    /**
     * Path to the output JSONL file.
     */
    private final String outputFilePath;

    /**
     * Constructs a TextSerializer for a given task directory.
     *
     * @param taskDirectory the directory containing student submissions
     */
    public TextSerializer(String taskDirectory) {
        this.inputDirectory = taskDirectory;
        Path path = Paths.get(taskDirectory);
        this.outputFilePath = inputDirectory + File.separator +
                path.getFileName().toString().split(" ")[0] + ".jsonl";
    }

    /**
     * Deletes all JSON files in each student's submission directory within the input directory.
     * Only affects files ending with ".json".
     */
    public void deleteAllStudentJsonFiles() {
        File submissionsRoot = new File(inputDirectory);
        File[] studentDirs = submissionsRoot.listFiles();
        if (studentDirs == null) return;
        for (File studentDir : studentDirs) {
            if (studentDir.isDirectory() && !studentDir.isHidden()) {
                File[] jsonFiles = studentDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
                if (jsonFiles == null) continue;
                for (File jsonFile : jsonFiles) {
                    jsonFile.delete();
                }
            }
        }
    }

    /**
     * Lists all first-level directories inside the input directory.
     *
     * @return a list of first-level directory File objects
     * @throws IOException if an I/O error occurs
     */
    private List<File> listFirstLevelDirectories() throws IOException {
        File inputDir = new File(inputDirectory);
        File[] files = inputDir.listFiles();
        if (files == null) return List.of();
        return Arrays.stream(files)
                .filter(File::isDirectory)
                .collect(Collectors.toList());
    }

    /**
     * Finds all JSON files within the input directory and its subdirectories (up to depth 2).
     *
     * @return a list of JSON File objects
     * @throws IOException if an I/O error occurs
     */
    private List<File> findAllJsonFiles() throws IOException {
        return Files.walk(Paths.get(inputDirectory), 2)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String fileName = path.getFileName().toString().toLowerCase();
                    int lastDot = fileName.lastIndexOf('.');
                    if (lastDot == -1) return false;
                    String ext = fileName.substring(lastDot + 1);
                    return "json".equals(ext);
                })
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    /**
     * Concatenates the contents of the given files, separating each file's content by a line separator.
     *
     * @param files the list of files to concatenate
     * @return the concatenated string content
     * @throws IOException if an I/O error occurs
     */
    private String concatenateFileContents(List<File> files) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            sb.append(Files.readString(file.toPath()))
              .append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Writes the given content to a file at the specified output path.
     *
     * @param content        the content to write
     * @param outputFilePath the path to the output file
     * @throws IOException if an I/O error occurs
     */
    private static void writeJsonlFile(String content, String outputFilePath) throws IOException {
        Files.write(Paths.get(outputFilePath), content.getBytes());
    }

    /**
     * Generates a single JSONL file from all JSON files found in the input directory.
     *
     * @throws IOException if an I/O error occurs
     */
    public void generateJsonl() throws IOException {
        List<File> jsonFiles = findAllJsonFiles();
        generateJsonl(jsonFiles.size());
    }

    /**
     * Generates multiple JSONL files, each containing up to {@code jsonsPerVolume} JSON files.
     *
     * @param jsonsPerVolume the maximum number of JSON files per JSONL volume
     * @throws IOException if an I/O error occurs
     */
    public void generateJsonl(int jsonsPerVolume) throws IOException {
        List<File> jsonFiles = findAllJsonFiles();
        int totalFiles = jsonFiles.size();
        int totalVolumes = totalFiles / jsonsPerVolume + (totalFiles % jsonsPerVolume == 0 ? 0 : 1);
        File outputFile = new File(outputFilePath);
        File outputDir = outputFile.getParentFile();
        String baseName = removeExtension(outputFile.getName());
        String extension = getExtension(outputFile.getName());
        for (int i = 0; i < totalFiles; i += jsonsPerVolume) {
            List<File> currentVolume = jsonFiles.subList(i, Math.min(i + jsonsPerVolume, totalFiles));
            String content = concatenateFileContents(currentVolume);
            String volumeFileName = String.format("%svol%dde%d.%s", baseName, (i + jsonsPerVolume) / jsonsPerVolume, totalVolumes, extension);
            File volumeFile = new File(outputDir, volumeFileName);
            writeJsonlFile(content, volumeFile.toString());
        }
    }

    /**
     * Removes the extension from a file name.
     *
     * @param fileName the file name
     * @return the file name without its extension
     */
    private static String removeExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }

    /**
     * Gets the extension from a file name.
     *
     * @param fileName the file name
     * @return the extension, or the original file name if no extension is found
     */
    private static String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(lastDotIndex + 1);
    }

    /**
     * Packages all first-level directories by serializing their contents using FileConcatenation.
     *
     * @param instructions instructions to pass to the FileConcatenation serializer
     * @throws IOException if an I/O error occurs
     */
    public void packageFiles(String instructions) throws IOException {
        List<File> directories = listFirstLevelDirectories();
        for (File dir : directories) {
            FileConcatenation fileConcatenation = new FileConcatenation(dir.getAbsolutePath(), new DigestSHA1());
            fileConcatenation.serialize(instructions);
        }
    }
}
