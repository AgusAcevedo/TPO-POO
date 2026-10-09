package zonaliberada.models.combate.acciones;

import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.inventario.Consumible;
import zonaliberada.models.personajes.Entidad;

public class ContextoAccion {
    private Entidad origen;
    private Entidad[] objetivos;
    private Consumible item;
    private Habilidad habilidad;

    public ContextoAccion() {
    }

    public ContextoAccion(Entidad origen, Entidad[] objetivos, Consumible item, Habilidad habilidad) {
    }
}
