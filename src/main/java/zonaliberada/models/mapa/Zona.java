package zonaliberada.models.mapa;

import java.util.List;

public class Zona {
    private String nombre;
    private List<UbicacionMapa> ubicaciones;
    private boolean desbloqueada;

    public Zona() {
    }

    public Zona(String nombre, List<UbicacionMapa> ubicaciones, boolean desbloqueada) {
    }

    public UbicacionMapa siguienteUbicacion() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean puedeAcceder(UbicacionMapa ubicacion) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void actualizarDisponibilidad() {
    }

    public boolean estaCompletada() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
