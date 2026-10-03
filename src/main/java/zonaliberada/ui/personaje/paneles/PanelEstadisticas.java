package zonaliberada.ui.personaje.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Estadísticas de muestra; no calcula daño ni turnos. */
public final class PanelEstadisticas extends PanelSeccion {
    public PanelEstadisticas(Ficha ficha) {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new GridLayout(5, 1, 0, 12));
        contenido.add(Estilo.etiqueta("Ataque: " + ficha.ataque, 15, Font.BOLD));
        contenido.add(Estilo.etiqueta("Defensa: " + ficha.defensa, 15, Font.BOLD));
        contenido.add(Estilo.etiqueta("Velocidad: " + ficha.velocidad, 15, Font.BOLD));
        contenido.add(Estilo.etiqueta("PA máximos: " + DatosDemo.PUNTOS_ACCION_MAXIMOS,
                15, Font.BOLD));
        JLabel nota = Estilo.etiqueta("Valores de ejemplo", 13, Font.PLAIN);
        nota.setForeground(Estilo.TEXTO_SECUNDARIO);
        contenido.add(nota);
        add(Estilo.tarjeta("ESTADÍSTICAS", contenido), BorderLayout.CENTER);
    }
}
