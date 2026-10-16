package zonaliberada.ui.victoria.paneles;

import java.awt.*;
import java.util.List;
import javax.swing.JPanel;
import zonaliberada.dtos.*;
import zonaliberada.ui.*;

/** Objetos y desbloqueos que el controlador ya agregó al estado de partida. */
public final class PanelRecompensas extends PanelSeccion {
    public PanelRecompensas(ResultadoBatallaDTO resultado, List<PersonajeDTO> roster) {
        super(new BorderLayout());
        JPanel lista = Estilo.panel(new GridLayout(0, 1, 0, 12));
        if (resultado.getItems().isEmpty()) lista.add(Estilo.descripcion(resultado.isEntrenamiento()
                ? "Entrenamiento: se entrega XP, sin repetir objetos ni desbloqueos."
                : "Este encuentro no entrega objetos."));
        for (ItemDTO item : resultado.getItems()) {
            lista.add(Estilo.etiqueta(item.getNombre() + " × " + item.getCantidad(), 17, Font.BOLD));
        }
        for (String id : resultado.getDesbloqueados()) {
            PersonajeDTO aliado = PersonajeDTO.buscarPorId(roster, "aliado:" + id);
            String nombre = aliado == null ? TextosUI.legible(id) : aliado.getNombre();
            lista.add(Estilo.etiqueta("ALIADO DESBLOQUEADO: " + nombre, 14, Font.BOLD));
        }
        lista.add(Estilo.descripcion("Los objetos ya están en el inventario. No se duplican al volver a esta pantalla."));
        if (resultado.isPartidaCompletada() && !resultado.isEntrenamiento()) {
            lista.add(Estilo.descripcion("Derrotaste al antagonista final. El recorrido está completo y podés seguir entrenando."));
        }
        add(Estilo.tarjeta("RECOMPENSAS", Estilo.desplazableVertical(lista)));
    }
}
