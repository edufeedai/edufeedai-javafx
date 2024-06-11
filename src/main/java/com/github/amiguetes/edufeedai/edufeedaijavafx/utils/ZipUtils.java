package com.github.amiguetes.edufeedai.edufeedaijavafx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void unzipAndRemove(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".zip"));

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String destDir = fileName.substring(0, fileName.lastIndexOf("."));

                File destDirFile = new File(destDir);
                if (!destDirFile.exists()) {
                    destDirFile.mkdir();
                }

                try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
                    ZipEntry zipEntry;
                    while ((zipEntry = zis.getNextEntry()) != null) {
                        File newFile = newFile(destDirFile, zipEntry);
                        if (zipEntry.isDirectory()) {
                            if (!newFile.isDirectory() && !newFile.mkdirs()) {
                                throw new IOException("Failed to create directory " + newFile);
                            }
                        } else {
                            File parent = newFile.getParentFile();
                            if (!parent.isDirectory() && !parent.mkdirs()) {
                                throw new IOException("Failed to create directory " + parent);
                            }

                            Files.copy(zis, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                        zis.closeEntry();
                    }
                }

                file.delete();
            }
        }
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    /**
     * Compress all files with the given file extension in the given directory and remove the directories
     * @param path the path of the directory
     * @param fileExtension the file extension of the files to compress
     * @throws IOException if an I/O error occurs
     * 
     */
    public static void compressFileAndRemoveDirectories(Path path, String fileExtension) throws IOException {

        File[] files = path.toFile().listFiles((d, name) -> name.toLowerCase().endsWith(fileExtension));

        File outputZip = new File(path.toString() + ".zip");

        if (files != null) {

            try (FileOutputStream fos = new FileOutputStream(outputZip);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                for (File file : files) {
                    zos.putNextEntry(new ZipEntry(file.getName()));

                    Files.copy(file.toPath(), zos);

                    zos.closeEntry();
                }
            }

            deleteDirectory(path.toFile());
        }
    }

    private static void deleteDirectory(File directory) throws IOException {
        Files.walk(directory.toPath())
             .map(Path::toFile)
             .forEach(File::delete);
        directory.delete();
    }


}
