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
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import zonaliberada.dtos.PersonajeDTO;

/** Carga los PNG una sola vez y prepara íconos pequeños para Swing. */
public final class Imagenes {
    private static final Map<String, BufferedImage> CARGADAS = new HashMap<>();

    private Imagenes() {
    }

    public static BufferedImage cargar(String ruta) {
        // El caché evita volver a leer el mismo PNG al abrir otra pantalla.
        BufferedImage imagen = CARGADAS.get(ruta);
        if (imagen == null) {
            imagen = leer(ruta);
            CARGADAS.put(ruta, imagen);
        }
        return imagen;
    }

    public static String fondoZona(int indice) {
        return switch (indice) {
            case 0 -> "zona_moron.png";
            case 1 -> "zona_barracas.png";
            default -> "zonas.png";
        };
    }

    /** Un boss sin sprite se identifica por texto; no se inventa su apariencia. */
    public static JLabel personaje(PersonajeDTO personaje, int ancho, int alto) {
        String archivo = personaje.getId().startsWith("aliado:")
                ? personaje.getId().substring(7) : switch (personaje.getNombre()) {
                    case "Motochorro" -> "motochorro";
                    case "Barra brava", "Refuerzo de la barra" -> "barra_brava";
                    case "Piquetero" -> "piquetero";
                    case "Fisura" -> "fisura";
                    case "Trapito" -> "trapito";
                    case "Transa" -> "transa";
                    case "Patovica" -> "patovica";
                    default -> "";
                };
        String ruta = "/imagenes/personajes/"
                + (personaje.getId().startsWith("aliado:") ? "aliados/" : "enemigos/")
                + archivo + ".png";
        JLabel figura = new JLabel("BOSS".equals(personaje.getRol()) ? "JEFE" : "ALIADO",
                SwingConstants.CENTER);
        figura.setPreferredSize(new java.awt.Dimension(ancho, alto));
        figura.setForeground(Estilo.VERDE);
        if (!archivo.isEmpty() && archivo.matches("[a-z0-9_-]+")
                && (Imagenes.class.getResource(ruta) != null
                    || java.nio.file.Files.isRegularFile(Path.of("src/main/resources" + ruta)))) {
            figura.setText("");
            figura.setIcon(icono(ruta, ancho, alto));
        }
        figura.getAccessibleContext().setAccessibleName(personaje.getNombre());
        return figura;
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
