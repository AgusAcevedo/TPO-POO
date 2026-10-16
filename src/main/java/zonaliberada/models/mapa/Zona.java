package zonaliberada.models.mapa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Una zona habilita sus ubicaciones en orden, sin permitir saltos. */
public final class Zona {
    private final String nombre;
    private final List<UbicacionMapa> ubicaciones;
    private boolean desbloqueada;

    public Zona(String nombre, List<UbicacionMapa> ubicaciones, boolean desbloqueada) {
        if (nombre == null || nombre.isBlank() || ubicaciones == null
                || ubicaciones.isEmpty()) {
            throw new IllegalArgumentException("La zona necesita nombre y ubicaciones");
        }
        for (UbicacionMapa ubicacion : ubicaciones) {
            if (ubicacion == null) throw new IllegalArgumentException("Las ubicaciones no pueden contener null");
        }
        this.nombre = nombre.strip();
        this.ubicaciones = List.copyOf(ubicaciones);
        this.desbloqueada = desbloqueada;
        validarSecuencia();
        actualizarDisponibilidad();
    }

    public String getNombre() { return nombre; }
    public List<UbicacionMapa> getUbicaciones() { return ubicaciones; }
    public boolean estaDesbloqueada() { return desbloqueada; }

    public boolean estaCompletada() {
        for (UbicacionMapa ubicacion : ubicaciones) {
            if (ubicacion.getEstado() != EstadoUbicacion.COMPLETADA) return false;
        }
        return true;
    }

    public void desbloquear() {
        desbloqueada = true;
        actualizarDisponibilidad();
    }

    public UbicacionMapa siguienteUbicacion() {
        if (!desbloqueada) return null;
        for (UbicacionMapa ubicacion : ubicaciones) {
            if (ubicacion.getEstado() != EstadoUbicacion.COMPLETADA) return ubicacion;
        }
        return null;
    }

    public boolean puedeAcceder(UbicacionMapa ubicacion) {
        if (!desbloqueada || ubicacion == null) return false;
        for (UbicacionMapa propia : ubicaciones) {
            // Debe ser la ubicación real de esta zona, no otra instancia con el mismo ID.
            if (propia == ubicacion) {
                if (ubicacion.getEstado() == EstadoUbicacion.COMPLETADA) return ubicacion.esRepetible();
                return ubicacion == siguienteUbicacion()
                        && ubicacion.getEstado() == EstadoUbicacion.DISPONIBLE;
            }
        }
        return false;
    }

    public void actualizarDisponibilidad() {
        if (!desbloqueada) return;
        UbicacionMapa siguiente = siguienteUbicacion();
        if (siguiente != null) siguiente.habilitar();
    }

    private void validarSecuencia() {
        boolean pendiente = false;
        Set<String> ids = new HashSet<>();
        for (UbicacionMapa ubicacion : ubicaciones) {
            if (!ids.add(ubicacion.getId())) throw new IllegalArgumentException("ID de ubicación repetido");
            if (!desbloqueada && ubicacion.getEstado() != EstadoUbicacion.BLOQUEADA) {
                throw new IllegalArgumentException("Una zona bloqueada no puede tener progreso");
            }
            if (ubicacion.getEstado() == EstadoUbicacion.COMPLETADA) {
                if (pendiente) throw new IllegalArgumentException("Progreso fuera de secuencia");
            } else {
                if (pendiente && ubicacion.getEstado() != EstadoUbicacion.BLOQUEADA) {
                    throw new IllegalArgumentException("Solo se habilita la siguiente ubicación");
                }
                pendiente = true;
            }
        }
    }

}
