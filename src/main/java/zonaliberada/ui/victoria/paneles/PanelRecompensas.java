package zonaliberada.ui.victoria.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JPanel;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Objetos anunciados al ganar; todavía no se agregan al inventario. */
public final class PanelRecompensas extends PanelSeccion {
    public PanelRecompensas() {
        super(new BorderLayout());
        JPanel lista = Estilo.panel(new GridLayout(4, 1, 0, 12));
        lista.add(Estilo.etiqueta("Mate × 1", 17, Font.BOLD));
        lista.add(Estilo.etiqueta("Fernet × 1", 17, Font.BOLD));
        lista.add(Estilo.etiqueta("Equipable: protección", 17, Font.BOLD));
        lista.add(Estilo.etiqueta("<html>Objetos de ejemplo.<br>"
                + "Las recompensas únicas no se duplican al repetir.</html>", 13, Font.PLAIN));
        add(Estilo.tarjeta("RECOMPENSAS", lista), BorderLayout.CENTER);
    }
}
