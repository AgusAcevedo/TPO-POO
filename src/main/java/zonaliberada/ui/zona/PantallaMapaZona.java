package zonaliberada.ui.zona;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.zona.paneles.PanelUbicaciones;

/** Ubicaciones de la zona: tarjetas consecutivas de batalla, mejora y boss. */
public final class PantallaMapaZona extends PantallaBase {
    public PantallaMapaZona(Consumer<PantallaId> navegar) {
        super("ZONA A · PALERMO", "zona_palermo.png", navegar);

        add(Estilo.centrarFila(new PanelUbicaciones(this::mostrar, this::avisoPendiente), 260),
                BorderLayout.CENTER);

        JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));
        pie.add(ir("PARTY E INVENTARIO", Estilo.VERDE, PantallaId.PARTY));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
    }
}
