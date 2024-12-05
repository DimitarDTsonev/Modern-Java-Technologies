package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SobelEdgeDetectionTest {

    @Test
    void testEdgeDetection() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        image.setRGB(1, 1, 255);

        LuminosityGrayscale grayscale = new LuminosityGrayscale();
        SobelEdgeDetection sobel = new SobelEdgeDetection(grayscale);
        BufferedImage result = sobel.process(image);

        assertNotNull(result);
    }
}