package GlobalControllers.DumpControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.ColourModels.HSB;
import GlobalControllers.ColourModels.RGB;
import GlobalControllers.Interfaces.Dumb;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Implementacja filtru Kuwahary, czyli filtru dolnoprzepustowego zachowującego krawędzie
 */
public class KuwaharasController extends ColourController implements Dumb {

    /**
     * Reprezenatcja podmaski (ćwiartki maski) ułatwiająca analizę obszarową maski
     */
    private static class KuwaharasRegion {

        /**
         * Średnia wartość czerwonej barwy w pixelach tej podmaski
         */
        @Getter
        private double meanR;

        /**
         * Średnia wartość zielonej barwy w pixelach tej podmaski
         */
        @Getter
        private double meanG;

        /**
         * Średnia wartość niebieskiej barwy w pixelach tej podmaski
         */
        @Getter
        private double meanB;

        /**
         * Reprezentacja RGB uśrednionych barw z podmaski
         */
        public final RGB rgb;

        /**
         * Wartość wariancji tej podmaski
         */
        public final double variance;

        public KuwaharasRegion(HSB[] subMask) {
            double brightMean = countMean(subMask);

            variance = countVariance(subMask, brightMean);

            for (var pix : subMask) {
                var rgbPix = pix.toRGB();
                meanR += rgbPix.red();
                meanG += rgbPix.green();
                meanB += rgbPix.blue();
            }

            meanR /= subMask.length;
            meanG /= subMask.length;
            meanB /= subMask.length;

            rgb = new RGB((int) meanR, (int) meanG, (int) meanB);
        }

        /**
         * Obliczenie średniej wartości jasności wszystkich reprezentacji koloru w formacie HSB umieszczonych w podanej tablicy
         *
         * @param mask podmaska do analizy
         *
         * @return średnia wartość janości
         */
        private static double countMean(HSB[] mask) {
            double sum = 0;
            for (var pix : mask) sum += pix.brightness();

            return sum / mask.length;
        }

        /**
         * Obliczenie wartości wariancji wszystkich elementów w podanej masce
         *
         * @param mask podmaska do analizy
         * @param mean średnia wartość jasności
         *
         * @return wariancja jasności
         */
        private static double countVariance(HSB[] mask, double mean) {
            double variance = 0;
            for (var pix : mask) variance += Math.pow(mean - pix.brightness(), 2);

            return variance / mask.length;
        }
    }

    /**
     * Filtr polegajacy na analizie wariancji jasności czterech podmasek i
     * zmianie koloru analizowanego pixela na
     * uśrednioną wartość koloru z podmaski o najmniejszej wariancji.
     *
     * @param image obraz do filtracji
     * @param size parametr n maski o rozmiarze n x n
     *
     * @return przefiltrowany obraz
     */
    public static BufferedImage action(@NotNull final BufferedImage image, int size) {
        if (size % 2 == 0) throw new RuntimeException("rozmiar macierzy musi być nieparzysty");

        var tmpImg = deepCopyImage(image);

        HSB[][] mask = new HSB[size][size];
        int maskSize = mask.length;
        for (int x = maskSize / 2, maxX = image.getWidth() - (maskSize / 2); x < maxX; x++) {
            for (int y = maskSize / 2, maxY = image.getHeight() - (maskSize / 2); y < maxY; y++) {
                for (int i = 0; i < maskSize; i++) {
                    for (int j = 0; j < maskSize; j++) {
                        mask[i][j] = RGB.toRGB(image.getRGB(x - (maskSize / 2) + i, y - (maskSize / 2) + j)).toHSB();
                    }
                }

                var region0 = new KuwaharasRegion(makeSubMatrix(mask, 0, 0, (maskSize / 2) + 1));
                var region1 = new KuwaharasRegion(makeSubMatrix(mask, maskSize / 2, 0, (maskSize / 2) + 1));
                var region2 = new KuwaharasRegion(makeSubMatrix(mask, 0, maskSize / 2, (maskSize / 2) + 1));
                var region3 = new KuwaharasRegion(makeSubMatrix(mask, maskSize / 2, maskSize / 2, (maskSize / 2) + 1));

                KuwaharasRegion relevantRegion = region0;

                if (relevantRegion.variance > region1.variance) relevantRegion = region1;
                if (relevantRegion.variance > region2.variance) relevantRegion = region2;
                if (relevantRegion.variance > region3.variance) relevantRegion = region3;

                tmpImg.setRGB(x, y,
                        ColourController.toRGB(
                        (int) Math.round(relevantRegion.getMeanR()),
                        (int) Math.round(relevantRegion.getMeanG()),
                        (int) Math.round(relevantRegion.getMeanB())
                ));
            }
        }

        return  tmpImg;
    }

    /**
     * Tworzenie podmaski o zadanym rozmiarze przez kopiowanie wartości z macierzy podanej
     *
     * @param matrix macierz wzorcowa
     * @param startX parametr X punktu startowego
     * @param startY parametr Y punktu startowego
     * @param size parametr n podmaski o rozmiarze n x n
     *
     * @return tablica reprezentująca pod maskę
     */
    private static HSB[] makeSubMatrix(HSB[][] matrix, int startX, int startY, int size) {
        HSB[] subMatrix = new HSB[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size ; j++) {
                subMatrix[i + (size * j)] = matrix[i + startX][j + startY];
            }
        }
        return subMatrix;
    }
}
