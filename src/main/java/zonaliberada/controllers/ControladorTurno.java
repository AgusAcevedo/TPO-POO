package zonaliberada.controllers;

import zonaliberada.models.combate.Batalla;
import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.combate.acciones.Accion;
import zonaliberada.models.combate.acciones.ContextoAccion;
import zonaliberada.models.inventario.Consumible;
import zonaliberada.models.inventario.Inventario;
import zonaliberada.models.personajes.Enemigo;
import zonaliberada.models.personajes.Entidad;

public class ControladorTurno {
    private Batalla batalla;
    private Inventario inventario;

    public ControladorTurno() {
    }

    public ControladorTurno(Batalla batalla, Inventario inventario) {
    }

    public void iniciarTurno() {
    }

    public boolean ejecutarAccion(Accion accion, ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void finalizarTurno() {
    }

    private ContextoAccion crearContexto(Entidad[] objetivos, Habilidad habilidad, Consumible item) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    private void ejecutarTurnoEnemigo(Enemigo enemigo) {
    }
}
