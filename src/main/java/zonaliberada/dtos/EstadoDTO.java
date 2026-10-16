package zonaliberada.dtos;
import zonaliberada.models.combate.TipoEstado;
/** Datos visibles de un estado; la duración real se administra en EstadoAlterado. */
public final class EstadoDTO {
    private final TipoEstado tipo;
    private final int turnosRestantes;

    public EstadoDTO(
            TipoEstado tipo,
            int turnosRestantes) {
        this.tipo = tipo;
        this.turnosRestantes = turnosRestantes;
    }

    public TipoEstado getTipo() { return tipo; }
    public int getTurnosRestantes() { return turnosRestantes; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof EstadoDTO)) return false;
        EstadoDTO otro = (EstadoDTO) objeto;
        return java.util.Objects.equals(tipo, otro.tipo)
                && turnosRestantes == otro.turnosRestantes;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(tipo, turnosRestantes);
    }

    @Override
    public String toString() {
        return "EstadoDTO["
                + "tipo=" + tipo
                + ", turnosRestantes=" + turnosRestantes + "]";
    }

}
