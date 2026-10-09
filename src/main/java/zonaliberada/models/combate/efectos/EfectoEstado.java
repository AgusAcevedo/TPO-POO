package zonaliberada.models.combate.efectos;

import zonaliberada.models.combate.TipoEstado;
import zonaliberada.models.personajes.Entidad;

public class EfectoEstado extends EfectoCombate {
    private TipoEstado tipo;
    private int duracion;
    private int magnitud;
    private double probabilidad;

    public EfectoEstado() {
    }

    public EfectoEstado(TipoEstado tipo, int duracion, int magnitud, double probabilidad) {
    }

    @Override
    public void aplicar(Entidad usuario, Entidad objetivo) {
    }
}
