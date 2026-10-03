package zonaliberada.ui.batalla.paneles;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Actor actual, puntos de acción y próximos turnos de muestra. */
public final class PanelEstadoTurno extends PanelSeccion {
    public PanelEstadoTurno() {
        super(new GridLayout(2, 1));
        // Estos valores son fijos. El controlador actualizará el actor y los PA.
        JLabel pa = Estilo.etiqueta("ACTÚA: Gaucho · PA: 2/2", 19, Font.BOLD);
        pa.setHorizontalAlignment(SwingConstants.CENTER);
        add(pa);
        JLabel orden = Estilo.etiqueta(
                "PRÓXIMOS: Médico → Electricista → Barra brava",
                13, Font.PLAIN);
        orden.setHorizontalAlignment(SwingConstants.CENTER);
        add(orden);
    }
}
