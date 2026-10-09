package zonaliberada.models.inventario;

public abstract class Item {
    private String id;
    private String nombre;
    private String descripcion;

    protected Item() {
    }

    protected Item(String id, String nombre, String descripcion) {
    }
}
