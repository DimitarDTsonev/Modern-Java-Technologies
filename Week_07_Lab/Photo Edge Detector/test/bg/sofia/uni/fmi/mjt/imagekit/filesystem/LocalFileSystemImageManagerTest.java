package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalFileSystemImageManagerTest {

    private final FileSystemImageManager fileManager = new LocalFileSystemImageManager();

    @Test
    void testLoadImage() throws IOException {
        File validImage = new File("kitten.png");
        BufferedImage image = fileManager.loadImage(validImage);
        assertDoesNotThrow(() -> fileManager.loadImage(validImage));
    }

    @Test
    void testLoadImageThrowsExceptionForInvalidFile() {
        File invalidFile = new File("non-existent-file.png");
        assertThrows(IOException.class, () -> fileManager.loadImage(invalidFile));
    }

    @Test
    void testSaveImageTrows() throws IOException {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        File outputFile = new File("kitten.png");
        assertThrows(IOException.class, () -> fileManager.saveImage(image, outputFile));
    }

    @Test
    void testSaveImage() throws IOException {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        File outputFile = new File("kitten2.png");

        fileManager.saveImage(image, outputFile);

        assertTrue(outputFile.exists(), "The output file should exist after saving the image");
    }

    @Test
    void testLoadImagesFromEmptyDirectory() throws IOException {
        File emptyDirectory = new File("test");
        List<BufferedImage> images = fileManager.loadImagesFromDirectory(emptyDirectory);

        assertNotNull(images, "Images list should not be null");
        assertTrue(images.isEmpty(), "Images list should be empty for an empty directory");
    }

}