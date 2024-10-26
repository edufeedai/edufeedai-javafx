package com.github.edufeedai.javafx.model.ocrlib;


import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class OCROpenCVImagePreprocess {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar la librería de OpenCV
    }

    public static void Binarize(String imagePath){

        Binarize(imagePath, imagePath);

    }

    public static void Binarize(String imagePathSource, String imagePathDestination){

        Mat originalImage = Imgcodecs.imread(imagePathSource, Imgcodecs.IMREAD_GRAYSCALE);
        if (originalImage.empty()) {
            System.out.println("No se pudo cargar la imagen");
            return;
        }

        // Aplicar binarización adaptativa con un bloque más grande
        Mat binarizedImage = new Mat();
        Imgproc.adaptiveThreshold(originalImage, binarizedImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15, 2);

        // Guardar la imagen procesada
        Imgcodecs.imwrite(imagePathDestination, binarizedImage);



    }

}
