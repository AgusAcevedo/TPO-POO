package zonaliberada.ui.victoria;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.*;
import zonaliberada.ui.*;
import zonaliberada.ui.victoria.paneles.*;

/** Informa un resultado ya procesado; abrirla no entrega la recompensa otra vez. */
public final class PantallaVictoria extends PantallaBase {
    private final JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));

    public PantallaVictoria(ContextoUI contexto) {
        super("VICTORIA", "victoria.png", contexto);
        add(pie, BorderLayout.SOUTH);
    }

    @Override public void actualizar() {
        ResultadoBatallaDTO resultado = contexto.juego().getUltimoResultado();
        if (resultado == null) return;
        titulo(resultado.isEntrenamiento() ? "ENTRENAMIENTO GANADO" : resultado.isPartidaCompletada()
                ? "CAMPAÑA COMPLETADA" : "VICTORIA");
        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 12));
        JPanel columnas = Estilo.panel(new GridLayout(1, 2, 14, 0));
        columnas.add(new PanelExperiencia(resultado, contexto.juego().getPartyDTO()));
        columnas.add(new PanelRecompensas(resultado, contexto.juego().getPartyDTO()));
        cuerpo.add(columnas, BorderLayout.CENTER);
        JLabel guardado = Estilo.etiqueta(resultado.getMensaje(), 13, Font.BOLD);
        guardado.setHorizontalAlignment(SwingConstants.CENTER);
        guardado.setForeground(Estilo.VERDE);
        cuerpo.add(Estilo.tarjeta(null, guardado), BorderLayout.SOUTH);
        reemplazarCentro(cuerpo);

        // Las opciones cambian según sea entrenamiento, victoria de zona o final de campaña.
        pie.removeAll();
        MapaDTO.UbicacionDTO ubicacion = buscarUbicacion(resultado.getUbicacionId());
        if (ubicacion != null && ubicacion.isRepetible()) {
            JButton repetir = Estilo.boton("ENTRENAR OTRA VEZ", Estilo.GRIS);
            repetir.addActionListener(e -> contexto.iniciarUbicacion(resultado.getUbicacionId()));
            pie.add(repetir);
        }
        JButton continuar = Estilo.boton(resultado.isPartidaCompletada() ? "EXPLORAR MAPA" : "CONTINUAR AL MAPA",
                Estilo.VERDE);
        continuar.addActionListener(e -> {
            if (resultado.isPartidaCompletada()) mostrar(PantallaId.MAPA_PRINCIPAL);
            else if (resultado.isEntrenamiento()) contexto.verZona(contexto.zonaSeleccionada());
            else contexto.verZonaActual();
        });
        pie.add(continuar);
        pie.revalidate();
        pie.repaint();
    }

    private MapaDTO.UbicacionDTO buscarUbicacion(String id) {
        for (MapaDTO.ZonaDTO zona : contexto.juego().getMapaDTO().getZonas()) {
            for (MapaDTO.UbicacionDTO ubicacion : zona.getUbicaciones()) {
                if (ubicacion.getId().equals(id)) return ubicacion;
            }
        }
        return null;
    }
}
