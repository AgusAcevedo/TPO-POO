package zonaliberada.models.combate;

import zonaliberada.models.personajes.Entidad;

public class EstadoAlterado {
    private TipoEstado tipo;
    private int turnosRestantes;
    private int magnitud;

    public EstadoAlterado() {
    }

    public EstadoAlterado(TipoEstado tipo, int turnosRestantes, int magnitud) {
    }

    public void alInicioTurno(Entidad entidad) {
    }

    public void alFinTurno(Entidad entidad) {
    }

    public int modificarAtaque(int valor) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public int modificarDefensa(int valor) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public int modificarVelocidad(int valor) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean impideActuar() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean finalizo() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
