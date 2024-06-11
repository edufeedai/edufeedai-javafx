package com.github.amiguetes.edufeedai.edufeedaijavafx.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        // Crear directorio de prueba
        File testDir = new File(testDirPath);
        if (!testDir.exists()) {
            testDir.mkdir();
        }

        // Crear algunos archivos en el directorio de prueba
        Files.createFile(Paths.get(testDirPath, testFile1));
        Files.createFile(Paths.get(testDirPath, testFile2));

        // Crear directorio zipDir y algunos archivos en él
        File zipDir = new File(zipDirPath);
        if (!zipDir.exists()) {
            zipDir.mkdir();
        }
        Files.createFile(Paths.get(zipDirPath, testFile3));
        Files.createFile(Paths.get(zipDirPath, testFile4));

        // Comprimir el directorio de prueba en un archivo ZIP
        try (FileOutputStream fos = new FileOutputStream(testDirFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            zipDirectory(testDir, testDir.getName(), zos);
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Eliminar directorios y archivos de prueba
        deleteDirectory(new File(testDirPath));
        deleteDirectory(new File(zipDirPath));
        Files.deleteIfExists(Paths.get(testDirFile));
        Files.deleteIfExists(Paths.get(zipDirFile));
    }

    @Test
    public void testUnzipAndRemove() throws IOException {
        // Ejecutar el método de descomprimir y eliminar
        ZipUtils.unzipAndRemove(".");

        // Verificar que el archivo ZIP haya sido eliminado
        assertFalse(new File(testDirFile).exists());

        // Verificar que el directorio haya sido descomprimido correctamente
        assertTrue(new File(testDirPath + File.separator + testFile1).exists());
        assertTrue(new File(testDirPath + File.separator + testFile2).exists());
    }

    @Test
    public void testCompressAndRemoveDirectories() throws IOException {
        // Ejecutar el método de comprimir y eliminar
        ZipUtils.compressAndRemoveDirectories(".");

        // Verificar que el directorio original haya sido eliminado
        assertFalse(new File(zipDirPath).exists());

        // Verificar que el archivo ZIP haya sido creado
        assertTrue(new File(zipDirFile).exists());
    }

    // Método auxiliar para comprimir un directorio
    private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
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

    // Método auxiliar para eliminar un directorio y su contenido
    private void deleteDirectory(File directory) throws IOException {
        Files.walk(directory.toPath())
             .map(Path::toFile)
             .forEach(File::delete);
    }
}
