package com.github.edufeedai;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import com.github.edufeedai.utils.ZipUtils;

public class App {
    private static final String MARK_FILE = ".edufeedai";

    private static final int ERROR_CURRENT_FOLDER_NOT_EDUFEEDAI_WORKSPACE = 1;
    private static final int ERROR_ZIP_NOT_FOUND = 2;
    private static final int ERROR_ZIP_EXTRACTION_FAILED = 3;

    public static void main(String[] args) {
        if (args.length == 1 && args[0].endsWith(".zip")) {
            // Modo comando: ZIP como argumento
            String zipPath = args[0];
            File zipFile = new File(zipPath);
            if (!zipFile.exists()) {
                System.out.println("El archivo ZIP no existe: " + zipPath);
                System.exit(ERROR_ZIP_NOT_FOUND);
            }
            String outputDirName = zipFile.getName().replaceFirst("\\.zip$", "");
            File outputDir = new File(zipFile.getParentFile(), outputDirName);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            try {
                ZipUtils.Unzip(zipFile.getAbsolutePath(), outputDir.getAbsolutePath());
                // Crear marca
                File mark = new File(outputDir, MARK_FILE);
                mark.createNewFile();
                System.out.println("ZIP extraído en: " + outputDir.getAbsolutePath());
                System.out.println("Directorio preparado para procesamiento. Ejecuta la aplicación dentro de este directorio para continuar.");
            } catch (Exception e) {
                System.out.println("Error al extraer el ZIP: " + e.getMessage());
            }
            System.exit(0);
        }
        // Modo interactivo: sin argumentos
        File currentDir = new File(System.getProperty("user.dir"));
        File mark = new File(currentDir, MARK_FILE);
        if (!mark.exists()) {
            System.out.println("Este directorio no parece ser un entorno de trabajo de EduFeedAI.\nEjecuta la aplicación pasando un ZIP como argumento para inicializarlo.");
            System.exit(ERROR_CURRENT_FOLDER_NOT_EDUFEEDAI_WORKSPACE);
        }
        // Menú interactivo
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== EduFeedAI CLI (modo interactivo) ===");
            System.out.println("1. Procesar entregas");
            System.out.println("2. Ver ayuda");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.println("[Funcionalidad pendiente] Procesando entregas...");
                    // Aquí se llamará a la lógica de procesamiento real
                    break;
                case "2":
                    mostrarAyuda();
                    break;
                case "0":
                    System.out.println("¡Hasta pronto!");
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
        scanner.close();
    }

    private static void mostrarAyuda() {
        System.out.println("\n--- Ayuda EduFeedAI CLI ---");
        System.out.println("- Ejecuta: java -jar edufeedai-cli.jar <archivo.zip> para inicializar un entorno de trabajo.");
        System.out.println("- Luego, ejecuta la aplicación dentro del directorio extraído para gestionar y procesar las entregas.");
    }
}
