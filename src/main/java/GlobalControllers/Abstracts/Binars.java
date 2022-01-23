package GlobalControllers.Abstracts;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Klasa serializująca transformacje binaryzacji obrazu
 */
public abstract class Binars {
    /**
     * Pixele o wartość RGB powyżej podanego progu ustawiane są na maksymalną wartość RGB.
     * W przeciwnym wypadku ich wartość ustawiana jest na 0.
     *
     * @param image obraz do przetworzenia
     * @param threshold wartość progowa
     *
     * @return przetworzony obraz binarny
     */
    public static BufferedImage binarImage(@NotNull BufferedImage image, final long threshold) {
        for (int y = 0, maxY = image.getHeight(); y < maxY;  y++) {
            for (int x = 0, maxX = image.getWidth(); x < maxX; x++) {
                int rgb = ColourController.toGrayMean(image.getRGB(x, y));
                if (rgb > threshold) {
                    image.setRGB(x, y, ColourController.toRGB(255, 255, 255));
                }
                else {
                    image.setRGB(x, y, 0);
                }
            }
        }
        return  image;
    }
}
