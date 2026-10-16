package zonaliberada.ui.zona.paneles;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.MapaDTO;
import zonaliberada.models.mapa.*;
import zonaliberada.ui.*;

/** Tarjetas ordenadas: iniciar delega el beneficio o el combate al backend. */
public final class PanelUbicaciones extends PanelSeccion {
    public PanelUbicaciones(MapaDTO.ZonaDTO zona, ContextoUI contexto) {
        super(new GridLayout(1, zona.getUbicaciones().size(), 12, 0));
        setPreferredSize(new Dimension(zona.getUbicaciones().size() * 225, 290));
        for (MapaDTO.UbicacionDTO ubicacion : zona.getUbicaciones()) add(tarjeta(ubicacion, contexto));
    }

    private JPanel tarjeta(MapaDTO.UbicacionDTO u, ContextoUI contexto) {
        boolean combate = u.getTipo() == TipoUbicacion.BATALLA
                || u.getTipo() == TipoUbicacion.BOSS_ZONA || u.getTipo() == TipoUbicacion.BOSS_FINAL;
        boolean entrenamiento = u.getEstado() == EstadoUbicacion.COMPLETADA && u.isRepetible();
        String detalle = TextosUI.legible(u.getTipo().name()) + "\n"
                + TextosUI.legible(u.getEstado().name()) + "\n\n"
                + u.getExperiencia() + " XP por participante";
        if (!entrenamiento) {
            detalle += "\n" + u.getItems().size() + " objeto(s) de recompensa";
            if (!u.getAliadosDesbloqueados().isEmpty()) detalle += "\nDesbloquea: "
                    + String.join(", ", u.getAliadosDesbloqueados());
        } else detalle += "\nSin repetir objetos ni desbloqueos.";
        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        contenido.add(Estilo.descripcion(detalle), BorderLayout.CENTER);
        // Una actividad disponible puede requerir completar la formación antes de combatir.
        boolean revisarParty = combate && u.getEstado() == EstadoUbicacion.DISPONIBLE && !u.isPuedeIniciarse();
        String texto = revisarParty ? "ARMAR PARTY" : entrenamiento ? "ENTRENAR"
                : u.isPuedeIniciarse() ? combate ? "COMBATIR" : "RECIBIR"
                : u.getEstado() == EstadoUbicacion.COMPLETADA ? "COMPLETADA" : "BLOQUEADA";
        JButton boton = Estilo.boton(texto, u.isPuedeIniciarse() ? Estilo.VERDE : Estilo.GRIS);
        boton.setName("ubicacion:" + u.getId());
        boton.setEnabled(u.isPuedeIniciarse() || revisarParty);
        boton.addActionListener(e -> {
            if (revisarParty) contexto.mostrar(PantallaId.PARTY);
            else contexto.iniciarUbicacion(u.getId());
        });
        contenido.add(boton, BorderLayout.SOUTH);
        return Estilo.tarjeta("<html>" + TextosUI.html(u.getNombre()) + "</html>", contenido);
    }
}
