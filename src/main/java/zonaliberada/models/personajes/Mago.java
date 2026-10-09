package zonaliberada.models.personajes;

public class Mago extends Aliado {
    private int concentracion;

    public Mago() {
    }

    public Mago(int concentracion) {
    }

    @Override
    public TipoClase tipoClase() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public double ataqueTotal() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
