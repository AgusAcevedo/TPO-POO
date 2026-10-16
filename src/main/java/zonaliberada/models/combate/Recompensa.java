package zonaliberada.models.combate;

import java.util.List;
import zonaliberada.models.inventario.Inventario;
import zonaliberada.models.inventario.Item;

/** Contenido de una entrega concreta; volver a procesarla no repite sus premios. */
public final class Recompensa {
    private final String id = java.util.UUID.randomUUID().toString();
    private final int experiencia;
    // Reutiliza las reglas de cantidades y piezas únicas, sin exponer un inventario modificable.
    private final Inventario contenido;
    private final int curacion;

    public Recompensa(int experiencia, List<Item> items, int curacion) {
        if (experiencia < 0 || curacion < 0) {
            throw new IllegalArgumentException("La experiencia y la curación no pueden ser negativas");
        }
        this.experiencia = experiencia;
        this.contenido = new Inventario(items);
        this.curacion = curacion;
    }

    public String getId() { return id; }
    public int getExperiencia() { return experiencia; }
    public List<Item> getItems() { return contenido.getItems(); }
    public int getCantidad(Item item) { return contenido.getCantidad(item); }
    public int getCuracion() { return curacion; }
}
