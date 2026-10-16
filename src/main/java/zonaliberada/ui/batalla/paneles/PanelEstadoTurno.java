package zonaliberada.ui.batalla.paneles;

import java.awt.*;
import javax.swing.*;
import zonaliberada.dtos.*;
import zonaliberada.ui.*;

/** Conserva los componentes y actualiza sus textos con los datos del backend. */
public final class PanelEstadoTurno extends PanelSeccion {
    private final JLabel titulo = Estilo.etiqueta("", 18, Font.BOLD);
    private final JTextArea mensajes = Estilo.descripcion("");

    public PanelEstadoTurno() {
        super(new BorderLayout(0, 4));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);
        mensajes.setRows(3);
        mensajes.setName("mensajesCombate");
        JScrollPane historial = Estilo.desplazableVertical(mensajes);
        historial.setPreferredSize(new Dimension(600, 65));
        add(historial, BorderLayout.CENTER);
    }

    public void actualizar(BatallaDTO batalla) {
        PersonajeDTO actor = batalla.aliadoActual();
        String texto = actor == null ? "Sin turno de aliado disponible"
                : "ACTÚA: " + actor.getNombre() + " · PA " + actor.getPuntosAccion() + "/"
                    + actor.getPuntosAccionMaximos() + " · MP " + actor.getMana() + "/" + actor.getManaMaximo();
        if (actor != null && !actor.getAtributoEspecial().isEmpty()) {
            texto += " · " + actor.getAtributoEspecial() + " " + actor.getValorEspecial();
        }
        titulo.setText(texto);
        mensajes.setText(batalla.getMensaje().isBlank()
                ? "Los enemigos actúan automáticamente." : batalla.getMensaje());
        mensajes.setCaretPosition(mensajes.getDocument().getLength());
    }
}
