package zonaliberada.models.inventario;

import zonaliberada.catalogo.ConsumibleDef;
import zonaliberada.models.combate.efectos.EfectoCombate;
import zonaliberada.models.personajes.Entidad;

/** Objeto reutilizable de un tipo; usarlo descuenta una dosis del inventario. */
public final class Consumible extends Item {
    private final ConsumibleDef definicion;

    public Consumible(ConsumibleDef definicion) {
        super(definicion.getId(), definicion.getNombre(), definicion.getDescripcion());
        this.definicion = definicion;
    }

    public boolean afectaAliados() { return definicion.afectaAliados(); }
    public boolean esGrupal() { return definicion.esGrupal(); }

    public String getDescripcionEfectos() {
        return EfectoCombate.describir(definicion.getEfectos());
    }

    public boolean puedeUsarse(Entidad usuario, Entidad objetivo) {
        // Los efectos actuales no reviven; PA, maná e inventario se validan en el controlador.
        return usuario != null && usuario.estaVivo()
                && objetivo != null && objetivo.estaVivo();
    }

    public String aplicarEfectos(Entidad usuario, Entidad objetivo) {
        if (!puedeUsarse(usuario, objetivo)) {
            throw new IllegalArgumentException("El usuario y el objetivo deben estar vivos");
        }
        return EfectoCombate.aplicarTodos(definicion.getEfectos(), usuario, objetivo);
    }
}
