package zonaliberada.models.inventario;

/** Datos comunes: el ID identifica un tipo consumible o una pieza equipable. */
public abstract class Item {

    private final String id;
    private final String nombre;
    private final String descripcion;

    protected Item(String id, String nombre, String descripcion) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador del ítem es obligatorio");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del ítem es obligatorio");
        }

        // Permitimos una descripción vacía, pero no null.
        if (descripcion == null) {
            throw new IllegalArgumentException(
                    "La descripción no puede ser null");
        }

        this.id = id.strip();
        this.nombre = nombre.strip();
        this.descripcion = descripcion.strip();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
