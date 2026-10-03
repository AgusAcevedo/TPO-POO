package zonaliberada.ui.mapa.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.Imagenes;
import zonaliberada.ui.PanelSeccion;

/** Fila de zonas del mapa; cada tarjeta conserva su botón de ingreso. */
public final class PanelZonas extends PanelSeccion {
    public PanelZonas(Consumer<PantallaId> navegar) {
        // Una fila, cuatro columnas. Las tarjetas mantienen el mismo tamaño.
        super(new GridLayout(1, 4, 14, 0));
        add(zona("ZONA A", "Palermo · CABA", "Planetario / Bosques de Palermo",
                "zona_palermo.png", true, navegar));
        add(zona("ZONA B", "Barracas · CABA", "Puente / barrio",
                "zona_barracas.png", false, navegar));
        add(zona("ZONA C", "Avellaneda · Zona Sur", "Estadio / estación",
                "zona_avellaneda.png", false, navegar));
        add(zona("ZONA D", "Morón · Zona Oeste", "Plaza / estación",
                "zona_moron.png", false, navegar));
    }

    private JPanel zona(String nombre, String lugar, String referencia,
            String miniatura, boolean abierta, Consumer<PantallaId> navegar) {
        JPanel contenido = Estilo.panel(new BorderLayout(0, 8));
        // Cada zona tiene una vista previa distinta, incluso si aún está cerrada.
        JLabel paisaje = new JLabel(Imagenes.icono("/imagenes/fondos/" + miniatura, 196, 110));
        paisaje.setHorizontalAlignment(SwingConstants.CENTER);
        contenido.add(paisaje, BorderLayout.NORTH);
        // Swing admite HTML básico en las etiquetas para partir líneas.
        JLabel descripcion = Estilo.etiqueta("<html><center>" + lugar
                + "<br><br>" + referencia + "</center></html>", 14, Font.PLAIN);
        descripcion.setHorizontalAlignment(SwingConstants.CENTER);
        contenido.add(descripcion, BorderLayout.CENTER);

        if (abierta) {
            JButton ingresar = Estilo.boton("INGRESAR", Estilo.VERDE);
            ingresar.addActionListener(evento -> navegar.accept(PantallaId.MAPA_ZONA));
            contenido.add(ingresar, BorderLayout.SOUTH);
        } else {
            // Una zona cerrada no ofrece una acción que el jugador no pueda usar.
            JLabel bloqueada = Estilo.etiqueta("BLOQUEADA", 14, Font.BOLD);
            bloqueada.setHorizontalAlignment(SwingConstants.CENTER);
            bloqueada.setForeground(Estilo.ROJO);
            contenido.add(bloqueada, BorderLayout.SOUTH);
        }
        return Estilo.tarjeta(nombre, contenido);
    }
}
