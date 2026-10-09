package zonaliberada.models.personajes;

import zonaliberada.models.combate.Batalla;
import zonaliberada.models.combate.acciones.Accion;

public class Boss extends Enemigo {
    private boolean esFinal;
    private int faseActual;

    public Boss() {
    }

    public Boss(boolean esFinal, int faseActual) {
    }

    @Override
    public Accion elegirAccion(Batalla batalla) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public Entidad[] elegirObjetivos(Accion accion, Batalla batalla) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
