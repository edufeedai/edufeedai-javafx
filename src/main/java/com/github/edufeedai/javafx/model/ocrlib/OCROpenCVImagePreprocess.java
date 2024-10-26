package com.github.edufeedai.javafx.model.ocrlib;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class OCROpenCVImagePreprocess {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar la librería de OpenCV
    }

    public static void Binarize(String imagePath){

        Mat originalImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE);
        if (originalImage.empty()) {
            System.out.println("No se pudo cargar la imagen");
            return;
        }

        // Aumentar el contraste y brillo
        Mat contrastImage = new Mat();
        double alpha = 1.5;  // Factor de contraste (1.0 = sin cambio, >1.0 para aumentar contraste)
        double beta = 50;    // Factor de brillo (0 = sin cambio, >0 para aumentar brillo)
        originalImage.convertTo(contrastImage, -1, alpha, beta);

        // Aplicar la binarización (método de Otsu)
        Mat binarizedImage = new Mat();
        Imgproc.threshold(contrastImage, binarizedImage, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // Guardar la imagen binarizada
        Imgcodecs.imwrite(imagePath, binarizedImage);

    }

}
