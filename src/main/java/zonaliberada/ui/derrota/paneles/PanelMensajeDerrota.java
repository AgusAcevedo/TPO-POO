package zonaliberada.ui.derrota.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Mensaje central de derrota; no restaura por sí mismo un checkpoint. */
public final class PanelMensajeDerrota extends PanelSeccion {
    public PanelMensajeDerrota() {
        super(new BorderLayout());
        JLabel mensaje = Estilo.etiqueta("EL EQUIPO FUE DERROTADO", 28, Font.BOLD);
        mensaje.setForeground(Estilo.ROJO);
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        add(mensaje, BorderLayout.CENTER);
    }
}
