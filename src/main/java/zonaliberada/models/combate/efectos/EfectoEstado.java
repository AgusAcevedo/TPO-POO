package zonaliberada.models.combate.efectos;

import zonaliberada.models.combate.EstadoAlterado;
import zonaliberada.models.combate.TipoEstado;
import zonaliberada.models.personajes.Entidad;

/** Intenta aplicar un estado según su probabilidad, sin compartir el contador entre objetivos. */
public class EfectoEstado extends EfectoCombate {

    private final EstadoAlterado estado;
    private final double probabilidad;

    public EfectoEstado(
            TipoEstado tipo,
            int duracion,
            int magnitud,
            double probabilidad) {

        // NaN e infinitos tampoco representan probabilidades válidas.
        if (!Double.isFinite(probabilidad)
                || probabilidad < 0.0
                || probabilidad > 1.0) {

            throw new IllegalArgumentException(
                    "La probabilidad debe estar entre 0 y 1");
        }

        // La configuración se valida una vez y nunca consume su duración.
        this.estado = new EstadoAlterado(tipo, duracion, magnitud);
        this.probabilidad = probabilidad;
    }

    @Override
    public String descripcion() {
        int magnitud = estado.getMagnitud();
        String efecto = switch (estado.getTipo()) {
            case VENENO -> "Veneno: " + magnitud + " de daño por comienzo de turno";
            case ATURDIDO -> "Aturdido: pierde 1 PA al abrir el turno";
            case ESCUDO -> "Escudo: reduce el daño recibido " + magnitud + " %";
            case RALENTIZADO -> "Ralentizado: velocidad −" + magnitud + " %";
            case AUMENTO_ATAQUE -> "Ataque +" + magnitud + " %";
            case REDUCCION_ATAQUE -> "Ataque −" + magnitud + " %";
            case AUMENTO_DEFENSA -> "Defensa +" + magnitud + " %";
            case REDUCCION_DEFENSA -> "Defensa −" + magnitud + " %";
        };
        return efecto + " (" + estado.getTurnosRestantes() + " comienzo(s) de turno propio; "
                + Math.round(probabilidad * 100) + " % de probabilidad)";
    }

    @Override
    public String aplicar(Entidad usuario, Entidad objetivo) {
        if (usuario == null || objetivo == null) {
            throw new IllegalArgumentException(
                    "El usuario y el objetivo son obligatorios");
        }

        // Math.random devuelve un valor entre 0 inclusive y 1 exclusivo.
        if (!objetivo.estaVivo()) return "";
        if (Math.random() >= probabilidad) return "El estado no se aplicó a " + objetivo.getNombre() + ".";

        // Entidad guarda una copia con duración propia para cada portador.
        objetivo.agregarEstado(estado);
        return objetivo.getNombre() + ": " + descripcion() + ".";
    }
}
