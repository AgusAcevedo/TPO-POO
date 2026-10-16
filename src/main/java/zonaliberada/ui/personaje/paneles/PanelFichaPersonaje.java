package zonaliberada.ui.personaje.paneles;

import java.awt.*;
import javax.swing.*;
import zonaliberada.dtos.PersonajeDTO;
import zonaliberada.ui.*;

/** Retrato y recursos efectivos, incluyendo el equipamiento. */
public final class PanelFichaPersonaje extends PanelSeccion {
    public PanelFichaPersonaje(PersonajeDTO ficha) {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        contenido.add(Imagenes.personaje(ficha, 86, 129), BorderLayout.NORTH);
        JPanel datos = Estilo.panel(new GridLayout(5, 1, 0, 8));
        datos.add(Estilo.etiqueta("Nivel " + ficha.getNivel() + " · " + TextosUI.legible(ficha.getRol()), 15, Font.BOLD));
        datos.add(Estilo.etiqueta(ficha.getPosicion() == 0 ? "En reserva" : "Posición " + ficha.getPosicion(), 14, Font.PLAIN));
        datos.add(Estilo.barra("HP", ficha.getVida(), ficha.getVidaMaxima(), Estilo.ROJO));
        datos.add(Estilo.barra("MP", ficha.getMana(), ficha.getManaMaximo(), Estilo.AZUL));
        datos.add(Estilo.experiencia(ficha));
        contenido.add(datos, BorderLayout.CENTER);
        add(Estilo.tarjeta(ficha.getNombre(), contenido));
    }
}
