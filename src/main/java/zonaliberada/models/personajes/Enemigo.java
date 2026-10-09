package zonaliberada.models.personajes;

import zonaliberada.models.combate.Batalla;
import zonaliberada.models.combate.acciones.Accion;

public class Enemigo extends Entidad {
    private int experienciaOtorgada;

    public Enemigo() {
    }

    public Enemigo(int experienciaOtorgada) {
    }

    public Accion elegirAccion(Batalla batalla) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad[] elegirObjetivos(Accion accion, Batalla batalla) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
