package zonaliberada.ui.inventario.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Equipables disponibles y objetos de muestra usados por la party. */
public final class PanelEquipables extends PanelSeccion {
    public PanelEquipables() {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        JPanel disponibles = Estilo.panel(new GridLayout(0, 1, 0, 8));
        disponibles.add(Estilo.etiqueta("Disponibles para equipar", 14, Font.BOLD));
        for (String nombre : DatosDemo.EQUIPABLES_DISPONIBLES) {
            disponibles.add(Estilo.etiqueta(nombre + " · sin equipar", 14, Font.PLAIN));
        }
        contenido.add(disponibles, BorderLayout.NORTH);

        // Leemos las armas de las fichas activas para no repetir sus nombres aquí.
        JPanel equipados = Estilo.panel(new GridLayout(5, 1, 0, 8));
        equipados.add(Estilo.etiqueta("Equipados en la party", 14, Font.BOLD));
        for (Ficha ficha : DatosDemo.partyActiva()) {
            equipados.add(Estilo.etiqueta(ficha.arma + " · " + ficha.nombre, 14, Font.PLAIN));
        }
        contenido.add(equipados, BorderLayout.CENTER);

        JLabel nota = Estilo.etiqueta("Objetos y estados de ejemplo.", 13, Font.PLAIN);
        nota.setForeground(Estilo.TEXTO_SECUNDARIO);
        contenido.add(nota, BorderLayout.SOUTH);
        add(Estilo.tarjeta("EQUIPABLES DESBLOQUEADOS", contenido), BorderLayout.CENTER);
    }
}
