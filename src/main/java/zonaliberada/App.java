package zonaliberada;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Punto de entrada: prepara la interfaz, no contiene reglas del juego. */
public final class App {
    private App() {
    }

    public static void main(String[] args) {
        // El aspecto nativo de macOS puede ignorar los colores de las barras.
        // Metal es el aspecto estándar de Swing y se ve igual desde IntelliJ.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception error) {
            // Si no estuviera disponible, Swing usa el aspecto predeterminado.
            System.err.println("No se pudo aplicar el aspecto de Swing: " + error.getMessage());
        }
        // Swing gestiona la interfaz en su propio hilo (EDT).
        // invokeLater agenda la creación de la ventana en ese hilo.
        SwingUtilities.invokeLater(() -> new VentanaJuego().setVisible(true));
    }
}
