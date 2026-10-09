package zonaliberada.models.mapa;

import java.util.List;

public class Mapa {
    private List<Zona> zonas;
    private int indiceZonaActual;

    public Mapa() {
    }

    public Mapa(List<Zona> zonas, int indiceZonaActual) {
    }

    public Zona zonaActual() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean puedeAcceder(UbicacionMapa ubicacion) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void actualizarProgreso() {
    }

    public boolean estaCompletado() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
