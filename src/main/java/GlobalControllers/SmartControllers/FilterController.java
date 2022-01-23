package GlobalControllers.SmartControllers;

import GlobalControllers.Interfaces.Dumb;
import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Smart;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Uniwersalna klasa do filtrów graficznych
 */
public class FilterController extends ColourController implements Smart, Dumb {

    /**
     * Najlepiej działa do filtrów dolnoprzepustowych.
     * Uśrednia wartości pikseli w obszarze określonym rozmiarem maski.
     * Wagi filtrów są sumowane i stanowią dzielnik przy wyliczaniu śrendiej wartości.
     * Wartość piksela jest mnożona przez wartość wagi odpowiadającej pozycji w masce.
     *
     * @param originalImage obraz do przefiltrowania
     * @param mask zbiór wag filtrów
     *
     * @return przefiltrowany obraz
     */
    public static BufferedImage action(@NotNull final BufferedImage originalImage, int[][] mask) {
        var tmpImg = deepCopyImage(originalImage);

        int maskSum = 0;
        for (int[] ints : mask)
            for (int j = 0; j < mask.length; j++)
                maskSum += ints[j];

        int halfMaskLength = mask.length / 2;
        for (int y = halfMaskLength, maxY = originalImage.getHeight() - y; y < maxY; y++) {
            for (int x = halfMaskLength, maxX = originalImage.getWidth() - x; x < maxX; x++) {
                int r = 0;
                int g = 0;
                int b = 0;

                for (int i = 0; i < mask.length; i++)
                    for (int j = 0; j < mask.length; j++) {
                        int pxVal = originalImage.getRGB(x + i - halfMaskLength, y + j - halfMaskLength);
                        r += getRed(pxVal) * mask[i][j];
                        g += getGreen(pxVal) * mask[i][j];
                        b += getBlue(pxVal) * mask[i][j];
                    }

                int pixel = normalizeColors(r / maskSum, g / maskSum, b / maskSum);
                tmpImg.setRGB(x, y, pixel);
            }
        }

        return tmpImg;
    }

    /**
     * Najlepiej działa do filtrów górnoprzepustowych niskich wartości.
     * Prosta implementacja filtru z zastosowaniem kernela.
     *
     * @param originalImage obraz do przefiltrowania
     * @param kernel reprezentacja maski
     *
     * @return przefiltrowany  obraz
     */
    public static BufferedImage action(final BufferedImage originalImage, Kernel kernel) {
        var tmpImg = deepCopyImage(originalImage);
           BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);

        return op.filter(tmpImg, null);
    }
}
