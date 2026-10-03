package zonaliberada.ui.inventario;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.inventario.paneles.PanelConsumibles;
import zonaliberada.ui.inventario.paneles.PanelEquipables;

/** Inventario compartido: muestra objetos, pero todavía no los modifica. */
public final class PantallaInventario extends PantallaBase {
    public PantallaInventario(Consumer<PantallaId> navegar) {
        super("INVENTARIO", "inventario.png", navegar);

        // Dos tarjetas separan lo que se consume en combate de lo que se equipa.
        JPanel columnas = Estilo.panel(new GridLayout(1, 2, 12, 0));
        columnas.add(new PanelConsumibles());
        columnas.add(new PanelEquipables());
        add(Estilo.centrarFila(columnas, 380), BorderLayout.CENTER);

        JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));
        pie.add(ir("VOLVER A PARTY", Estilo.VERDE, PantallaId.PARTY));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
    }
}
