package zonaliberada.ui.party.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.Imagenes;
import zonaliberada.ui.PanelSeccion;

/** Catálogo de siete aliados; los bloqueados no ofrecen ninguna acción. */
public final class PanelCatalogoAliados extends PanelSeccion {
    public PanelCatalogoAliados(Consumer<Ficha> verDetalle) {
        super(new GridLayout(1, 7, 7, 0));
        // Los bloqueados son paneles, no botones deshabilitados, para conservar
        // el contraste en macOS.
        for (Ficha ficha : DatosDemo.ALIADOS) {
            if (!ficha.desbloqueado) {
                JPanel bloqueado = Estilo.panel(new BorderLayout(0, 6));
                bloqueado.setOpaque(true);
                bloqueado.setBackground(Estilo.GRIS);
                bloqueado.setBorder(BorderFactory.createLineBorder(Estilo.TINTA, 2));
                JLabel figura = new JLabel(Imagenes.icono(ficha.imagen, 56, 84));
                figura.setHorizontalAlignment(SwingConstants.CENTER);
                bloqueado.add(figura, BorderLayout.CENTER);
                JLabel texto = Estilo.etiqueta("<html><center>" + ficha.nombre
                        + "<br>Bloqueado</center></html>", 14, Font.BOLD);
                texto.setHorizontalAlignment(SwingConstants.CENTER);
                bloqueado.add(texto, BorderLayout.SOUTH);
                add(bloqueado);
                continue;
            }
            JButton personaje = Estilo.boton(
                    "<html><center>" + ficha.nombre + "<br>Disponible</center></html>",
                    Estilo.VERDE);
            personaje.setIcon(Imagenes.icono(ficha.imagen, 56, 84));
            personaje.setHorizontalTextPosition(SwingConstants.CENTER);
            personaje.setVerticalTextPosition(SwingConstants.BOTTOM);
            personaje.setIconTextGap(8);
            personaje.addActionListener(evento -> verDetalle.accept(ficha));
            add(personaje);
        }
    }
}
