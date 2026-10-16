package zonaliberada.ui.zona;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.MapaDTO;
import zonaliberada.ui.*;
import zonaliberada.ui.zona.paneles.PanelUbicaciones;

/** La misma vista sirve para cualquier zona y sus ubicaciones configuradas. */
public final class PantallaMapaZona extends PantallaBase {
    public PantallaMapaZona(ContextoUI contexto) {
        super("MAPA DE ZONA", "zona_moron.png", contexto);
        JPanel pie = Estilo.panel(new GridLayout(1, 3, 12, 0));
        pie.add(ir("PARTY E INVENTARIO", Estilo.VERDE, PantallaId.PARTY));
        pie.add(guardarPendiente("GUARDAR PARTIDA"));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
    }

    @Override public void actualizar() {
        MapaDTO mapa = contexto.juego().getMapaDTO();
        if (mapa == null) return;
        int indice = contexto.zonaSeleccionada();
        MapaDTO.ZonaDTO zona = mapa.getZonas().get(indice);
        titulo(zona.getNombre().toUpperCase(java.util.Locale.ROOT));
        cambiarFondo(Imagenes.fondoZona(indice));
        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 10));
        cuerpo.add(Estilo.tarjeta(null, Estilo.descripcion(
                "Las ubicaciones se habilitan en orden. Las batallas completadas permiten entrenar.\n"
                + "Al comenzar un combate, los aliados seleccionados recuperan toda su vida y maná.")),
                BorderLayout.NORTH);
        cuerpo.add(Estilo.desplazable(Estilo.centrarFila(new PanelUbicaciones(zona, contexto), 290)),
                BorderLayout.CENTER);
        reemplazarCentro(cuerpo);
    }
}
