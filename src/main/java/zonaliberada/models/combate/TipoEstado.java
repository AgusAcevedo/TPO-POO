package zonaliberada.models.combate;

/** Identifica el efecto; EstadoAlterado guarda su magnitud y su duración para cada portador. */
public enum TipoEstado {
    VENENO,
    ATURDIDO,
    ESCUDO,
    RALENTIZADO,
    AUMENTO_ATAQUE,
    REDUCCION_ATAQUE,
    AUMENTO_DEFENSA,
    REDUCCION_DEFENSA;

    public boolean esNegativo() {
        return this == VENENO || this == ATURDIDO || this == RALENTIZADO
                || this == REDUCCION_ATAQUE || this == REDUCCION_DEFENSA;
    }
}
