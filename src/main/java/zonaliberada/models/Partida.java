package zonaliberada.models;

import zonaliberada.models.combate.Recompensa;
import zonaliberada.models.inventario.Inventario;
import zonaliberada.models.mapa.Mapa;
import zonaliberada.models.mapa.UbicacionMapa;

public class Partida {
    private Party party;
    private Inventario inventario;
    private Mapa mapa;

    public Partida() {
    }

    public Partida(Party party, Inventario inventario, Mapa mapa) {
    }

    public UbicacionMapa ubicacionActual() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean puedeIniciar(UbicacionMapa ubicacion) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void completarUbicacion(UbicacionMapa ubicacion) {
    }

    public void aplicarRecompensa(Recompensa recompensa) {
    }
}
