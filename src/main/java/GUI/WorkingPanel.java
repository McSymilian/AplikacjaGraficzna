package GUI;

import GlobalControllers.DumpControllers.*;
import GlobalControllers.ImageController;
import GlobalControllers.SmartControllers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

/**
 * Panel do działania filtrami
 * Skróty klawiszowe:
 * ctrl + l -> załaduj nowy obraz
 * ctrl + s -> zapisz nowy obraz
 * ctrl + z -> wczytaj oryginalny obraz
 * shift + z -> wczytaj poprzedni obraz
 */
public class WorkingPanel extends JPanel{
    //stałe elementy GUI
    private JPanel contentPanel;
    private JPanel mainPanel;
    private JLabel imgLabel;
    private JPanel staticPanel;

    /**
     * Okno użytkujące ten panel
     */
    private JFrame parent;

    /**
     * Mechanizm do zmieniania wyświetlanego panelu pomocniczego
     */
    private final CardLayout cards = (CardLayout) contentPanel.getLayout();
    //klucze do paneli
    private final String MAIN_VIEW = "main_view";
    private final String RGB_TO_GREY_ENHANCED_FILTER_VIEW = "rgb_filter_view";
    private final String RGB_TO_GREY_NORMAL_FILTER_VIEW = "rgb_filter_view";
    private final String BRIGHTNESS_VIEW = "brightness_view";
    private final String CONTRAST_VIEW = "contrast_view";
    private final String BINAR_VIEW = "binar_view";
    private final String CHARTS_VIEW = "charts_view";
    private final String ONLY_COLOURS_VIEW = "only_colours_view";
    private final String GREY_COLOURS_VIEW = "grey_colours_view";
    private final String CUSTOM_SMALL_VIEW = "custom_small_view";
    private final String CUSTOM_MEDIUM_VIEW = "custom_medium_view";
    private final String CUSTOM_LARGE_VIEW = "custom_large_view";
    private final String KUWAHARA_VIEW = "kuwahara_view";

    //zbiór paneli do obsługi filtórw
    private final TripleSliderPanel rgbToGreyEnhancedFilterPanel = new TripleSliderPanel();
    private final TripleSliderPanel rgbToGreyNormalFilterPanel = new TripleSliderPanel();
    private final SingleSliderPanel brightnessPanel = new SingleSliderPanel();
    private final SingleSliderPanel contrastPanel = new SingleSliderPanel();
    private final SingleSliderPanel binarPanel = new SingleSliderPanel();
    private final JPanel chartsPanel = new JPanel();
    private final ColoursForm onlyColours = new ColoursForm();
    private final ColoursForm greyColours = new ColoursForm();
    private final CustomSmallPanel customSPanel = new CustomSmallPanel();
    private final CustomMediumPanel customMPanel = new CustomMediumPanel();
    private final CustomLargePanel customLPanel = new CustomLargePanel();
    private final SingleNumberInputPanel kuhawaraPanel = new SingleNumberInputPanel();
    private final JPanel mainView = new JPanel();

