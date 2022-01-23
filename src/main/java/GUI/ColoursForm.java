package GUI;

import GlobalControllers.Abstracts.ColourController;

import javax.swing.*;

/**
 * Panel do wyboru koloru z kombinacji binarnej trzech barw
 */
public class ColoursForm extends JPanel{
    private JCheckBox czerwonyCheckBox;
    private JPanel panel1;
    private JCheckBox zielonyCheckBox;
    private JCheckBox niebieskiCheckBox;
    private JPanel boxes;
    public JButton acceptButton;

    //prosty konstrukor
    {
        add(panel1);
    }

    /**
     * Kanały mają skrajne wartości w zależności od odpowiedniego, wybranego pola
     *
     * @return model RGB kolorów w formacie liczby całkowitej
     */
    public int getFilterValue() {
        return ColourController.toRGB(czerwonyCheckBox.isSelected() ? 255 : 0, zielonyCheckBox.isSelected() ? 255 : 0, niebieskiCheckBox.isSelected() ? 255 : 0);
    }
}
