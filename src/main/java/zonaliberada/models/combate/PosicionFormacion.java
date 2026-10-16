package zonaliberada.models.combate;

import zonaliberada.models.personajes.Entidad;

/** Una casilla conserva su número y un ocupante opcional; vaciarla no elimina al personaje. */
public class PosicionFormacion {

    private final int numero;
    private Entidad ocupante;

    public PosicionFormacion(int numero) {
        if (numero < 1) {
            throw new IllegalArgumentException("El número de posición debe ser mayor o igual a 1");
        }

        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public Entidad getOcupante() {
        return ocupante;
    }

    public boolean estaLibre() {
        return ocupante == null;
    }

    public void asignar(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser null; para quitarla usá vaciar()");
        }

        // No reemplazamos accidentalmente a quien ocupa la posición.
        if (!estaLibre()) {
            throw new IllegalStateException("La posición ya está ocupada");
        }

        ocupante = entidad;
    }

    public void vaciar() {
        ocupante = null;
    }
}
