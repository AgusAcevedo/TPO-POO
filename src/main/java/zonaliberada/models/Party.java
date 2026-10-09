package zonaliberada.models;

import java.util.List;
import zonaliberada.models.combate.FormacionCombate;
import zonaliberada.models.personajes.Aliado;

public class Party {
    private List<Aliado> roster;
    private FormacionCombate formacion;
    private int maximoAliadosActivos = 4;

    public Party() {
    }

    public Party(List<Aliado> roster, FormacionCombate formacion) {
    }

    public boolean desbloquearAliado(Aliado aliado) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean contiene(Aliado aliado) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean editarFormacion(int numero, Aliado aliado) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean quitarDeFormacion(Aliado aliado) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public FormacionCombate formacionActual() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean formacionValida() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Aliado[] miembrosVivos() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void recuperarVidaCompleta() {
    }
}
