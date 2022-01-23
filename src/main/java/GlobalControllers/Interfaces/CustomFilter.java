package GlobalControllers.Interfaces;

import org.jetbrains.annotations.Nullable;

import java.awt.image.Kernel;

/**
 * Interfejs dla GUI przeznaczonego do ustawiania własnych wag filtrów
 */
public interface CustomFilter {
    /**
     * Na podstawie podanych wartości tworzy matrycę wag
     *
     * @return matryca wag filtru
     */
    int[][] getMatrix();

    /**
     * Na podstawie podanych wartości tworzy odpowiednik matrycy wag w postaci kernela
     *
     * @return kernel wag filtrów
     */
    @Nullable
    Kernel getKernel();
}