    //Zbiór elementów tworzących menu w ułożeniu strukturalnym
    public final JMenuBar menuBar = new JMenuBar();
        private final JMenu fileMenu = new  JMenu("Plik");
            private final JMenuItem load = new JMenuItem("Wczytaj");
            private final JMenuItem save = new JMenuItem("Zapisz");
            private final JMenuItem undo = new JMenuItem("Cofnij");
            private final JMenuItem clear = new JMenuItem("Wyczyść");
        private final JMenu pixOpsMenu = new JMenu("Operacje na pikselach");
            private final JMenu greyShades = new JMenu("Filtry szarości");
                private final JMenuItem meanGrey = new JMenuItem("Szarość uśredniona");
                private final JMenuItem scaleGrey = new JMenuItem("Szarość skalowana");
                private final JMenuItem customNormalGrey = new JMenuItem("Ustawienia szarości");
                private final JMenuItem customEnhancedGrey = new JMenuItem("Wzmocnione ustawienia szarości");
            private final JMenuItem brightnessCorrect = new JMenuItem("Korekta jasności");
            private final JMenuItem contrastCorrect = new JMenuItem("Korekta kontrastu");
            private final JMenuItem negative = new JMenuItem("Negatyw");
            private final JMenu binarisation = new JMenu("Binaryzacja");
                private final JMenuItem binarisationMean = new JMenuItem("Binaryzacja ze śreniej");
                private final JMenuItem binarisationManual = new JMenuItem("Binaryzacja ze stopniem ustawianym ręcznie");
        private final JMenu graphs = new JMenu("Wykresy");
            private final JMenuItem charts = new JMenuItem("Historgram");
        private final JMenu graphicalFilters = new JMenu("Filtry graficzne");
            private final JMenu lpFilters = new JMenu("Filtry dolnoprzepustowe");
                private final JMenuItem meanFilter = new JMenuItem("Filtr uśredniający");
                private final JMenuItem lp1Filter = new JMenuItem("Filtr LP1");
                private final JMenuItem lp2Filter = new JMenuItem("Filtr LP2");
                private final JMenuItem lp3Filter = new JMenuItem("Filtr LP3");
                private final JMenuItem squareFilter = new JMenuItem("Filtr kwadratowy");
                private final JMenuItem circleFilter = new JMenuItem("Filtr kołowy");
                private final JMenuItem coneFilter = new JMenuItem("Filtr stożkowy");
                private final JMenuItem pyramidFilter = new JMenuItem("Filtr piramidalny");
                private final JMenuItem gauss1Filter = new JMenuItem("Filtr Gaussa1");
                private final JMenuItem gauss2Filter = new JMenuItem("Filtr Gaussa2");
                private final JMenuItem gauss3Filter = new JMenuItem("Filtr Gaussa3");
                private final JMenuItem gauss4Filter = new JMenuItem("Filtr Gaussa4");
                private final JMenuItem gauss5Filter = new JMenuItem("Filtr Gaussa5");
                private final JMenuItem kuwahars = new JMenuItem("Filtr Kuwahary");
            private final JMenu hpFilters = new JMenu("Filtry górnoprzepustowe");
                private final JMenuItem hp0Filter = new JMenuItem("Filtr wyostrzający");
                private final JMenuItem hp1Filter = new JMenuItem("Filtr HP1");
                private final JMenuItem hp2Filter = new JMenuItem("Filtr HP2");
            private final JMenu colourFilters = new JMenu("Filtry koloru");
                private final JMenuItem simpleColoursFilter = new JMenuItem("Filtr koloru");
                private final JMenuItem greyColoursFilter = new JMenuItem("Filtr koloru i szarości");
            private final JMenu customFilters = new JMenu("Własne filtry");
                private final JMenuItem custom0Filter = new JMenuItem("Filtr 3x3");
                private final JMenuItem custom1Filter = new JMenuItem("Filtr 5x5");
                private final JMenuItem custom2Filter = new JMenuItem("Filtr 7x7");
        private final JMenu borders = new JMenu("Wykrywanie krawędzi");
            private final JMenuItem roberts = new JMenuItem("Krzyż Robersta");
            private final JMenuItem sobels = new JMenuItem("Operator Sobela");

