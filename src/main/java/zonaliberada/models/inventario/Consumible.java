package zonaliberada.models.inventario;

import java.util.List;
import zonaliberada.models.combate.efectos.EfectoCombate;
import zonaliberada.models.personajes.Entidad;

public class Consumible extends Item {
    private boolean afectaAliados;
    private boolean esGrupal;
    private List<EfectoCombate> efectos;

    public Consumible() {
    }

    public Consumible(boolean afectaAliados, boolean esGrupal, List<EfectoCombate> efectos) {
    }

    public boolean puedeUsarse(Entidad usuario, Entidad objetivo) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void aplicarEfectos(Entidad usuario, Entidad objetivo) {
    }
}
