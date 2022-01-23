package GUI;

import GlobalControllers.Interfaces.CustomFilter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.image.Kernel;

/**
 * Panel do wybierania wag w filtrze 5x5
 */
public class CustomMediumPanel extends JPanel implements CustomFilter {
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JComboBox comboBox9;
    private JComboBox comboBox10;
    private JComboBox comboBox11;
    private JComboBox comboBox12;
    private JComboBox comboBox13;
    private JComboBox comboBox14;
    private JComboBox comboBox15;
    private JComboBox comboBox16;
    private JComboBox comboBox17;
    private JComboBox comboBox18;
    private JComboBox comboBox19;
    private JComboBox comboBox20;
    private JComboBox comboBox21;
    private JComboBox comboBox22;
    private JComboBox comboBox23;
    private JComboBox comboBox24;
    private JComboBox comboBox25;
    public JButton acceptButton;
    private JPanel panel1;
    private JPanel boxes;

    //prosty konstruktor
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
                {comboBox1.getSelectedIndex(), comboBox2.getSelectedIndex(), comboBox3.getSelectedIndex(), comboBox4.getSelectedIndex(), comboBox5.getSelectedIndex()},
                {comboBox6.getSelectedIndex(), comboBox7.getSelectedIndex(), comboBox8.getSelectedIndex(), comboBox9.getSelectedIndex(), comboBox10.getSelectedIndex()},
                {comboBox11.getSelectedIndex(), comboBox12.getSelectedIndex(), comboBox13.getSelectedIndex(), comboBox14.getSelectedIndex(), comboBox15.getSelectedIndex()},
                {comboBox16.getSelectedIndex(), comboBox17.getSelectedIndex(), comboBox18.getSelectedIndex(), comboBox19.getSelectedIndex(), comboBox20.getSelectedIndex()},
                {comboBox21.getSelectedIndex(), comboBox22.getSelectedIndex(), comboBox23.getSelectedIndex(), comboBox24.getSelectedIndex(), comboBox25.getSelectedIndex()}
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
