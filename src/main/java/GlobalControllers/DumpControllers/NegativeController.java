package GlobalControllers.DumpControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.ImageController;
import GlobalControllers.Interfaces.Dumb;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Klasa do tworzenia negatywu obrazu
 */
public class NegativeController extends ColourController implements Dumb {

    /**
     * Nowa wartość RGB pixela to różnica maksymalnej wartości RGB i wartości RGB z podanego obrazu.
     *
     * @param originalImage obraz do negatywizacji
     *
     * @return negatyw podanego obrazu
     */
    @Contract(pure = true)
    public static BufferedImage action(@NotNull final BufferedImage originalImage) {
        var tmpImage = deepCopyImage(originalImage);

        for (int y = 0, maxY = tmpImage.getHeight(); y < maxY; y++){
            for (int x = 0, maxX = tmpImage.getWidth(); x < maxX; x++) {
                tmpImage.setRGB(x, y, ImageController.toRGB(255, 255, 255) - originalImage.getRGB(x, y));
            }
        }
        return tmpImage;
    }
}
