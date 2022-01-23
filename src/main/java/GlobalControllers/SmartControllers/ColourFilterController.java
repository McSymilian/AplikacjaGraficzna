package GlobalControllers.SmartControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Smart;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Klasa do filtracji kolorów w obrazie
 */
public class ColourFilterController extends ColourController implements Smart {

    /**
     * Jeśli w filtrze wystąpi jakaś barwa to obraz wyjściowy będzie jej pozbawiona.
     *
     * @param image obraz do filtracji
     * @param colourFilter wartość RGB filtra kolorów
     *
     * @return obraz po filtracji koloru
     */
    public static BufferedImage filterOutColors(@NotNull final BufferedImage image, final int colourFilter) {
        var tmpImg = deepCopyImage(image);

        int r = 0;
        int g = 0;
        int b = 0;

        if (getRed(colourFilter)  > 0) r = 1;
        if (getGreen(colourFilter)  > 0) g = 1;
        if (getBlue(colourFilter)  > 0) b = 1;

        for (int y = 0, maxY = image.getHeight(); y < maxY; y++) {
            for (int x = 0, maxX = image.getWidth(); x < maxX; x++) {
                int rgb = image.getRGB(x, y);
                tmpImg.setRGB(x, y, toRGB(getRed(rgb) * r, getGreen(rgb) * g, getBlue(rgb) * b));
            }
        }

        return tmpImg;
    }

    /**
     * Barwy nieobecne w filtrze są zastępowane szarością obliczoną na podstawie wartości barw w konkretnym pixelu z oryginalnego obrazu.
     *
     * @param image obraz wejściowy
     * @param colourFilter wartość RGB filtra kolorów
     *
     * @return obraz z dominantą barw obecnych w filtrze
     */
    public static BufferedImage filterAndGrey(@NotNull final BufferedImage image, final int colourFilter) {
        var tmpImg = deepCopyImage(image);

        int r = 0;
        int g = 0;
        int b = 0;

        if (getRed(colourFilter)  > 0) r = 1;
        if (getGreen(colourFilter)  > 0) g = 1;
        if (getBlue(colourFilter)  > 0) b = 1;

        for (int y = 0, maxY = image.getHeight(); y < maxY; y++) {
            for (int x = 0, maxX = image.getWidth(); x < maxX; x++) {
                int rgb = image.getRGB(x, y);
                int grey = toGrayScale(rgb);

                tmpImg.setRGB(x, y, toRGB((r == 1 ? getRed(rgb) : grey), (g == 1 ? getGreen(rgb) : grey), (b == 1 ? getBlue(rgb) : grey)));
            }

        }
        return tmpImg;
    }
}
