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
 * Klasa do łatwej zmiany kontratu obrazu
 */
public class ContrastController extends ColourController implements Smart {

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
     * Wartość podana jest przeliczana do skali od 0.05 do ok. 3 (zależności od implementacji).
     * Wartość piksela z oryginalnego obrazu jest mnożona przez obliczony wpółczynnik k, a natępnie normalizowana.
     *
     * @param step intesywność kontrastu
     *
     * @return obraz ze zmienionym kontrastem
     */
    @SneakyThrows
    @Contract(pure = true)
    public static BufferedImage action(final int step) {
        if (originalImage == null) throw new IOException("Brak obrazu");
        var tmpImage = deepCopyImage(originalImage);

        double k = 1;
        if (step > 0) {
            k += (.1 * (step / 15.0));
        }
        else if (step < 0) {
            k -= (.05 * (-step / 15.0));
            if (k < 0) k = .05;
        }

        for (int y = 0, maxY = originalImage.getHeight(); y < maxY; y++) {
            for (int x = 0, maxX = originalImage.getWidth(); x < maxX; x++) {
                int rgb = originalImage.getRGB(x, y);

                int r = getRed(rgb) - 128;
                int g = getGreen(rgb) - 128;
                int b = getBlue(rgb) - 128;

                r *= k;
                g *= k;
                b *= k;

                r += 128;
                g += 128;
                b += 128;

                tmpImage.setRGB(x, y, normalizeColors(r, g, b));
            }
        }
        return tmpImage;
    }
}
