package zonaliberada.controllers;

import zonaliberada.models.combate.Batalla;
import zonaliberada.models.combate.EstadoBatalla;
import zonaliberada.models.combate.Recompensa;
import zonaliberada.models.inventario.Inventario;
import zonaliberada.models.mapa.UbicacionMapa;

public class ControladorBatalla {
    private Batalla batalla;
    private ControladorTurno controladorTurno;

    public ControladorBatalla() {
    }

    public ControladorBatalla(Batalla batalla, ControladorTurno controladorTurno) {
    }

    public boolean iniciarBatalla(Batalla batalla, Inventario inventario) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void siguienteTurno() {
    }

    public Recompensa finalizarBatalla() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void descartarBatalla() {
    }

    public EstadoBatalla estadoActual() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public UbicacionMapa ubicacionActual() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
