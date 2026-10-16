package zonaliberada.models.combate;

/** Etapas del encuentro: permiten rechazar acciones antes del inicio o después del final. */
public enum EstadoBatalla {
    NO_INICIADA,
    EN_CURSO,
    VICTORIA,
    DERROTA
}
