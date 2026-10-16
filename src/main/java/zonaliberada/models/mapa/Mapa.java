package zonaliberada.models.mapa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Mantiene el avance entre zonas; cada Zona controla el orden de sus ubicaciones. */
public class Mapa {
    private final List<Zona> zonas;
    private int indiceZonaActual;

    public Mapa(List<Zona> zonas, int indiceZonaActual) {
        if (zonas == null || zonas.isEmpty()
                || indiceZonaActual < 0 || indiceZonaActual >= zonas.size()) {
            throw new IllegalArgumentException("Zonas o índice actual inválidos");
        }
        for (Zona zona : zonas) {
            if (zona == null) throw new IllegalArgumentException("Las zonas no pueden contener null");
        }
        this.zonas = List.copyOf(zonas);
        this.indiceZonaActual = indiceZonaActual;
        validarProgreso();
    }

    public List<Zona> getZonas() { return zonas; }
    public int getIndiceZonaActual() { return indiceZonaActual; }
    public Zona zonaActual() { return zonas.get(indiceZonaActual); }

    public boolean estaCompletado() {
        for (Zona zona : zonas) {
            if (!zona.estaCompletada()) return false;
        }
        return true;
    }

    public UbicacionMapa buscarPorId(String id) {
        for (Zona zona : zonas) {
            for (UbicacionMapa ubicacion : zona.getUbicaciones()) {
                if (ubicacion.getId().equals(id)) return ubicacion;
            }
        }
        return null;
    }

    public boolean puedeAcceder(UbicacionMapa ubicacion) {
        // Las zonas anteriores siguen accesibles para los encuentros repetibles.
        for (int i = 0; i <= indiceZonaActual; i++) {
            if (zonas.get(i).puedeAcceder(ubicacion)) return true;
        }
        return false;
    }

    public void actualizarProgreso() {
        zonaActual().actualizarDisponibilidad();
        // Al completar una zona se habilita la siguiente, sin reiniciar las anteriores.
        while (zonaActual().estaCompletada() && indiceZonaActual < zonas.size() - 1) {
            indiceZonaActual++;
            zonaActual().desbloquear();
        }
    }

    private void validarProgreso() {
        Set<String> ids = new HashSet<>();
        Set<String> nombres = new HashSet<>();
        for (int i = 0; i < zonas.size(); i++) {
            Zona zona = zonas.get(i);
            if (!nombres.add(zona.getNombre())
                    || (i <= indiceZonaActual && !zona.estaDesbloqueada())
                    || (i < indiceZonaActual && !zona.estaCompletada())
                    || (i > indiceZonaActual && zona.estaDesbloqueada())) {
                throw new IllegalStateException("Progreso de zonas inconsistente");
            }
            for (UbicacionMapa ubicacion : zona.getUbicaciones()) {
                if (!ids.add(ubicacion.getId())) throw new IllegalStateException("ID de ubicación repetido");
            }
        }
        if (indiceZonaActual < zonas.size() - 1 && zonas.get(indiceZonaActual).estaCompletada()) {
            throw new IllegalStateException("Falta avanzar a la próxima zona");
        }
    }
}
