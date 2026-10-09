package zonaliberada.persistence;

import zonaliberada.models.Partida;

public class PersistenciaArchivo {
    private String rutaArchivo;

    public PersistenciaArchivo() {
    }

    public PersistenciaArchivo(String rutaArchivo) {
    }

    public boolean guardarCheckpoint(Partida partida) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Partida cargarCheckpoint() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean existeCheckpoint() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
