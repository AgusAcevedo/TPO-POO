package zonaliberada.models.mapa;

import java.util.List;
import zonaliberada.models.personajes.TipoEnemigo;

public class UbicacionMapa {
    private String nombre;
    private TipoUbicacion tipo;
    private EstadoUbicacion estado;
    private boolean repetible;
    private List<TipoEnemigo> enemigos;
    private String bossId;

    public UbicacionMapa() {
    }

    public UbicacionMapa(
            String nombre,
            TipoUbicacion tipo,
            EstadoUbicacion estado,
            boolean repetible,
            List<TipoEnemigo> enemigos,
            String bossId) {
    }

    public void completar() {
    }

    public void habilitar() {
    }

    public boolean puedeIniciarse() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean esCombate() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
