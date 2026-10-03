package zonaliberada.ui.victoria.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.Imagenes;
import zonaliberada.ui.PanelSeccion;

/** XP actual de la party y recompensa de ejemplo de una batalla normal. */
public final class PanelExperiencia extends PanelSeccion {
    public PanelExperiencia() {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        contenido.add(Estilo.etiqueta("Recompensa de ejemplo: +5 XP por aliado", 13, Font.PLAIN),
                BorderLayout.NORTH);
        JPanel lista = Estilo.panel(new GridLayout(4, 1, 0, 10));
        for (Ficha ficha : DatosDemo.partyActiva()) {
            JPanel fila = Estilo.panel(new BorderLayout(8, 3));
            fila.add(new JLabel(Imagenes.icono(ficha.imagen, 42, 63)), BorderLayout.WEST);
            JPanel datos = Estilo.panel(new BorderLayout(0, 3));
            datos.add(Estilo.etiqueta(ficha.nombre + " · Nivel " + ficha.nivel + " · XP actual",
                    14, Font.BOLD), BorderLayout.NORTH);
            // La vista no simula subidas de nivel: eso será responsabilidad del modelo.
            datos.add(Estilo.barra("XP", ficha.experiencia, ficha.experienciaSiguiente,
                    Estilo.DORADO), BorderLayout.CENTER);
            fila.add(datos, BorderLayout.CENTER);
            lista.add(fila);
        }
        contenido.add(lista, BorderLayout.CENTER);
        add(Estilo.tarjeta("EXPERIENCIA", contenido), BorderLayout.CENTER);
    }
}
