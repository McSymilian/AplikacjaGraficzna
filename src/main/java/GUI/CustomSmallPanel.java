package GUI;

import GlobalControllers.Interfaces.CustomFilter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.image.Kernel;

/**
 * Panel do wybierania wag w filtrze 3x3
 */
public class CustomSmallPanel extends JPanel implements CustomFilter {
    private JPanel panel1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JComboBox comboBox9;
    private JPanel boxes;
    public JButton acceptButton;

    //prosty konstruktor statyczny
    {
       add(panel1);
    }

    /**
     *
     * @return macierz z wartościami wybranymi
     */
    @Override
    public int[][] getMatrix() {
        return new int[][]{
                {comboBox1.getSelectedIndex(), comboBox2.getSelectedIndex(), comboBox3.getSelectedIndex()},
                {comboBox4.getSelectedIndex(), comboBox5.getSelectedIndex(), comboBox6.getSelectedIndex()},
                {comboBox7.getSelectedIndex(), comboBox8.getSelectedIndex(), comboBox9.getSelectedIndex()}
        };
    }

    /**
     * Metoda przygotowana w razie potrzeby zmiany macierzy na kernel
     *
     * @return kernel z wartościami wpisanymi
     */
    @Override
    public @Nullable Kernel getKernel() {
        return null;
    }
}
