package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.image.BufferedImage;
import bg.sofia.uni.fmi.mjt.imagekit.constants.Constants;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("image is null");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int red = (rgb >> Constants.RED_NUM) & Constants.NUM;
                int green = (rgb >> Constants.GREEN_NUM) & Constants.NUM;
                int blue = rgb & Constants.NUM;

                int gray = (int) (Constants.GRAY_NUMS[0] * red +
                        Constants.GRAY_NUMS[1] * green + Constants.GRAY_NUMS[2] * blue);
                int grayRgb = (gray << Constants.RED_NUM) | (gray << Constants.GREEN_NUM) | gray;

                grayscaleImage.setRGB(x, y, grayRgb);
            }
        }

        return grayscaleImage;
    }
}
