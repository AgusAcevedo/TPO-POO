package zonaliberada.models;

/** Reglas fijas compartidas por todos los personajes del juego. */
public final class ReglasJuego {
    // static final define valores de la clase compartidos por todo el juego.
    public static final int PA_POR_TURNO = 2;
    public static final int UMBRAL_INICIATIVA = 100;
    public static final int CANTIDAD_POSICIONES = 6;
    public static final int CANTIDAD_FRONTALES = 3;
    public static final int MINIMO_ALIADOS_ACTIVOS = 2;
    public static final int MAXIMO_ALIADOS_ACTIVOS = 4;

    // No hace falta crear objetos de esta clase para consultar sus constantes.
    private ReglasJuego() {}
}
