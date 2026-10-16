package zonaliberada.dtos;
import java.util.List;
import zonaliberada.models.combate.EstadoBatalla;
/** Foto del encuentro para dibujarlo; el actor actual puede ser aliado o enemigo. */
public final class BatallaDTO {
    private final EstadoBatalla estado;
    private final String ubicacionId;
    private final String ubicacionNombre;
    private final String actorActualId;
    private final List<PersonajeDTO> aliados;
    private final List<PersonajeDTO> enemigos;
    private final String mensaje;

    public BatallaDTO(
            EstadoBatalla estado,
            String ubicacionId,
            String ubicacionNombre,
            String actorActualId,
            List<PersonajeDTO> aliados,
            List<PersonajeDTO> enemigos, String mensaje) {
        aliados = List.copyOf(aliados);
        enemigos = List.copyOf(enemigos);
        this.estado = estado;
        this.ubicacionId = ubicacionId;
        this.ubicacionNombre = ubicacionNombre;
        this.actorActualId = actorActualId;
        this.aliados = aliados;
        this.enemigos = enemigos;
        this.mensaje = mensaje;
    }

    public EstadoBatalla getEstado() { return estado; }
    public String getUbicacionId() { return ubicacionId; }
    public String getUbicacionNombre() { return ubicacionNombre; }
    public String getActorActualId() { return actorActualId; }
    public List<PersonajeDTO> getAliados() { return aliados; }
    public List<PersonajeDTO> getEnemigos() { return enemigos; }
    public String getMensaje() { return mensaje; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof BatallaDTO)) return false;
        BatallaDTO otro = (BatallaDTO) objeto;
        return java.util.Objects.equals(estado, otro.estado)
                && java.util.Objects.equals(ubicacionId, otro.ubicacionId)
                && java.util.Objects.equals(ubicacionNombre, otro.ubicacionNombre)
                && java.util.Objects.equals(actorActualId, otro.actorActualId)
                && java.util.Objects.equals(aliados, otro.aliados)
                && java.util.Objects.equals(enemigos, otro.enemigos)
                && java.util.Objects.equals(mensaje, otro.mensaje);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                estado, ubicacionId, ubicacionNombre, actorActualId, aliados, enemigos, mensaje);
    }

    @Override
    public String toString() {
        return "BatallaDTO["
                + "estado=" + estado
                + ", ubicacionId=" + ubicacionId
                + ", ubicacionNombre=" + ubicacionNombre
                + ", actorActualId=" + actorActualId
                + ", aliados=" + aliados
                + ", enemigos=" + enemigos + ", mensaje=" + mensaje + "]";
    }

    public PersonajeDTO aliadoActual() {
        // Devuelve null si todavía no hay actor o si está actuando un enemigo.
        return PersonajeDTO.buscarPorId(aliados, actorActualId);
    }
}
