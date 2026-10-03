package zonaliberada.ui.batalla;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.batalla.paneles.PanelAcciones;
import zonaliberada.ui.batalla.paneles.PanelBandos;
import zonaliberada.ui.batalla.paneles.PanelEstadoTurno;

/** Tablero y acciones de ejemplo; no resuelve daño, iniciativa ni turnos reales. */
public final class PantallaBatalla extends PantallaBase {
    public PantallaBatalla(Consumer<PantallaId> navegar) {
        super("BATALLA", "combate.png", navegar);

        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 10));
        cuerpo.add(new PanelEstadoTurno(), BorderLayout.NORTH);

        // Dos bandos uno junto al otro. Las líneas frontales quedan enfrentadas.
        cuerpo.add(new PanelBandos(), BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER);
        add(new PanelAcciones(this::mostrar, this::avisoPendiente), BorderLayout.SOUTH);
    }
}
