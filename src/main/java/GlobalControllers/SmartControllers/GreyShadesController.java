package GlobalControllers.SmartControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.ColourModels.RGB;
import GlobalControllers.Interfaces.Dumb;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Klasa do monochromatyzacji obrazu
 */
public class GreyShadesController extends ColourController implements Dumb {
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
     * Wartości barw z filtra redukują wartości odpowiadającyh barw z pixela.
     * Przy liczeniu odcienia szarości ze średniej z trzech barw, waga każdego koloru jest równa jego wartości.
     * Redukuje to znaczenie barw skrajnie niższych od innych.
     *
     * @param filter filtr osłabiający poszczegulne kanały barw
     *
     * @return obraz w odcieniach szarości z odfiltrowanymi kolorami
     */
    @SneakyThrows
    public static BufferedImage imageToGreyColorFilterEnhanced(RGB filter) {
        if (originalImage == null) throw new IOException("Brak obrazu do konwersji");
        var tmpImg = deepCopyImage(originalImage);

        for (int y = 0, maxY = tmpImg.getHeight(); y < maxY; y++) {
            for (int x = 0, maxX = tmpImg.getWidth(); x < maxX; x++) {
                var px = RGB.toRGB(originalImage.getRGB(x, y));

                var r = px.red() - filter.red();
                var g = px.green() - filter.green();
                var b = px.blue() - filter.blue();

                var red = Math.max(r, 1);
                var green = Math.max(g, 1);
                var blue = Math.max(b, 1);

                var mean = red * red + green * green + blue * blue;

                mean /= (red + green + blue);

                tmpImg.setRGB(x, y, toGreyRGB(mean));
            }
        }
        return tmpImg;
    }

    /**
     * Wartości barw z filtra redukują wartości odpowiadającyh barw z pixela.
     * Wartość odcienia szarości to średnia wartość kolorów po redukcji.
     *
     * @param filter filtr osłabiający poszczegulne kanały barw
     *
     * @return obraz w odcieniach szarości z odfiltrowanymi kolorami
     */
    @SneakyThrows
    public static BufferedImage imageToGreyColorFilterNormal(RGB filter) {
        if (originalImage == null) throw new IOException("Brak obrazu do konwersji");
        var tmpImg = deepCopyImage(originalImage);

        for (int y = 0, maxY = tmpImg.getHeight(); y < maxY; y++) {
            for (int x = 0, maxX = tmpImg.getWidth(); x < maxX; x++) {
                var px = RGB.toRGB(originalImage.getRGB(x, y));

                var r = px.red() - filter.red();
                var g = px.green() - filter.green();
                var b = px.blue() - filter.blue();

                var red = Math.max(r, 1);
                var green = Math.max(g, 1);
                var blue = Math.max(b, 1);

                var mean = red + green + blue;

                mean /= 3;

                tmpImg.setRGB(x, y, toGreyRGB(mean));
            }
        }
        return tmpImg;
    }

    /**
     * Ten rodzaj monochromatyzacji daje głęboki obraz, bardzo zbliożony do analogowego zdjęcia czarno-białego
     *
     * @param originalImage obraz do monochromatyzacji
     *
     * @return monochromatyczny obraz
     */
    @Contract(pure = true)
    public static BufferedImage imageToGreyScale(@NotNull final BufferedImage originalImage) {
        var tmpImg = deepCopyImage(originalImage);

        int pixel;
        int pixelWynikowy;

        for(int y = 0, maxY = tmpImg.getHeight(); y < maxY; y++){
            for(int x = 0, maxX = tmpImg.getWidth(); x < maxX; x++){
                pixel = tmpImg.getRGB(x, y);

                pixelWynikowy = toGreyRGB(toGrayScale(pixel));

                tmpImg.setRGB(x, y, pixelWynikowy);
            }
        }

        return tmpImg;
    }

    /**
     * Ten rodzaj monochromatyzacji daje płytki i blady obraz
     * Wartość trzech barw w pixelu z oryginalnego obrazu jest uśredniana i zapisywana w nowym obrazie.
     *
     * @return monochromatyczny obraz
     */
    @SneakyThrows
    @Contract(pure = true)
    public BufferedImage imageToGreyMean() {
        if (originalImage == null) throw new IOException("Brak obrazu do konwersji");
        var tmpImg = deepCopyImage(originalImage);

        var t1 = new MeanThread(originalImage, tmpImg, 0, 0, originalImage.getWidth() / 2, originalImage.getHeight() / 2);
        var t2 = new MeanThread(originalImage, tmpImg, originalImage.getWidth() / 2, 0, originalImage.getWidth() / 2 + 1, originalImage.getHeight() / 2);
        var t3 = new MeanThread(originalImage, tmpImg, 0, originalImage.getHeight() / 2, originalImage.getWidth() / 2, originalImage.getHeight() / 2 + 1);
        var t4 = new MeanThread(originalImage, tmpImg, originalImage.getWidth() / 2, originalImage.getHeight() / 2, originalImage.getWidth() / 2 + 1, originalImage.getHeight() / 2 + 1);

        while (t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive()) {}
        return originalImage;
    }

    /**
     * Wątek wyszarzający przez uśrednienie barw konkretny obszar podanego obrazu
     */
    private static class MeanThread extends Thread {
        private final BufferedImage template;
        private final BufferedImage output;

        private final int startX;
        private final int startY;

        private final int width;
        private final int height;

        public MeanThread(BufferedImage template, BufferedImage output, int startX, int startY, int width, int height) {
            this.template = template;
            this.output = output;
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
            start();
        }

        @Override
        public void run() {
            for (int i = startX, endX = startX + width; i < endX && i < template.getWidth() ; i++) {
                for (int j = startY, endY = startY + height; j < endY && j < template.getHeight(); j++) {
                    int brightness = RGB.toRGB(template.getRGB(i, j)).getMean();
                    output.setRGB(i, j, RGB.getAsGrey(brightness).toInt());
                }
            }
        }
    }
}


