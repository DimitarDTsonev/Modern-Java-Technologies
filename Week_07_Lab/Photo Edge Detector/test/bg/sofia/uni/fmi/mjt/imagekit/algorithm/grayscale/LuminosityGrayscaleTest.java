package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LuminosityGrayscaleTest {

    @Test
    void testGrayscaleConversion() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, (255 << 16) | (100 << 8) | 50);
        image.setRGB(0, 1, (0 << 16) | (255 << 8) | 100);

        LuminosityGrayscale grayscale = new LuminosityGrayscale();
        BufferedImage result = grayscale.process(image);

        int expectedGray1 = (int) (0.21 * 255 + 0.72 * 100 + 0.07 * 50);
        int expectedGray2 = (int) (0.21 * 0 + 0.72 * 255 + 0.07 * 100);

        assertEquals(expectedGray1, (result.getRGB(0, 0) & 0xFF));
        assertEquals(expectedGray2, (result.getRGB(0, 1) & 0xFF));
    }
}