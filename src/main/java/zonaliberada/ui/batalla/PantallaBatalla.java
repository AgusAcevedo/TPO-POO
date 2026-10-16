package zonaliberada.ui.batalla;

import java.awt.*;
import javax.swing.JPanel;
import zonaliberada.dtos.BatallaDTO;
import zonaliberada.ui.*;
import zonaliberada.ui.batalla.paneles.*;

/** Refresca datos; conserva la cabecera y renueva los controles de cada solicitud. */
public final class PantallaBatalla extends PantallaBase {
    private final JPanel cuerpo = Estilo.panel(new BorderLayout(0, 8));
    private final PanelEstadoTurno estadoTurno = new PanelEstadoTurno();

    public PantallaBatalla(ContextoUI contexto) {
        super("BATALLA", "combate.png", contexto);
        cuerpo.add(estadoTurno, BorderLayout.NORTH);
        reemplazarCentro(cuerpo);
    }

    @Override public void actualizar() {
        BatallaDTO batalla = contexto.juego().getBatallaDTO();
        titulo(batalla.getUbicacionNombre().isEmpty() ? "BATALLA" : batalla.getUbicacionNombre());
        estadoTurno.actualizar(batalla);
        Component tablero = ((BorderLayout) cuerpo.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (tablero != null) cuerpo.remove(tablero);
        cuerpo.add(Estilo.desplazable(new PanelBandos(batalla)), BorderLayout.CENTER);

        Component acciones = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        if (acciones != null) remove(acciones);
        // Se renueva el panel de acciones para invalidar clics de la solicitud anterior.
        add(new PanelAcciones(contexto, batalla), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
