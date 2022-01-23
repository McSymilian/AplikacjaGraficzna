package GlobalControllers;

import GlobalControllers.Abstracts.ColourController;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Operator głównego obrazu, odpowiedzialny za zapis, odczyt i przechowywanie obrazu
 */
public class ImageController extends ColourController {
    @Nullable
    @Getter
    private static BufferedImage img;

    @Nullable
    @Getter
    private static Path imagePath;

    @Nullable
    @Getter
    private static BufferedImage lastCopy;

    @Nullable
    private static BufferedImage originalImg;

    /**
     * Kopiuje stary obraz i zapisuje nowy w jego miejsce
     *
     * @param image obraz do zapisania
     */
    public static void setIMG(@NotNull final BufferedImage image) {
        assert img != null;
        lastCopy = deepCopyImage(img);
        img = image;
    }


    /**
     * Używa JFileChooser'a w celu wybrania pliku do obróbki, i zapisuje wybrany plik
     */
    public static void loadImg(JFrame parent) {
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("jpg, jpeg, gif, png", "jpg", "jpeg", "gif", "png");
        var jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(filter);
        jFileChooser.showOpenDialog(parent);

        try {
            if (jFileChooser.getSelectedFile() != null) {
                File file = new File(jFileChooser.getSelectedFile().getAbsolutePath());
                img = ImageIO.read(file);
                lastCopy = deepCopyImage(img);
                originalImg = deepCopyImage(img);
                imagePath = file.toPath();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Zapisuje obraz w wybranej lokalizacji pod wybraną pod podaną nazwą z rozszerzeniem jpg
     */
    public static void saveImg(JFrame parent) {
        if (img != null) {
            var chooser = new JFileChooser();
            if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                try {
                    var file = new File(chooser.getSelectedFile() + ".jpg");
                    ImageIO.write(img, "jpg", file);
                    imagePath = file.toPath();
                    originalImg = deepCopyImage(img);
                    lastCopy = deepCopyImage(img);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Zastępuje aktualny obraz oryginalnym.
     *
     * @return obraz lub null
     */
    @Nullable
    public static BufferedImage resetChanges() {
        if (originalImg != null)
            return img = deepCopyImage(originalImg);
        else
            return null;
    }
}
