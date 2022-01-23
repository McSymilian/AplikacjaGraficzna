package GlobalControllers.DumpControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Dumb;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Klasa do znajdywania krawędzi operatorem Sobela
 */
public class SobelsOperator extends ColourController implements Dumb {

    /**
     * Otrzymany obraz jest traktowany jako obraz wzorcowy.
     * Pobierane są z niego wartości pikseli w celu dalszej analizy.
     * Dla każdego piksela liczona jest surowa wartość na podstawie masek.
     * Surowe wartości są zapisywane w tablicy o wielkości obrazu, na podstawie której tworzony jest nowy obraz.
     * Surowe wartości mnożone są przez skalę jasności czyli iloraz maksymalnej wartości barwy i największej wartości RGB pixela w oryginalnym obrazie.
     * Ostatecznie barwa krawędzi to suma bitowa minimalnej wartości RGB i wartości każdej barwy zapisanej w macierzy wartości surowych.
     *
     * @param originalImage obraz do analizy
     *
     * @return obraz krawędzi
     */
    @Contract(pure = true)
    public static BufferedImage action(@NotNull final BufferedImage originalImage) {
        var tmpImg = deepCopyImage(originalImage);

        int maxY = tmpImg.getHeight();
        int maxX = tmpImg.getWidth();

        double sigma = 1.;
        int[][] edgeColors = new int[maxX][maxY];
        int maxGradient = -1;
        int[][] hMask = new int[][]{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] vMask = new int[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (x == 0 || x == maxX - 1 || y == 0 || y == maxY - 1) tmpImg.setRGB(x, y, 0);
                else {
                    int[] pixels = new int[] {
                            toGrayScale(originalImage.getRGB(x - 1, y - 1)),
                            toGrayScale(originalImage.getRGB(x, y - 1)),
                            toGrayScale(originalImage.getRGB(x + 1, y - 1)),

                            toGrayScale(originalImage.getRGB(x - 1, y)),
                            toGrayScale(originalImage.getRGB(x, y)),
                            toGrayScale(originalImage.getRGB(x + 1, y)),

                            toGrayScale(originalImage.getRGB(x - 1, y + 1)),
                            toGrayScale(originalImage.getRGB(x, y + 1)),
                            toGrayScale(originalImage.getRGB(x + 1, y + 1))
                    };

                    int hVal = countPixValWithMask(sigma, hMask, pixels);
                    int vVal = countPixValWithMask(sigma, vMask, pixels);

                    int rawValue = (int) Math.sqrt(vVal * vVal + hVal * hVal);

                    if(maxGradient < rawValue) {
                        maxGradient = rawValue;
                    }

                    edgeColors[x][y] = rawValue;
                }
            }
        }

        double scale = 255.0 / maxGradient;

        for (int i = 1; i < maxX - 1; i++) {
            for (int j = 1; j < maxY - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = -16_777_216 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                tmpImg.setRGB(i, j, edgeColor);
            }
        }
        return tmpImg;
    }

    /**
     * Oblicza iloczyn sumy iloczynów wartości w RGB pixeli z wagimi filtrów, oraz wzmocnienia.
     * Wartośc filtra z maski mnożona jest przez wartość piksela oraz wzpółczynnik sigma.
     * Po sumowaniu obliczocznych wartości pixeli otrzymujemy surową wartość na podstawie której sprawdzana jest pozycja krawędzi.
     *
     * @param sigma współczynnik wzmocnienia
     * @param mask tablica zawierająca wartości wag filtrów
     * @param pixels tablica z wartościami RGB pixeli pokrywającyh się z akutalną maską
     *
     * @return surowa wartość po przeliczeniu maski
     */
    private static int countPixValWithMask(double sigma, int[][] mask, int[] pixels) {
        return (int) (
                (pixels[0] * mask[0][0]
                + pixels[1] * mask[0][1]
                + pixels[2] * mask[0][2]
                + pixels[3] * mask[1][0]
                + pixels[4] * mask[1][1]
                + pixels[5] * mask[1][2]
                + pixels[6] * mask[2][0]
                + pixels[7] * mask[2][1]
                + pixels[8] * mask[2][2])
                * sigma
        );
    }
}
