package zonaliberada.persistence;

import zonaliberada.models.Partida;

/** Guardado y carga pendientes de implementar. No lee ni escribe archivos. */
public class PersistenciaArchivo {
    private String ultimoError = "";
    public String getUltimoError() { return ultimoError; }

    public boolean guardarCheckpoint(Partida partida) {
        ultimoError = "Guardar partidas está pendiente de implementar.";
        return false;
    }

    public Partida cargarCheckpoint() {
        ultimoError = "Cargar partidas está pendiente de implementar.";
        return null;
    }
}
