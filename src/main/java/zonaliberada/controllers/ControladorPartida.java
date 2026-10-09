package zonaliberada.controllers;

import zonaliberada.factories.FabricaJuego;
import zonaliberada.models.Partida;
import zonaliberada.models.inventario.Equipable;
import zonaliberada.models.mapa.UbicacionMapa;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.persistence.PersistenciaArchivo;

public class ControladorPartida {
    private Partida partida;
    private PersistenciaArchivo persistencia;
    private FabricaJuego fabrica;
    private ControladorBatalla controladorBatalla;

    public ControladorPartida() {
    }

    public ControladorPartida(
            Partida partida,
            PersistenciaArchivo persistencia,
            FabricaJuego fabrica,
            ControladorBatalla controladorBatalla) {
    }

    public void nuevaPartida() {
    }

    public boolean guardarPartida() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean cargarPartida() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean iniciarUbicacion(UbicacionMapa ubicacion) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void finalizarBatalla() {
    }

    public boolean editarFormacion(int numero, Aliado aliado) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean quitarDeFormacion(Aliado aliado) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean equipar(Aliado aliado, Equipable item) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean desequipar(Aliado aliado, Equipable item) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
