package zonaliberada.ui.mapa;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.MapaDTO;
import zonaliberada.ui.*;
import zonaliberada.ui.mapa.paneles.PanelZonas;

/** Muestra todas las zonas que realmente tiene la partida. */
public final class PantallaMapaPrincipal extends PantallaBase {
    public PantallaMapaPrincipal(ContextoUI contexto) {
        super("SELECCIÓN DE ZONAS", "zonas.png", contexto);
        JPanel pie = Estilo.panel(new GridLayout(1, 3, 12, 0));
        pie.add(ir("PARTY E INVENTARIO", Estilo.VERDE, PantallaId.PARTY));
        pie.add(guardarPendiente("GUARDAR PARTIDA"));
        JButton menu = Estilo.boton("VOLVER AL MENÚ", Estilo.GRIS);
        menu.addActionListener(e -> contexto.volverAlMenu());
        pie.add(menu);
        add(pie, BorderLayout.SOUTH);
    }

    @Override public void actualizar() {
        MapaDTO mapa = contexto.juego().getMapaDTO();
        if (mapa == null) return;
        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 10));
        String estado = mapa.isPartidaCompletada() ? "CAMPAÑA COMPLETADA · Podés entrenar y explorar tu party."
                : "Completá las ubicaciones de una zona para desbloquear la siguiente.";
        cuerpo.add(Estilo.tarjeta(null, Estilo.descripcion(estado + "\n"
                + contexto.juego().getUltimoMensaje())), BorderLayout.NORTH);
        cuerpo.add(Estilo.desplazable(Estilo.centrarFila(
                new PanelZonas(mapa, contexto), 325)), BorderLayout.CENTER);
        reemplazarCentro(cuerpo);
    }
}
