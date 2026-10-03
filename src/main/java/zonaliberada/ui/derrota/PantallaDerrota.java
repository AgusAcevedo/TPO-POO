package zonaliberada.ui.derrota;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.derrota.paneles.PanelMensajeDerrota;

/** Resultado de derrota: permite reintentar el combate o volver al menú. */
public final class PantallaDerrota extends PantallaBase {
    public PantallaDerrota(Consumer<PantallaId> navegar) {
        super("DERROTA", "derrota.png", navegar);

        // La vista informa el resultado. Restaurar el checkpoint será tarea
        // del controlador, no de este panel ni de su botón Reintentar.
        add(new PanelMensajeDerrota(), BorderLayout.CENTER);

        JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));
        pie.add(ir("REINTENTAR BATALLA", Estilo.VERDE, PantallaId.BATALLA));
        pie.add(ir("VOLVER AL MENÚ", Estilo.GRIS, PantallaId.INICIO));
        add(pie, BorderLayout.SOUTH);
    }
}
