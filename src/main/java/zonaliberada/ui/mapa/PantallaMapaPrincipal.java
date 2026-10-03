package zonaliberada.ui.mapa;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.mapa.paneles.PanelZonas;

/** Selección de zonas en orden A, B, C y D; el desbloqueo aún es de muestra. */
public final class PantallaMapaPrincipal extends PantallaBase {
    public PantallaMapaPrincipal(Consumer<PantallaId> navegar) {
        super("SELECCIÓN DE ZONAS", "zonas.png", navegar);

        add(Estilo.centrarFila(new PanelZonas(this::mostrar), 315), BorderLayout.CENTER);

        JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));
        pie.add(ir("PARTY E INVENTARIO", Estilo.VERDE, PantallaId.PARTY));
        pie.add(ir("VOLVER AL MENÚ", Estilo.GRIS, PantallaId.INICIO));
        add(pie, BorderLayout.SOUTH);
    }
}
