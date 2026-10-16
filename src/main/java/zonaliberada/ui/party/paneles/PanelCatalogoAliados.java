package zonaliberada.ui.party.paneles;

import java.awt.*;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import zonaliberada.dtos.*;
import zonaliberada.ui.*;

/** El roster no tiene un máximo fijo; los pendientes se leen del mapa. */
public final class PanelCatalogoAliados extends PanelSeccion {
    public PanelCatalogoAliados(List<PersonajeDTO> aliados, MapaDTO mapa, ContextoUI contexto) {
        super(new GridLayout(1, 0, 8, 0));
        Map<String, String> pendientes = new LinkedHashMap<>();
        // El mapa indica dónde se desbloquea cada aliado; quitamos los que ya están en el roster.
        for (MapaDTO.ZonaDTO zona : mapa.getZonas()) for (MapaDTO.UbicacionDTO ubicacion : zona.getUbicaciones()) {
            for (String id : ubicacion.getAliadosDesbloqueados()) pendientes.putIfAbsent(id, ubicacion.getNombre());
        }
        for (PersonajeDTO a : aliados) {
            pendientes.remove(a.getId().substring(7));
            JButton boton = Estilo.boton("<html><center>" + TextosUI.html(a.getNombre())
                    + "<br>Nivel " + a.getNivel() + "<br>"
                    + (a.getPosicion() == 0 ? "Reserva" : "Posición " + a.getPosicion()) + "</center></html>", Estilo.VERDE);
            boton.setName("catalogo:" + a.getId());
            boton.setIcon(Imagenes.personaje(a, 38, 57).getIcon());
            boton.setHorizontalTextPosition(SwingConstants.CENTER);
            boton.setVerticalTextPosition(SwingConstants.BOTTOM);
            boton.addActionListener(e -> contexto.verPersonaje(a.getId()));
            add(boton);
        }
        for (Map.Entry<String, String> pendiente : pendientes.entrySet()) {
            JPanel contenido = Estilo.panel(new BorderLayout(0, 8));
            contenido.add(Estilo.etiqueta("BLOQUEADO", 13, Font.BOLD), BorderLayout.NORTH);
            contenido.add(Estilo.descripcion("Se desbloquea en:\n" + pendiente.getValue()), BorderLayout.CENTER);
            add(Estilo.tarjeta(TextosUI.legible(pendiente.getKey()), contenido));
        }
        setPreferredSize(new Dimension(Math.max(1, aliados.size() + pendientes.size()) * 165, 165));
    }
}
