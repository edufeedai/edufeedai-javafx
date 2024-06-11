package com.github.amiguetes.edufeedai.edufeedaijavafx.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ZipUtilsTest {

    @TempDir
    Path tempDir;

    Path testDirectoryPath;

    private final String testDirPath = "testDir";
    private final String zipDirPath = "zipDir";

    private final String testDirFile = testDirPath + ".zip";
    private final String zipDirFile = zipDirPath + ".zip";

    private final String testFile1 = "file1.txt";
    private final String testFile2 = "file2.txt";
    private final String testFile3 = "file3.txt";
    private final String testFile4 = "file4.txt";

    @BeforeEach
    public void setUp() throws IOException {

        String testDirPathString = "test-"  + getClass().getName() + "-" + System.currentTimeMillis();
        testDirectoryPath = tempDir.resolve(testDirPathString);
        Files.createDirectory(testDirectoryPath);

        // Crear directorio de prueba
        Path testPath = testDirectoryPath.resolve(testDirPath);
        Files.createDirectories(testPath);

        // Crear algunos archivos en el directorio de prueba
        Files.createFile(testPath.resolve(testFile1));
        Files.createFile(testPath.resolve(testFile2));

        // Crear directorio zipDir y algunos archivos en él
        Path zipPath = testDirectoryPath.resolve(zipDirPath);
        Files.createDirectories(zipPath);

        Files.createFile(zipPath.resolve(testFile3));
        Files.createFile(zipPath.resolve(testFile4));

        // Comprimir el directorio de prueba en un archivo ZIP
        try (FileOutputStream fos = new FileOutputStream(testDirectoryPath.resolve(testDirFile).toFile());
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            zipDirectory(testPath, testPath.getFileName().toString(), zos);
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Eliminar directorios y archivos de prueba
        deleteDirectory(testDirectoryPath);
    }

    @Test
    public void testUnzipAndRemove() throws IOException {
        // Ejecutar el método de descomprimir y eliminar
        ZipUtils.unzipAndRemove(testDirectoryPath.toString());

        // Verificar que el archivo ZIP haya sido eliminado
        assertFalse(Files.exists(testDirectoryPath.resolve(testDirFile)));

        // Verificar que el directorio haya sido descomprimido correctamente

        String relFile1 = testDirPath + File.separator + testFile1;
        String relFile2 = testDirPath + File.separator + testFile2;

        assertTrue(Files.exists(testDirectoryPath.resolve(relFile1)));
        assertTrue(Files.exists(testDirectoryPath.resolve(relFile2)));
    }

    @Test
    public void testCompressAndRemoveDirectories() throws IOException {
        // Ejecutar el método de comprimir y eliminar
        ZipUtils.compressAndRemoveDirectories(testDirectoryPath.toString());

        // Verificar que el directorio original haya sido eliminado
        assertFalse(Files.exists(testDirectoryPath.resolve(testDirPath)));

        // Verificar que el archivo ZIP haya sido creado
        assertTrue(Files.exists(testDirectoryPath.resolve(testDirFile)));
    }

    // Método auxiliar para comprimir un directorio
    private void zipDirectory(Path folder, String parentFolder, ZipOutputStream zos) throws IOException {
        Files.walk(folder).forEach(path -> {
            if (Files.isDirectory(path)) {
                return; // Si es un directorio, continuar la iteración
            }
            String zipEntryName = parentFolder + "/" + folder.relativize(path).toString().replace("\\", "/");
            try {
                zos.putNextEntry(new ZipEntry(zipEntryName));
                Files.copy(path, zos);
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Método auxiliar para eliminar un directorio y su contenido
    private void deleteDirectory(Path directory) throws IOException {
        Files.walk(directory)
             .sorted((a, b) -> b.compareTo(a)) // Eliminar primero los archivos y subdirectorios
             .forEach(path -> {
                 try {
                     Files.delete(path);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             });
    }
}
