package zonaliberada.models.personajes;

public class Curador extends Aliado {
    public Curador() {
    }

    @Override
    public TipoClase tipoClase() {
        return TipoClase.CURADOR;
    }
}
