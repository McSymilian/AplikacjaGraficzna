package GUI;

import GlobalControllers.Interfaces.CustomFilter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.image.Kernel;

/**
 * Panel do wybierania wag w filtrze 7x7
 */
public class CustomLargePanel extends JPanel implements CustomFilter {
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
    private JComboBox comboBox26;
    private JComboBox comboBox27;
    private JComboBox comboBox28;
    private JComboBox comboBox29;
    private JComboBox comboBox30;
    private JComboBox comboBox31;
    private JComboBox comboBox32;
    private JComboBox comboBox33;
    private JComboBox comboBox34;
    private JComboBox comboBox35;
    private JComboBox comboBox36;
    private JComboBox comboBox37;
    private JComboBox comboBox38;
    private JComboBox comboBox39;
    private JComboBox comboBox40;
    private JComboBox comboBox41;
    private JComboBox comboBox42;
    private JComboBox comboBox43;
    private JComboBox comboBox44;
    private JComboBox comboBox45;
    private JComboBox comboBox46;
    private JComboBox comboBox47;
    private JComboBox comboBox48;
    private JComboBox comboBox49;
    private JPanel boxes;
    public JButton acceptButton;

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
                {comboBox1.getSelectedIndex(), comboBox2.getSelectedIndex(), comboBox3.getSelectedIndex(), comboBox4.getSelectedIndex(), comboBox5.getSelectedIndex(), comboBox6.getSelectedIndex(), comboBox7.getSelectedIndex()},
                {comboBox8.getSelectedIndex(), comboBox9.getSelectedIndex(), comboBox10.getSelectedIndex(), comboBox11.getSelectedIndex(), comboBox12.getSelectedIndex(), comboBox13.getSelectedIndex(), comboBox14.getSelectedIndex()},
                {comboBox15.getSelectedIndex(), comboBox16.getSelectedIndex(), comboBox17.getSelectedIndex(), comboBox18.getSelectedIndex(), comboBox19.getSelectedIndex(), comboBox20.getSelectedIndex(), comboBox21.getSelectedIndex()},
                {comboBox22.getSelectedIndex(), comboBox23.getSelectedIndex(), comboBox24.getSelectedIndex(), comboBox25.getSelectedIndex(), comboBox26.getSelectedIndex(), comboBox27.getSelectedIndex(), comboBox28.getSelectedIndex()},
                {comboBox29.getSelectedIndex(), comboBox30.getSelectedIndex(), comboBox31.getSelectedIndex(), comboBox32.getSelectedIndex(), comboBox33.getSelectedIndex(), comboBox34.getSelectedIndex(), comboBox35.getSelectedIndex()},
                {comboBox36.getSelectedIndex(), comboBox37.getSelectedIndex(), comboBox38.getSelectedIndex(), comboBox39.getSelectedIndex(), comboBox40.getSelectedIndex(), comboBox41.getSelectedIndex(), comboBox42.getSelectedIndex()},
                {comboBox43.getSelectedIndex(), comboBox44.getSelectedIndex(), comboBox45.getSelectedIndex(), comboBox46.getSelectedIndex(), comboBox47.getSelectedIndex(), comboBox48.getSelectedIndex(), comboBox49.getSelectedIndex()}
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
