package zonaliberada.ui.inicio;

import zonaliberada.ui.ContextoUI;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.inicio.paneles.PanelMenuInicio;

/** Menú sin partida implícita: el jugador crea una sesión o continúa la actual. */
public final class PantallaInicio extends PantallaBase {
    public PantallaInicio(ContextoUI contexto) { super(null, "inicio.png", contexto); }

    @Override public void actualizar() {
        reemplazarCentro(new PanelMenuInicio(contexto));
    }
}
