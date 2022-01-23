package GlobalControllers.SmartControllers;

import GlobalControllers.Abstracts.Binars;
import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Smart;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BinarSmartController extends ColourController implements Smart {
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
     * Piksele o wartości poniżej podanej wartości progowej ustawiane są na 0, inne na kolor czarny
     *
     * @param threshold wartość progowa
     *
     * @return zbinaryzowany obraz
     */
    @SneakyThrows
    public static BufferedImage action(final int threshold) {
        if (originalImage == null) throw new IOException("Brak obrazu do progowania");

        GreyShadesController.setIMG(originalImage);
        var greyImg = new GreyShadesController();

        var templateImg = greyImg.imageToGreyMean();

        return Binars.binarImage(templateImg, (threshold + 255) / 2);
    }
}
