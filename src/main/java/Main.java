import GUI.MyFrame;

import java.awt.*;

public class Main {
    /**
     * Start działania okna na osobnym wątku
     *
     * @param args argumenty startowe programu
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(MyFrame::new);
    }
}
