package zonaliberada.models.personajes;

public class Arquero extends Aliado {
    private int presicion;

    public Arquero() {
    }

    public Arquero(int presicion) {
    }

    @Override
    public TipoClase tipoClase() {
        return TipoClase.ARQUERO;
    }

    @Override
    public double ataqueTotal() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
