package zonaliberada.ui.personaje.paneles;

import java.awt.*;
import javax.swing.JPanel;
import zonaliberada.dtos.PersonajeDTO;
import zonaliberada.ui.*;

/** Consulta valores del DTO, no vuelve a sumar bonus del equipo. */
public final class PanelEstadisticas extends PanelSeccion {
    public PanelEstadisticas(PersonajeDTO ficha) {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new GridLayout(0, 1, 0, 10));
        contenido.add(Estilo.etiqueta("Ataque: " + TextosUI.numero(ficha.getAtaque()), 15, Font.BOLD));
        contenido.add(Estilo.etiqueta("Defensa: " + TextosUI.numero(ficha.getDefensa()), 15, Font.BOLD));
        contenido.add(Estilo.etiqueta("Velocidad: " + ficha.getVelocidad(), 15, Font.BOLD));
        contenido.add(Estilo.etiqueta("PA por turno: " + ficha.getPuntosAccionMaximos(), 15, Font.BOLD));
        contenido.add(Estilo.etiqueta("Ataque: " + (ficha.isADistancia() ? "a distancia" : "cuerpo a cuerpo"), 14, Font.PLAIN));
        if (!ficha.getAtributoEspecial().isEmpty()) {
            contenido.add(Estilo.etiqueta(ficha.getAtributoEspecial() + ": " + ficha.getValorEspecial(), 15, Font.BOLD));
        }
        contenido.add(Estilo.descripcion("Estadísticas efectivas. La vida y el maná se recuperan al comenzar el próximo combate."));
        add(Estilo.tarjeta("ESTADÍSTICAS", contenido));
    }
}
