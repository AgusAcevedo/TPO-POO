package zonaliberada.models.personajes;

import java.util.List;
import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.inventario.Equipable;

public abstract class Aliado extends Entidad {
    private int nivel;
    private int experiencia;
    private List<Equipable> equipados;

    protected Aliado() {
    }

    protected Aliado(int nivel, int experiencia, List<Equipable> equipados) {
    }

    public boolean equipar(Equipable item) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean desequipar(Equipable item) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean tieneEquipado(Equipable item) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void ganarExperiencia(int cantidad) {
    }

    @Override
    public Habilidad[] habilidadesDisponibles() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public abstract TipoClase tipoClase();
}
