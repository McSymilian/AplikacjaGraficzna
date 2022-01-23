package GUI;

import GlobalControllers.ImageController;

import javax.swing.*;
import java.awt.*;

/**
 * Głowne okno aplikacji
 */
public class MyFrame extends JFrame {
    private JPanel contentPanel;

    //operator paneli
    private final CardLayout cards = (CardLayout) contentPanel.getLayout();

    //panele operaycjne
    private final StartPanel startPanel = new StartPanel();
    private final WorkingPanel workingPanel = new WorkingPanel(this);

    //klucze do odpowiednich paneli
    private final String WORKING_VIEW = "working_view";
    private final String START_VIEW = "start_view";

    //akcja wywołana kliknięciem przycisku, zmieniająca aktualny panel na ostateczny
    {
        startPanel.openButton.addActionListener((evt) -> {
            ImageController.loadImg(this);
            if (ImageController.getImg() != null) {
                workingPanel.iconIt(ImageController.getImg());
                cards.show(contentPanel, WORKING_VIEW);
                setJMenuBar(workingPanel.menuBar);
                pack();
            }
        });
    }

    /**
     * Setup okna
     */
    public MyFrame() {
        super();
        add(contentPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        contentPanel.add(startPanel, START_VIEW);
        contentPanel.add(workingPanel, WORKING_VIEW);

        cards.show(contentPanel, START_VIEW);
        setSize(new Dimension(500, 500));
    }
}