    // Konstruktor statyczny implementujący zachowania do itemów menu
    {
        save.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        save.addActionListener((evt) -> {
            ImageController.saveImg(parent);
            parent.setTitle(ImageController.getImagePath().getFileName().toString());
        });

        load.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        load.addActionListener((evt) -> {
                ImageController.loadImg(parent);
                iconIt(ImageController.getImg());
                setChanges();
        });

        undo.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.SHIFT_DOWN_MASK));
        undo.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(ImageController.getLastCopy());
                setChanges();
            }
        });

        clear.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        clear.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(ImageController.resetChanges());
                setChanges();
            }
        });

        meanGrey.addActionListener((actionEvent) -> {
            if (ImageController.getImg() != null) {
                GreyShadesController.setIMG(ImageController.getImg());
                var greyImg = new GreyShadesController();
                var thread = new Thread(() -> {
                    iconIt(greyImg.imageToGreyMean());
                    setChanges();
                });
                thread.setDaemon(true);
                thread.start();
            }
        });

        scaleGrey.addActionListener((actionEvent) -> {
            if (ImageController.getImg() != null) {
                iconIt(GreyShadesController.imageToGreyScale(ImageController.getImg()));
                setChanges();
            }
        });

        customEnhancedGrey.addActionListener((actionEvent) -> {
            if (ImageController.getImg() != null) {
                cards.show(contentPanel, RGB_TO_GREY_ENHANCED_FILTER_VIEW);
                GreyShadesController.setIMG(ImageController.getImg());
                setChanges();
            }
        });
        rgbToGreyEnhancedFilterPanel.slider1.addChangeListener((evt) -> iconIt(GreyShadesController.imageToGreyColorFilterEnhanced(rgbToGreyEnhancedFilterPanel.getFilter())));
        rgbToGreyEnhancedFilterPanel.slider2.addChangeListener((evt) -> iconIt(GreyShadesController.imageToGreyColorFilterEnhanced(rgbToGreyEnhancedFilterPanel.getFilter())));
        rgbToGreyEnhancedFilterPanel.slider3.addChangeListener((evt) -> iconIt(GreyShadesController.imageToGreyColorFilterEnhanced(rgbToGreyEnhancedFilterPanel.getFilter())));

        customNormalGrey.addActionListener((actionEvent) -> {
            if (ImageController.getImg() != null) {
                cards.show(contentPanel, RGB_TO_GREY_NORMAL_FILTER_VIEW);
                GreyShadesController.setIMG(ImageController.getImg());
                setChanges();
            }
        });
        rgbToGreyNormalFilterPanel.slider1.addChangeListener((evt) -> iconIt(GreyShadesController.imageToGreyColorFilterNormal(rgbToGreyNormalFilterPanel.getFilter())));
        rgbToGreyNormalFilterPanel.slider2.addChangeListener((evt) -> iconIt(GreyShadesController.imageToGreyColorFilterNormal(rgbToGreyNormalFilterPanel.getFilter())));
        rgbToGreyNormalFilterPanel.slider3.addChangeListener((evt) -> iconIt(GreyShadesController.imageToGreyColorFilterNormal(rgbToGreyNormalFilterPanel.getFilter())));

        negative.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(NegativeController.action(ImageController.getImg()));
                setChanges();
            }
        });

        binarisationMean.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(BinarController.action(ImageController.getImg()));
                setChanges();
            }
        });

        binarisationManual.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                cards.show(contentPanel, BINAR_VIEW);

                binarPanel.slider.setValue(0);

                BinarSmartController.setIMG(ImageController.getImg());
            }
        });
        binarPanel.slider.addChangeListener((changeEvent) -> iconIt(BinarSmartController.action(binarPanel.slider.getValue())));

        brightnessCorrect.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                cards.show(contentPanel, BRIGHTNESS_VIEW);

                brightnessPanel.slider.setValue(0);

                BrightnessController.setIMG(ImageController.getImg());
            }
        });
        brightnessPanel.slider.addChangeListener((changeEvent) -> iconIt(BrightnessController.action(brightnessPanel.slider.getValue())));

        contrastCorrect.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                cards.show(contentPanel, CONTRAST_VIEW);

                contrastPanel.slider.setValue(0);

                ContrastController.setIMG(ImageController.getImg());
            }
        });
        contrastPanel.slider.addChangeListener((changeEvent) -> iconIt(ContrastController.action(contrastPanel.slider.getValue())));

        charts.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                if (ChartController.getMainChart() != null)
                    chartsPanel.remove(ChartController.getMainChart());

                ChartController.createCharts(ImageController.getImg());
                chartsPanel.add(ChartController.getMainChart());

                cards.show(contentPanel, CHARTS_VIEW);
            }
        });


        meanFilter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}));
                setChanges();
            }
        });

        lp1Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 1, 1}, {1, 2, 1}, {1, 1, 1}}));
                setChanges();
            }
        });

        lp2Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 1, 1}, {1, 4, 1}, {1, 1, 1}}));
                setChanges();
            }
        });

        lp3Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 1, 1}, {1, 12, 1}, {1, 1, 1}}));
                setChanges();
            }
        });

        squareFilter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}}));
                setChanges();
            }
        });

        circleFilter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{0, 1, 1, 1, 0}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {0, 1, 1, 1, 0}}));
                setChanges();
            }
        });

        coneFilter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{0, 0, 1, 0, 0}, {0, 2, 2, 2, 0}, {1, 2, 5, 2, 1}, {0, 2, 2, 2, 0}, {0, 0, 1, 0, 0}}));
                setChanges();
            }
        });

        pyramidFilter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 2, 3, 2, 1}, {2, 4, 6, 4, 2}, {3, 6, 9, 6, 3}, {2, 4, 6, 4, 2}, {1, 2, 3, 2, 1}}));
                setChanges();
            }
        });

        gauss1Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 2, 1}, {2, 4, 2}, {1, 2, 1}}));
                setChanges();
            }
        });

        gauss2Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 1, 2, 1, 1}, {1, 2, 4, 2, 1}, {2, 4, 8, 4, 2}, {1, 2, 4, 2, 1}, {1, 1, 2, 1, 1}}));
                setChanges();
            }
        });

        gauss3Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{0, 1, 2, 1, 0}, {1, 4, 8, 4, 1}, {2, 8, 16, 8, 2}, {1, 4, 8, 4, 1}, {0, 1, 2, 1, 0}}));
                setChanges();
            }
        });

        gauss4Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 4, 7, 4, 1}, {4, 16, 26, 26, 4}, {7, 26, 41, 26, 7}, {4, 26, 16, 26}, {1, 4, 7, 4, 1}}));
                setChanges();
            }
        });

        gauss5Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new int[][]{{1, 1, 2, 2, 2, 1, 1}, {1, 2, 2, 4, 2, 2, 1}, {2, 2, 4, 8, 4, 2, 2}, {2, 4, 8, 16, 8, 4, 2}, {2, 2, 4, 8, 4, 2, 2}, {1, 2, 2, 4, 2, 2, 1}, {1, 1, 2, 2, 2, 1, 1}}));
                setChanges();
            }
        });

        hp0Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new Kernel(3, 3, new float[] {-1f, -1f, -1f, -1f, 9f, -1f, -1f, -1f, -1f})));
                setChanges();
            }
        });

        hp1Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new Kernel(3, 3, new float[] {0f, -1f, 0f, -1f, 5f, -1f, 0f, -1f, 0f})));
                setChanges();
            }
        });

        hp2Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(FilterController.action(ImageController.getImg(), new Kernel(3, 3, new float[] {1f, -2f, 1f, -2f, 5f, -2f, 1f, -2f, 1f})));
                setChanges();
            }
        });

        simpleColoursFilter.addActionListener((evt) -> {
            if (ImageController.getImg() != null)
                cards.show(contentPanel, ONLY_COLOURS_VIEW);
        });
        onlyColours.acceptButton.addActionListener((evnt) -> iconIt(ColourFilterController.filterOutColors(ImageController.getImg(), onlyColours.getFilterValue())));

        greyColoursFilter.addActionListener((evt) -> {
            if (ImageController.getImg() != null)
                cards.show(contentPanel, GREY_COLOURS_VIEW);

        });
        greyColours.acceptButton.addActionListener((evnt) -> iconIt(ColourFilterController.filterAndGrey(ImageController.getImg(), greyColours.getFilterValue())));

        custom0Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null)
                cards.show(contentPanel, CUSTOM_SMALL_VIEW);
        });
        customSPanel.acceptButton.addActionListener((evnt) -> iconIt(FilterController.action(ImageController.getImg(), customSPanel.getMatrix())));

        custom1Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null)
                cards.show(contentPanel, CUSTOM_MEDIUM_VIEW);
        });
        customMPanel.acceptButton.addActionListener((evnt) -> iconIt(FilterController.action(ImageController.getImg(), customMPanel.getMatrix())));

        custom2Filter.addActionListener((evt) -> {
            if (ImageController.getImg() != null)
                cards.show(contentPanel, CUSTOM_LARGE_VIEW);
        });
        customLPanel.acceptButton.addActionListener((evnt) -> iconIt(FilterController.action(ImageController.getImg(), customLPanel.getMatrix())));

        kuwahars.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                cards.show(contentPanel,KUWAHARA_VIEW);
            }
        });
        kuhawaraPanel.acceptButton.addActionListener((evt) -> iconIt(KuwaharasController.action(ImageController.getImg(), kuhawaraPanel.getInput())));

        roberts.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(RoberstsCross.action(ImageController.getImg()));
                setChanges();
            }
        });

        sobels.addActionListener((evt) -> {
            if (ImageController.getImg() != null) {
                iconIt(SobelsOperator.action(ImageController.getImg()));
                setChanges();
            }
        });
    }

    /**
     * Ustawienie menu z odpowiednimi itemami, oraz dodanie do kontenera paneli pomocniczych pod odpowiednimi kluczami.
     *
     * @param parent okno wyświetlające ten panel
     */
    public WorkingPanel(JFrame parent) {
        super();
        this.parent = parent;
        parent.setTitle("empty project");
        add(mainPanel);

        contentPanel.add(mainView, MAIN_VIEW);
        contentPanel.add(rgbToGreyEnhancedFilterPanel, RGB_TO_GREY_ENHANCED_FILTER_VIEW);
        contentPanel.add(rgbToGreyNormalFilterPanel, RGB_TO_GREY_NORMAL_FILTER_VIEW);
        contentPanel.add(brightnessPanel, BRIGHTNESS_VIEW);
        contentPanel.add(contrastPanel, CONTRAST_VIEW);
        contentPanel.add(binarPanel, BINAR_VIEW);
        contentPanel.add(chartsPanel, CHARTS_VIEW);
        contentPanel.add(onlyColours, ONLY_COLOURS_VIEW);
        contentPanel.add(greyColours, GREY_COLOURS_VIEW);
        contentPanel.add(customSPanel, CUSTOM_SMALL_VIEW);
        contentPanel.add(customMPanel, CUSTOM_MEDIUM_VIEW);
        contentPanel.add(customLPanel, CUSTOM_LARGE_VIEW);
        contentPanel.add(kuhawaraPanel, KUWAHARA_VIEW);

        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.addSeparator();
//        undo.setEnabled(false);
        fileMenu.add(undo);
        fileMenu.add(clear);
        menuBar.add(fileMenu);

        greyShades.add(meanGrey);
        greyShades.add(scaleGrey);
        greyShades.add(customNormalGrey);
        greyShades.add(customEnhancedGrey);
        pixOpsMenu.add(greyShades);
        pixOpsMenu.addSeparator();
        pixOpsMenu.add(brightnessCorrect);
        pixOpsMenu.add(contrastCorrect);
        pixOpsMenu.add(negative);
        pixOpsMenu.add(binarisation);
        binarisation.add(binarisationMean);
        binarisation.add(binarisationManual);
        menuBar.add(pixOpsMenu);

        graphs.add(charts);
//        graphs.add(projections);
        menuBar.add(graphs);

        lpFilters.add(meanFilter);
        lpFilters.add(lp1Filter);
        lpFilters.add(lp2Filter);
        lpFilters.add(lp3Filter);
        lpFilters.add(squareFilter);
        lpFilters.add(circleFilter);
        lpFilters.add(coneFilter);
        lpFilters.add(pyramidFilter);
        lpFilters.add(gauss1Filter);
        lpFilters.add(gauss2Filter);
        lpFilters.add(gauss3Filter);
        lpFilters.add(gauss4Filter);
        lpFilters.add(gauss5Filter);
        lpFilters.add(kuwahars);
        graphicalFilters.add(lpFilters);

        hpFilters.add(hp0Filter);
        hpFilters.add(hp1Filter);
        hpFilters.add(hp2Filter);
        graphicalFilters.add(hpFilters);

        colourFilters.add(simpleColoursFilter);
        colourFilters.add(greyColoursFilter);
        graphicalFilters.add(colourFilters);

        customFilters.add(custom0Filter);
        customFilters.add(custom1Filter);
        customFilters.add(custom2Filter);
        graphicalFilters.add(customFilters);
        menuBar.add(graphicalFilters);

        borders.add(roberts);
        borders.add(sobels);
        menuBar.add(borders);

        cards.first(contentPanel);
    }

    /**
     * Zapisuje podany obraz w statycznym kontrolerze, oraz pokazuje go na głównym panelu
     *
     * @param img obraz do wyświetlenia
     */
    public void iconIt(BufferedImage img) {
        if (img != null) {
            ImageController.setIMG(img);
            Image obrazPrzeskalowany = img.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon ikonaOryginalna = new ImageIcon(obrazPrzeskalowany);
            imgLabel.setIcon(ikonaOryginalna);
            parent.setTitle(ImageController.getImagePath().getFileName().toString());
        }
    }

    /**
     * Ustawienie wartości wszystkich suwaków na 0, zapisanie
     * we wszystkich potrzebujących tego klasach nowego obrazu
     * oraz rysuje na nowo zawartość tego panelu.
     */
    private void setChanges() {
        if (ImageController.getImg() != null) {
            ContrastController.setIMG(ImageController.getImg());
            contrastPanel.slider.setValue(0);

            BrightnessController.setIMG(ImageController.getImg());
            brightnessPanel.slider.setValue(0);

            BinarSmartController.setIMG(ImageController.getImg());
            binarPanel.slider.setValue(0);

            rgbToGreyEnhancedFilterPanel.slider1.setValue(0);
            rgbToGreyEnhancedFilterPanel.slider2.setValue(0);
            rgbToGreyEnhancedFilterPanel.slider3.setValue(0);

            rgbToGreyNormalFilterPanel.slider1.setValue(0);
            rgbToGreyNormalFilterPanel.slider2.setValue(0);
            rgbToGreyNormalFilterPanel.slider3.setValue(0);
            GreyShadesController.setIMG(ImageController.getImg());

            if (ChartController.getMainChart() != null) {
                chartsPanel.remove(ChartController.getMainChart());
                ChartController.createCharts(ImageController.getImg());
                chartsPanel.add(ChartController.getMainChart());
            }

            setVisible(true);
        }
    }
}
