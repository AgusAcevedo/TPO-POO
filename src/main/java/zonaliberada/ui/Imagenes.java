package zonaliberada.ui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/** Carga los PNG una sola vez y prepara íconos pequeños para Swing. */
public final class Imagenes {
    private static final Map<String, BufferedImage> CARGADAS = new HashMap<>();

    private Imagenes() {
    }

    public static BufferedImage cargar(String ruta) {
        // El caché evita volver a leer el mismo PNG al abrir otra pantalla.
        return CARGADAS.computeIfAbsent(ruta, Imagenes::leer);
    }

    public static ImageIcon icono(String ruta, int ancho, int alto) {
        BufferedImage original = cargar(ruta);
        BufferedImage reducido = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D dibujo = reducido.createGraphics();
        // Vecino más cercano mantiene el aspecto pixelado al reducir la imagen.
        dibujo.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        dibujo.drawImage(original, 0, 0, ancho, alto, null);
        dibujo.dispose();
        return new ImageIcon(reducido);
    }

    private static BufferedImage leer(String ruta) {
        try {
            URL recurso = Imagenes.class.getResource(ruta);
            // IntelliJ puede ejecutar el proyecto sin marcar resources como raíz.
            // Esta segunda opción funciona si el directorio de trabajo es el repo.
            BufferedImage imagen = recurso != null
                    ? ImageIO.read(recurso)
                    : ImageIO.read(Path.of("src/main/resources" + ruta).toFile());
            if (imagen == null) {
                throw new IllegalStateException("No se pudo leer la imagen: " + ruta);
            }
            return imagen;
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo cargar la imagen: " + ruta, error);
        }
    }
}
