package GlobalControllers.DumpControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Dumb;
import GlobalControllers.SmartControllers.GreyShadesController;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Klasa do wykrywania krawędzi w obrazie krzyżem Robertsa
 */
public class RoberstsCross extends ColourController implements Dumb {
    /**
     * Obraz krawędzi powstaje przez nałożenie na siebie filtru 135* i 45* pixela poniżej przy pomocy matrycy 3x3.
     * Filtr 135* to różnica wartości RGB centralnego pixela i pixela w górnym lewym rogu matrycy.
     * Filtr 45* to wartości RGB pixela dolego i prawego w matrycy.
     *
     * @param originalImage obraz do analizy
     *
     * @return obraz krawędzi
     */
    @Contract(pure = true)
    public static BufferedImage action(@NotNull final BufferedImage originalImage) {
        GreyShadesController.setIMG(originalImage);
        var greyImg = new GreyShadesController();

        var templateImg = greyImg.imageToGreyMean();
        var tmpImg = deepCopyImage(originalImage);

        for (int y = 0, maxY = tmpImg.getHeight(); y < maxY; y++){
            for (int x = 0, maxX = tmpImg.getWidth(); x < maxX; x++) {

                var tmp1 = templateImg.getRGB(x, y) - templateImg.getRGB((x < maxX - 1 ? x + 1 : x), (y < maxY - 1 ? y + 1 : y));
                var tmp2 = templateImg.getRGB((x < maxX - 1 ? x + 1 : x), y) - templateImg.getRGB(x, (y < maxY - 1 ? y + 1 : y));

                tmpImg.setRGB(x, y, toGreyRGB(toGrayScale(Math.abs(tmp1) + Math.abs(tmp2))));
            }
        }
        return tmpImg;
    }
}
