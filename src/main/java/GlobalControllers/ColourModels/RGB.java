package GlobalControllers.ColourModels;

import GlobalControllers.Abstracts.ColourController;
import org.jetbrains.annotations.Range;

/**
 * Prosty kontener to przetrzymywania wartości koloru w formacie RGB z odzielnymi kanałami barw.
 */
public record RGB(
        @Range(from = 0, to = 255) Integer red,
        @Range(from = 0, to = 255) Integer green,
        @Range(from = 0, to = 255) Integer blue) {

    /**
     * Konwersja liczby całkowitej na trzy kanały barw
     * @param rgb liczba całkowita opisująca kolor
     * @return Kontener w odpowiednimi wartościami kanałów
     */
    public static RGB toRGB(int rgb) {
        var red = ColourController.getRed(rgb);
        var green = ColourController.getGreen(rgb);
        var blue = ColourController.getBlue(rgb);
        return new RGB(red, green, blue);
    }

    @Range(from = 0, to = 255)
    public int getMean() {
        return (red + green + blue) / 3;
    }

    public static RGB getAsGrey(@Range(from = 0, to = 255) int brightness) {
        return new RGB(brightness, brightness, brightness);
    }

    /**
     * Szybka konwersja trzech kanałów barw na pojedynczą liczbę całkowitą
     *
     * @return wartość koloru w formacie pojedynczej liczby całkowitej
     */
    public int toInt() {
        return (((red << 8) | green) << 8) | blue;
    }

    /**
     *  Przeliczenie wartości koloru z modelu RGB na model HSB (stożkowy)
     *
     * @return odpowiednik tego koloru w formacie HSB
     */
    public HSB toHSB() {
        double r = red / 255.0;
        double g = green / 255.0;
        double b = blue / 255.0;

        double cmax = Math.max(r, Math.max(g, b));
        double cmin = Math.min(r, Math.min(g, b));
        double diff = cmax - cmin;

        double h;
        if (cmax == cmin)
            h = 0;
        else if (cmax == r)
            h = (g - b) / diff;
        else if (cmax == g)
            h = ((b - r) / diff) + 2;
        else
            h = ((r - g) / diff) + 4;

        h *= 60;
        if (h < 0)
            h += 360;

        double s;
        if (cmax == 0)
            s = 0;
        else
            s = diff / cmax;

        return new HSB((int) h, s, cmax);
    }

    @Override
    public String toString() {
        return "RGB("+ red + ", " + green + ", " + blue + ")";
    }
}
