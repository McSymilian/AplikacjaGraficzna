package GlobalControllers.SmartControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Smart;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Klasa do łatwej zmiany jasności obrazu
 */
public class BrightnessController extends ColourController implements Smart {
    /**
     * Obraz wzorcowy, jeśli jest jest null to wywołanie metody filtracji wywoła błąd
     */
    @Nullable
    private static BufferedImage originalImage;

    /**
     * Kopiuje podany obraz do obrazu wzorcowego
     *
     * @param img obraz do zapisu
     */
    @Contract(pure = true)
    public static void setIMG(@NotNull final BufferedImage img) {
        originalImage = deepCopyImage(img);
    }

    /**
     * Wartość podana jest dodawana no każdej barwy w pikselu z obrazu wzorcowym i zapisywana w jego kopii
     *
     * @param step intensywaność rozjaśnienia
     *
     * @return obraz ze zmionioną jasnością
     */
    @SneakyThrows
    public static BufferedImage action(final int step) {
        if (originalImage == null) throw new IOException("Brak obrazu wzorcowego");
        var tmpImage = deepCopyImage(originalImage);

        for (int y = 0, maxY = originalImage.getHeight(); y < maxY; y++) {
            for (int x = 0, maxX = originalImage.getWidth(); x < maxX; x++) {
                int rgb = originalImage.getRGB(x, y);
                int r = getRed(rgb) + step;
                int g = getGreen(rgb) + step;
                int b = getBlue(rgb) + step;

                tmpImage.setRGB(x, y, normalizeColors(r, g, b));
            }
        }

        return tmpImage;
    }
}

