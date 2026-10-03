package zonaliberada.ui.victoria;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.victoria.paneles.PanelExperiencia;
import zonaliberada.ui.victoria.paneles.PanelRecompensas;

/** Resultado de ejemplo de una batalla normal, sin aplicar recompensas reales. */
public final class PantallaVictoria extends PantallaBase {
    public PantallaVictoria(Consumer<PantallaId> navegar) {
        super("VICTORIA", "victoria.png", navegar);

        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 12));
        JPanel columnas = Estilo.panel(new GridLayout(1, 2, 14, 0));
        columnas.add(new PanelExperiencia());
        columnas.add(new PanelRecompensas());
        cuerpo.add(columnas, BorderLayout.CENTER);
        // No afirmamos que se guardó una partida: todavía no existe persistencia.
        JLabel guardado = Estilo.etiqueta("Autoguardado pendiente de implementación",
                13, Font.PLAIN);
        guardado.setHorizontalAlignment(SwingConstants.CENTER);
        cuerpo.add(guardado, BorderLayout.SOUTH);
        add(cuerpo, BorderLayout.CENTER);

        JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));
        // Esta maqueta representa una batalla normal. En bosses, el controlador
        // deberá ocultar la opción Repetir porque el documento no lo permite.
        pie.add(ir("REPETIR BATALLA", Estilo.GRIS, PantallaId.BATALLA));
        pie.add(ir("CONTINUAR AL MAPA", Estilo.VERDE, PantallaId.MAPA_ZONA));
        add(pie, BorderLayout.SOUTH);
    }
}
