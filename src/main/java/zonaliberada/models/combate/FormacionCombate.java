package zonaliberada.models.combate;

import zonaliberada.models.personajes.Entidad;

public class FormacionCombate {
    private TipoBando bando;
    private PosicionFormacion[] posiciones;

    public FormacionCombate() {
    }

    public FormacionCombate(TipoBando bando, PosicionFormacion[] posiciones) {
    }

    public boolean colocar(Entidad entidad, int numero) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean quitar(Entidad entidad) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad[] participantes() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad[] miembrosVivos() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean contiene(Entidad entidad) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad ocupanteEn(int numero) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad[] objetivosAlcanzables(boolean aDistancia) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
