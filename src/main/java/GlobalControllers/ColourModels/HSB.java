package GlobalControllers.ColourModels;

import org.jetbrains.annotations.Range;

import java.awt.*;

/**
 * Prosty kontener do przetrzymywania reprezentacji koloru w formacie HSB
 */
public record HSB(
        @Range(from = 0, to = 360) int hue,
        @Range(from = 0, to = 1) Double saturation,
        @Range(from = 0, to = 1) Double brightness) {

    /**
     * Prosta konwersja z formatu HSB na RGB z wykorzystaniem klasy Color
     *
     * @return Reprezentacja tego koloru w fromacie RGB
     */
    public RGB toRGB() {
        var color = Color.getHSBColor(hue / 360f, saturation.floatValue(), brightness.floatValue());

        return new RGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    public String toString() {
        return "HSB(" + hue + ", " + saturation + ", " + brightness + ")";
    }
}
