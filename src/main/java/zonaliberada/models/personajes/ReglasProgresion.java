package zonaliberada.models.personajes;

/** Balance provisional aprobado: cambiar aquí sin tocar los controladores. */
public final class ReglasProgresion {
    public static final int NIVEL_MAXIMO = 10;
    public static final int NIVEL_SEGUNDA_HABILIDAD = 5;
    private ReglasProgresion() {}

    public static int experienciaParaSubir(int nivel) {
        if (nivel < 1 || nivel > NIVEL_MAXIMO) {
            throw new IllegalArgumentException("Nivel fuera de rango");
        }
        return nivel == NIVEL_MAXIMO ? 0 : 100 * nivel;
    }

    public static void aplicarMejora(Aliado aliado) {
        // Las mejoras están en orden: vida, maná, ataque, defensa y velocidad.
        switch (aliado.tipoClase()) {
            case GUERRERO:
                aliado.aumentarEstadisticas(20, 5, 4, 3, 1);
                break;
            case MAGO:
                aliado.aumentarEstadisticas(10, 12, 5, 1, 1);
                break;
            case ARQUERO:
                aliado.aumentarEstadisticas(12, 7, 3, 2, 2);
                break;
            case CURADOR:
                aliado.aumentarEstadisticas(15, 10, 2, 2, 1);
                break;
        }
    }
}
