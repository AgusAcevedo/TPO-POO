package zonaliberada.models.personajes;

public class Guerrero extends Aliado {
    private int furia;

    public Guerrero() {
    }

    public Guerrero(int furia) {
    }

    @Override
    public TipoClase tipoClase() { return TipoClase.GUERRERO; }

    @Override
    public double ataqueTotal() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
