package GUI;

import GlobalControllers.ColourModels.RGB;

import javax.swing.*;

public class TripleSliderPanel extends JPanel {
    public JSlider slider1;
    private JPanel panel1;
    public JSlider slider2;
    public JSlider slider3;

    public TripleSliderPanel() {
        super();
        add(panel1);
    }

    /**
     * @return aktualne wartości suwaków w modelu RGB
     */
    public RGB getFilter() {
        return new RGB(slider1.getValue(), slider2.getValue(), slider3.getValue());
    }

}
