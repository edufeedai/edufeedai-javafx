package com.github.edufeedai;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== EduFeedAI CLI ===");
            System.out.println("1. Procesar PDF de alumno");
            System.out.println("2. Ayuda");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.println("[Funcionalidad pendiente] Procesar PDF de alumno...");
                    // Aquí se llamará a la lógica de edufeedai-lib
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
        System.out.println("1. Procesar PDF de alumno: Permite subir y analizar un boletín PDF para generar retroalimentación automática.");
        System.out.println("2. Ayuda: Muestra este mensaje de ayuda.");
        System.out.println("0. Salir: Cierra la aplicación.");
    }
}
