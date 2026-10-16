package zonaliberada.ui.victoria.paneles;

import java.awt.*;
import java.util.List;
import javax.swing.JPanel;
import zonaliberada.dtos.*;
import zonaliberada.ui.*;

/** Solo los participantes capturados aparecen, incluso si terminaron KO. */
public final class PanelExperiencia extends PanelSeccion {
    public PanelExperiencia(ResultadoBatallaDTO resultado, List<PersonajeDTO> roster) {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        contenido.add(Estilo.etiqueta("+" + resultado.getExperienciaPorParticipante() + " XP por participante",
                15, Font.BOLD), BorderLayout.NORTH);
        JPanel lista = Estilo.panel(new GridLayout(0, 1, 0, 12));
        for (ResultadoBatallaDTO.ProgresoAliadoDTO progreso : resultado.getAliados()) {
            PersonajeDTO ficha = PersonajeDTO.buscarPorId(roster, progreso.getId());
            if (ficha == null) continue;
            JPanel fila = Estilo.panel(new BorderLayout(10, 6));
            fila.add(Imagenes.personaje(ficha, 42, 63), BorderLayout.WEST);
            JPanel datos = Estilo.panel(new BorderLayout(0, 6));
            String nivel = progreso.getNivelAnterior() == progreso.getNivelActual()
                    ? "Nivel " + progreso.getNivelActual() : "Nivel " + progreso.getNivelAnterior()
                        + " → " + progreso.getNivelActual();
            datos.add(Estilo.etiqueta(progreso.getNombre() + " · " + nivel, 14, Font.BOLD), BorderLayout.NORTH);
            datos.add(Estilo.experiencia(ficha), BorderLayout.CENTER);
            fila.add(datos, BorderLayout.CENTER);
            lista.add(Estilo.tarjeta(null, fila));
        }
        contenido.add(Estilo.desplazableVertical(lista), BorderLayout.CENTER);
        add(Estilo.tarjeta("EXPERIENCIA", contenido));
    }
}
