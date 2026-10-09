package zonaliberada.models.combate;

import zonaliberada.models.personajes.Entidad;

public class PosicionFormacion {
    private int numero;
    private Entidad ocupante;

    public PosicionFormacion() {
    }

    public PosicionFormacion(int numero, Entidad ocupante) {
    }

    public void asignar(Entidad entidad) {
    }

    public void vaciar() {
    }

    public boolean estaLibre() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
