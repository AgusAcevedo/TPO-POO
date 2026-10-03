package zonaliberada.ui.personaje.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.Imagenes;
import zonaliberada.ui.PanelSeccion;

/** Retrato, nivel y barras del personaje seleccionado. */
public final class PanelFichaPersonaje extends PanelSeccion {
    public PanelFichaPersonaje(Ficha ficha) {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new BorderLayout(0, 8));
        JLabel figura = new JLabel(Imagenes.icono(ficha.imagen, 86, 129));
        figura.setHorizontalAlignment(SwingConstants.CENTER);
        contenido.add(figura, BorderLayout.NORTH);
        JPanel datos = Estilo.panel(new GridLayout(4, 1, 0, 8));
        datos.add(Estilo.etiqueta("Nivel " + ficha.nivel + " · " + ficha.rol, 15, Font.BOLD));
        datos.add(Estilo.barra("HP", ficha.vida, ficha.vidaMaxima, Estilo.ROJO));
        datos.add(Estilo.barra("MP", ficha.mana, ficha.manaMaximo, Estilo.AZUL));
        datos.add(Estilo.barra("XP", ficha.experiencia, ficha.experienciaSiguiente,
                Estilo.DORADO));
        contenido.add(datos, BorderLayout.CENTER);
        add(Estilo.tarjeta(ficha.nombre, contenido), BorderLayout.CENTER);
    }
}
