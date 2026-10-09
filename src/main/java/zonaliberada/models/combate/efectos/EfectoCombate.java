package zonaliberada.models.combate.efectos;

import zonaliberada.models.personajes.Entidad;

public abstract class EfectoCombate {
    protected EfectoCombate() {
    }

    public abstract void aplicar(Entidad usuario, Entidad objetivo);
}
