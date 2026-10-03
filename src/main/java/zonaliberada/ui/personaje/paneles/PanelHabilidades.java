package zonaliberada.ui.personaje.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Habilidades disponibles y bloqueadas del personaje seleccionado. */
public final class PanelHabilidades extends PanelSeccion {
    public PanelHabilidades(Ficha ficha) {
        super(new BorderLayout());
        JPanel lista = Estilo.panel(new GridLayout(1, 2, 12, 0));
        lista.add(Estilo.etiqueta(ficha.habilidadPrincipal + " · disponible", 14, Font.BOLD));
        JLabel bloqueada = Estilo.etiqueta(ficha.habilidadSecundaria + " · bloqueada",
                14, Font.PLAIN);
        bloqueada.setForeground(Estilo.TEXTO_SECUNDARIO);
        lista.add(bloqueada);
        add(Estilo.tarjeta("HABILIDADES", lista), BorderLayout.CENTER);
    }
}
