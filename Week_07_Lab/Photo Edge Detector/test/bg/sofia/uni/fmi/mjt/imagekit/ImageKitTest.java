package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageKitTest {

    @Test
    void testGrayscaleConversion() throws Exception {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        BufferedImage image = manager.loadImage(new File("kitten.png"));

        LuminosityGrayscale grayscale = new LuminosityGrayscale();
        BufferedImage grayImage = grayscale.process(image);

        assertNotNull(grayImage, "Grayscale image should not be null");
    }

    @Test
    void testEdgeDetection() throws Exception {
        LocalFileSystemImageManager manager = new LocalFileSystemImageManager();
        BufferedImage image = manager.loadImage(new File("kitten.png"));

        LuminosityGrayscale grayscale = new LuminosityGrayscale();
        SobelEdgeDetection edgeDetection = new SobelEdgeDetection(grayscale);

        BufferedImage edgeImage = edgeDetection.process(image);
        assertNotNull(edgeImage, "Edge-detected image should not be null");
    }

    @Test
    void testNullInputInAlgorithms() {
        LuminosityGrayscale grayscale = new LuminosityGrayscale();
        assertThrows(IllegalArgumentException.class, () -> grayscale.process(null));

        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscale);
        assertThrows(IllegalArgumentException.class, () -> sobel.process(null));
    }

    @Test
    void testLoadImagesFromInvalidDirectory() {
        FileSystemImageManager manager = new LocalFileSystemImageManager();
        File invalidDir = new File("invalid-directory");

        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(invalidDir));
    }
}