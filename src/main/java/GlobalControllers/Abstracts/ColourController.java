package GlobalControllers.Abstracts;

import GlobalControllers.ColourModels.HSB;
import GlobalControllers.ColourModels.RGB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Klasa zawierająca podstawowe metody do działania z obrazami i formatem kolorów RGB
 */
public abstract class ColourController {

    /**
     * Tworzy nową instacnję reprezentującą obraz o wymiarach obrazu podanego, a nastepnie kopiuje wartości pikseli.
     *
     * @param image obraz do skopiowania
     *
     * @return głęboka kopia podanego obrazu
     */
    @Contract(pure = true)
    public static BufferedImage deepCopyImage(final BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copy.createGraphics();
        g.drawImage(image, 0, 0, copy.getWidth(), copy.getHeight(), null);
        g.dispose();

        return copy;
    }

    /**
     * Przesuwa bitowo podaną wartość by usunąć bity nie opisujące czerwonej barwy.
     *
     * @param pixel wartość kolorów w formacie RGB
     *
     * @return wartość barwy czerwonej
     */
    @Contract(pure = true)
    @Range(from = 0, to = 255)
    public static int getRed(@Range(from = 0, to = 16_777_215) int pixel){
        return (pixel << 8) >>> 24;
    }

    /**
     * Przesuwa bitowo podaną wartość by usunąć bity nie opisujące zielonej barwy.
     *
     * @param pixel wartość kolorów w formacie RGB
     *
     * @return wartość barwy zielonej
     */
    @Contract(pure = true)
    @Range(from = 0, to = 255)
    public static int getGreen(int pixel){
        return (pixel << 16) >>> 24;
    }

    /**
     * Przesuwa bitowo podaną wartość by usunąć bity nie opisujące niebieskiej barwy.
     *
     * @param pixel wartość kolorów w formacie RGB
     *
     * @return wartość barwy niebieskiej
     */
    @Contract(pure = true)
    @Range(from = 0, to = 255)
    public static int getBlue(int pixel){
        return (pixel << 24) >>> 24;
    }

    /**
     * Przesuwa wartości odpowiednich kolorów o odpowiednią ilość bitów by wartości barw znalazły się w swoich kanałach
     *
     * @param r intensywaność barwy czerwonej
     * @param g intensywaność barwy zielonej
     * @param b intensywaność barwy niebeiskiej
     *
     * @return wartość opisująca konkretny kolor w formacie RGB
     */
    @Contract(pure = true)
    @Range(from = 0, to = 16_777_215)
    public static int toRGB(@Range(from = 0, to = 255) int r, @Range(from = 0, to = 255) int g, @Range(from = 0, to = 255) int b){
        return (((r << 8) | g) << 8) | b;
    }

    /**
     * Utworzenie szarości o jasności wartości podanej
     *
     * @param intensity jasność pixela
     *
     * @return odcień szarości w pistaci RGB
     */
    @Contract(pure = true)
    @Range(from = 0, to = 16_777_215)
    public static int toGreyRGB(@Range(from = 0, to = 255) int intensity) {
        return toRGB(intensity, intensity, intensity);
    }

    /**
     * Uzyskanie szarości przez iloczyn barwy i odpowiedniego wpółczynnika.
     * Daje efekt analogowego czarno-białego zdjęcia.
     * Konkretne barwy uzyskiwane są przez przesunięcie odpowiednie bitowe podanej liczby i użycia iloczynu bitowego w celu usunięcia niepotrzebnych bitów a
     * następnie mnożonie przez odpowiedni wpółczynnik.
     *
     * @param value RGB
     *
     * @return wartość odcienia szrości
     */
    @Contract(pure = true)
    @Range(from = 0, to = 16_777_215)
    public static int toGrayScale(@Range(from = 0, to = 16_777_215) int value) {
        int r = (int) (((value >> 16) & 255 ) * 0.2126);
        int g = (int) (((value >> 8) & 255) * 0.7152);
        int b = (int) (((value) & 255) * 0.0722);
        return r + g + b;
    }

    /**
     * Uzyskanie szarości przez uśrednienie barw.
     * Przesuwa bitowo podaną wartość by uzyskać składową część odpowiedzialną za konkretną barwę.
     * Konkretna barwa jest iloczynem bitowego przesunięcia i liczby 255 by zniwelować szumy (początkowe bity - np. wartość alfa)
     *
     * @param value RGB
     *
     * @return uśredniona wartość barwy czerwonej, zielonej i niebieskiej / wartość odcienia szarości
     */
    @Contract(pure = true)
    @Range(from = 0, to = 255)
    public static int toGrayMean(int value) {
        int r = getRed(value);
        int g = getGreen(value);
        int b = getBlue(value);

        return (r + g + b) / 3;
    }

    /**
     * Jeśli podana wartość składowa przekracza 255 (max. wartość barwy) to ustawiana jest na 255.
     * Jeśli wartość jest mniejsza od 0 (min. wartość barwy) to ustawiana jest na 0.
     *
     * @param r intensywaność barwy czerwonej
     * @param g intensywaność barwy zielonej
     * @param b intensywaność barwy niebeiskiej
     *
     * @return znormalizowana wartość RGB
     */
    @Contract(pure = true)
    @Range(from = 0, to = 16_777_215)
    public static int normalizeColors(int r, int g, int b) {
        if (r > 255) r = 255;
        else if (r < 0) r = 0;
        if (g > 255) g = 255;
        else if (g < 0) g = 0;
        if (b > 255) b = 255;
        else if (b < 0) b = 0;

        return toRGB(r, g, b);
    }

    public static HSB rgbToHsb(int r, int g, int b) {
        return new RGB(r, g, b).toHSB();
    }

    public static HSB rgbToHsb(int rgb) {
       return rgbToHsb(getRed(rgb), getGreen(rgb), getBlue(rgb));
    }

    public static HSB rgbToHsb(Color color) {
       return rgbToHsb(color.getRed(), color.getGreen(), color.getBlue());
    }




}
