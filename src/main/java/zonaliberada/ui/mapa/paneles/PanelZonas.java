package zonaliberada.ui.mapa.paneles;

import java.awt.*;
import javax.swing.*;
import zonaliberada.dtos.MapaDTO;
import zonaliberada.models.mapa.EstadoUbicacion;
import zonaliberada.ui.*;

/** Genera las tarjetas de zonas a partir del desbloqueo y el progreso del mapa. */
public final class PanelZonas extends PanelSeccion {
    public PanelZonas(MapaDTO mapa, ContextoUI contexto) {
        super(new GridLayout(1, mapa.getZonas().size(), 14, 0));
        setPreferredSize(new Dimension(mapa.getZonas().size() * 300, 325));
        for (int i = 0; i < mapa.getZonas().size(); i++) add(zona(mapa.getZonas().get(i), i, contexto));
    }

    private JPanel zona(MapaDTO.ZonaDTO zona, int indice, ContextoUI contexto) {
        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        JLabel paisaje = new JLabel(Imagenes.icono(
                "/imagenes/fondos/" + Imagenes.fondoZona(indice), 240, 130), SwingConstants.CENTER);
        contenido.add(paisaje, BorderLayout.NORTH);
        int completas = 0;
        for (MapaDTO.UbicacionDTO ubicacion : zona.getUbicaciones()) {
            if (ubicacion.getEstado() == EstadoUbicacion.COMPLETADA) completas++;
        }
        JLabel descripcion = Estilo.etiqueta("<html><center>" + completas + "/"
                + zona.getUbicaciones().size() + " ubicaciones completadas<br><br>"
                + (zona.isCompletada() ? "Zona completada" : zona.isDesbloqueada()
                    ? "Zona disponible" : "Completá la zona anterior") + "</center></html>", 14, Font.PLAIN);
        descripcion.setHorizontalAlignment(SwingConstants.CENTER);
        contenido.add(descripcion, BorderLayout.CENTER);
        JButton boton = Estilo.boton(zona.isDesbloqueada() ? "INGRESAR" : "BLOQUEADA",
                zona.isDesbloqueada() ? Estilo.VERDE : Estilo.GRIS);
        boton.setName("zona:" + indice);
        boton.setEnabled(zona.isDesbloqueada());
        boton.addActionListener(e -> contexto.verZona(indice));
        contenido.add(boton, BorderLayout.SOUTH);
        return Estilo.tarjeta(zona.getNombre(), contenido);
    }
}
