package zonaliberada.ui.personaje.paneles;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.dtos.HabilidadDTO;
import zonaliberada.dtos.PersonajeDTO;
import zonaliberada.ui.*;

/** Muestra desbloqueo, costo, destinatarios y efectos; nunca aplica una habilidad. */
public final class PanelHabilidades extends PanelSeccion {
    public PanelHabilidades(PersonajeDTO ficha) {
        super(new GridLayout(1, Math.max(1, ficha.getHabilidades().size()), 12, 0));
        for (HabilidadDTO habilidad : ficha.getHabilidades()) {
            JPanel cuerpo = Estilo.panel(new BorderLayout(0, 6));
            JLabel estado = Estilo.etiqueta(habilidad.isDesbloqueada() ? "Disponible"
                    : "Bloqueada · nivel " + habilidad.getNivelDesbloqueo(), 13, Font.BOLD);
            estado.setForeground(habilidad.isDesbloqueada() ? Estilo.VERDE : Estilo.TEXTO_SECUNDARIO);
            cuerpo.add(estado, BorderLayout.NORTH);
            String destino = habilidad.isAfectaAliados() ? "aliados" : "enemigos";
            cuerpo.add(Estilo.descripcion(habilidad.getCostoPA() + " PA · " + habilidad.getCostoMana()
                    + " MP · " + (habilidad.isGrupal() ? "Todo el grupo de " : "Un objetivo de ")
                    + destino + "\n" + habilidad.getDescripcion()), BorderLayout.CENTER);
            add(Estilo.tarjeta(habilidad.getNombre(), cuerpo));
        }
    }
}
