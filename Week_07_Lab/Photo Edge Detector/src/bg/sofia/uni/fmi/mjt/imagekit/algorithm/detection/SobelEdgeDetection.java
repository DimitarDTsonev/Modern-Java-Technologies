package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.constants.Constants;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    private final ImageAlgorithm grayscaleAlgorithm;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) throw new IllegalArgumentException("Image cannot be null");
        BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);
        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[][] gx = {{-1, 0, 1}, {Constants.TWO_TO_MINUS_1, 0, 2}, {-1, 0, 1}};
        int[][] gy = {{-1, Constants.TWO_TO_MINUS_1, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = 0;
                int pixelY = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int rgb = grayscaleImage.getRGB(x + kx, y + ky) & Constants.NUM;
                        pixelX += gx[ky + 1][kx + 1] * rgb;
                        pixelY += gy[ky + 1][kx + 1] * rgb;
                    }
                }
                int magnitude = (int) Math.min(Constants.TWO_TO_8, Math.sqrt(pixelX * pixelX + pixelY * pixelY));
                int edgeRgb = (magnitude << Constants.TWO_TO_4) | (magnitude << Constants.TWO_TO_3) | magnitude;

                edgeImage.setRGB(x, y, edgeRgb);
            }
        }
        return edgeImage;
    }
}