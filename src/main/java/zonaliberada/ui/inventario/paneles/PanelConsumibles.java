package zonaliberada.ui.inventario.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Consumibles guardados en el inventario; aquí no se pueden utilizar. */
public final class PanelConsumibles extends PanelSeccion {
    public PanelConsumibles() {
        super(new BorderLayout());
        // Las cantidades son ejemplos; no existe todavía una acción de usar.
        JPanel lista = Estilo.panel(new GridLayout(4, 1, 0, 12));
        lista.add(Estilo.etiqueta("Mate · " + DatosDemo.CANTIDAD_MATES + " unidades",
                16, Font.BOLD));
        lista.add(Estilo.etiqueta("Recupera maná", 14, Font.PLAIN));
        lista.add(Estilo.etiqueta("Fernet · " + DatosDemo.CANTIDAD_FERNETS + " unidades",
                16, Font.BOLD));
        lista.add(Estilo.etiqueta("Mejora ataque y reduce defensa", 14, Font.PLAIN));

        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        contenido.add(lista, BorderLayout.CENTER);
        JLabel nota = Estilo.etiqueta("Solo se usan durante el combate.", 13, Font.PLAIN);
        nota.setForeground(Estilo.TEXTO_SECUNDARIO);
        contenido.add(nota, BorderLayout.SOUTH);
        add(Estilo.tarjeta("CONSUMIBLES", contenido), BorderLayout.CENTER);
    }
}
