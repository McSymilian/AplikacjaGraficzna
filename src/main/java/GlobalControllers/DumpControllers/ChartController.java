package GlobalControllers.DumpControllers;

import GlobalControllers.Abstracts.ColourController;
import GlobalControllers.Interfaces.Dumb;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Klasa do tworzenia histogramu kolorów obrazu
 */
public class ChartController extends ColourController implements Dumb {

    /**
     * Główny histogram pokazujący przebieg koloru czerwonego, zielonego i niebieskiego lub
     * jeśli obraz składa się z uśrednionych odcieni to intensywności szarości
     */
    @Nullable
    @Getter
    private static ChartPanel mainChart;

    /**
     * Po zliczeniu występowania konkretnych intensywności barw tworzone są trzy serie bądź jedna seria.
     * Po zapakowaniu do histogramu zapisywane są w polu głównego histogramu
     *
     * @param image obraz do analizy
     */
    @Contract(pure = true)
    public static void createCharts(@NotNull final BufferedImage image) {
        int pixel;
        int[] red = new int[256];
        int[] green = new int[256];
        int[] blue = new int[256];

        boolean grey = true;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                pixel = image.getRGB(x, y);
                int r = getRed(pixel);
                int g = getGreen(pixel);
                int b = getBlue(pixel);

                if (r != g || r != b) grey = false;

                red[r]++;
                green[g]++;
                blue[b]++;
            }
        }
        XYSeriesCollection xyCollection;

        if (grey) {
            XYSeries histogram = new XYSeries("Odcień szarości");

            for (int i = 0; i < 256; i++)
                histogram.add(i, red[i]);

            xyCollection = new XYSeriesCollection(histogram);

        } else {
            XYSeries histogramRed = new XYSeries("Barwa Czerwona");
            XYSeries histogramGreen = new XYSeries("Barwa Zielona");
            XYSeries histogramBlue = new XYSeries("Barwa Niebieska");

            for (int i = 0; i < 256; i++) {
                histogramRed.add(i, red[i]);
                histogramGreen.add(i, green[i]);
                histogramBlue.add(i, blue[i]);
            }

            xyCollection = new XYSeriesCollection();
            xyCollection.addSeries(histogramRed);
            xyCollection.addSeries(histogramBlue);
            xyCollection.addSeries(histogramGreen);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                null, //tytuł wykresu
                null, //etykieta osi x
                null, //etykieta osi y
                xyCollection //nasz wykres
        );

        mainChart = new ChartPanel(chart);

        var chartDim = new Dimension(400,380);

        mainChart.setPreferredSize(chartDim);
    }

}
