package GlobalControllers.DumpControllers;

import GlobalControllers.Abstracts.Binars;
import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Dumb;
import GlobalControllers.SmartControllers.GreyShadesController;

import java.awt.image.BufferedImage;

/**
 * Klasa do binaryzacji / progowania obrazu
 */
public class BinarController extends ColourController implements Dumb {

    /**
     * Piksele o wartości poniżej średniej ustawiane są na 0, inne na kolor czarny
     *
     * @param originalImage obraz wzorcowy
     *
     * @return zbinaryzowany obraz
     */
    public static BufferedImage action(BufferedImage originalImage) {
        long mean = 0;

        GreyShadesController.setIMG(originalImage);
        var greyImg = new GreyShadesController();

        var templateImg = greyImg.imageToGreyMean();

        for (int y = 0, maxY = templateImg.getHeight(); y < maxY;  y++) {
            for (int x = 0, maxX = templateImg.getWidth(); x < maxX; x++) {
                 mean += getRed(templateImg.getRGB(x, y));
            }
        }
        mean /= ((long) templateImg.getHeight() * templateImg.getWidth());


        return Binars.binarImage(templateImg, mean);
    }
}
