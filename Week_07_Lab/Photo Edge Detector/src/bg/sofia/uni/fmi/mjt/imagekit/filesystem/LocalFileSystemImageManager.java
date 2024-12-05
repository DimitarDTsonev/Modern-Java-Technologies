package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException("Invalid file");
        }
        return ImageIO.read(imageFile);
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Directory cannot be null");
        }
        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            throw new IOException("Invalid directory");
        }

        List<BufferedImage> images = new ArrayList<>();
        File[] files = imagesDirectory.listFiles((dir, name) -> name.endsWith(".jpg")
                || name.endsWith(".png") || name.endsWith(".bmp"));

        if (files == null) {
            throw new IOException("Failed to list files in the directory");
        }

        for (File file : files) {
            images.add(ImageIO.read(file));
        }

        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image or file cannot be null");
        }
        if (imageFile.exists()) {
            throw new IOException("File already exists");
        }

        File parent = imageFile.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("Could not create parent directory");
            }
        }

        String format = getFormat(imageFile.getName());
        if (!ImageIO.write(image, format, imageFile)) {
            throw new IOException("Unsupported format");
        }
    }

    private String getFormat(String fileName) throws IOException {
        if (fileName.endsWith(".png")) {
            return "png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "jpeg";
        } else if (fileName.endsWith(".bmp")) {
            return "bmp";
        } else {
            throw new IOException("Unsupported file format");
        }
    }
}