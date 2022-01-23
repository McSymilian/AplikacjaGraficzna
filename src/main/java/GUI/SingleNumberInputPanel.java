package GUI;

import javax.swing.*;

public class SingleNumberInputPanel extends JPanel{
    private JComboBox comboBox1;
    private JPanel panel1;
    public JButton acceptButton;

    public SingleNumberInputPanel() {
        super();
        add(panel1);
    }

    /**
     *
     * @return wartość wybrana w comboBoxie w postaci liczby całkowitej
     */
    public int getInput() {
        return Integer.parseInt(String.valueOf(comboBox1.getSelectedItem()));
    }
}
