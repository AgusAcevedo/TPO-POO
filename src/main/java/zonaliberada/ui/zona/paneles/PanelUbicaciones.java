package zonaliberada.ui.zona.paneles;

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
import zonaliberada.ui.PanelSeccion;

/** Las cinco ubicaciones fijas de la zona A, en el mismo orden del mapa. */
public final class PanelUbicaciones extends PanelSeccion {
    public PanelUbicaciones(Consumer<PantallaId> navegar, Consumer<String> avisoPendiente) {
        // Cinco tarjetas iguales, una al lado de otra.
        super(new GridLayout(1, 5, 12, 0));
        add(ubicacion("BATALLA 1", "Completada<br>Se puede repetir", "REPETIR",
                PantallaId.BATALLA, navegar, avisoPendiente));
        add(ubicacion("MEJORA 1", "Disponible<br>Recompensa de zona", "RECIBIR",
                null, navegar, avisoPendiente));
        add(ubicacion("BATALLA 2", "Batalla normal", null, null, navegar, avisoPendiente));
        add(ubicacion("MEJORA 2", "Recompensa de zona", null, null, navegar, avisoPendiente));
        add(ubicacion("BOSS", "Boss de zona<br>No se puede repetir", null, null,
                navegar, avisoPendiente));
    }

    private JPanel ubicacion(String titulo, String descripcion, String accion, PantallaId destino,
            Consumer<PantallaId> navegar, Consumer<String> avisoPendiente) {
        JPanel contenido = Estilo.panel(new BorderLayout(0, 14));
        JLabel texto = Estilo.etiqueta("<html><center>" + descripcion + "</center></html>",
                13, Font.PLAIN);
        texto.setHorizontalAlignment(SwingConstants.CENTER);
        contenido.add(texto, BorderLayout.CENTER);

        // Una acción null representa una ubicación bloqueada en esta maqueta.
        if (accion == null) {
            JLabel bloqueada = Estilo.etiqueta("BLOQUEADA", 13, Font.BOLD);
            bloqueada.setForeground(Estilo.ROJO);
            bloqueada.setHorizontalAlignment(SwingConstants.CENTER);
            contenido.add(bloqueada, BorderLayout.SOUTH);
        } else {
            JButton boton = Estilo.boton(accion, Estilo.VERDE);
            if (destino == null) {
                // Las recompensas todavía no modifican estadísticas ni objetos.
                boton.addActionListener(evento -> avisoPendiente.accept("Resolver esta ubicación"));
            } else {
                boton.addActionListener(evento -> navegar.accept(destino));
            }
            contenido.add(boton, BorderLayout.SOUTH);
        }
        return Estilo.tarjeta(titulo, contenido);
    }
}
