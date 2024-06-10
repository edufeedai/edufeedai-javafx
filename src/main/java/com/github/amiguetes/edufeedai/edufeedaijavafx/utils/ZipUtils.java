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

    public static void compressAndRemoveDirectories(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        File[] directories = dir.listFiles(File::isDirectory);

        if (directories != null) {
            for (File directory : directories) {
                String dirName = directory.getName();
                String zipFileName = dirName + ".zip";

                try (FileOutputStream fos = new FileOutputStream(zipFileName);
                     ZipOutputStream zos = new ZipOutputStream(fos)) {

                    zipDirectory(directory, directory.getName(), zos);
                }

                deleteDirectory(directory);
            }
        }
    }

    private static void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }

            zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

            Files.copy(file.toPath(), zos);

            zos.closeEntry();
        }
    }

    private static void deleteDirectory(File directory) throws IOException {
        Files.walk(directory.toPath())
             .map(Path::toFile)
             .forEach(File::delete);
    }


}
