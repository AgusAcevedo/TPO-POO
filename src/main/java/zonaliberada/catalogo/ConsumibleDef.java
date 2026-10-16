package zonaliberada.catalogo;

import java.util.List;
import zonaliberada.models.combate.efectos.EfectoCombate;

/** Describe un tipo de consumible; su cantidad pertenece al inventario. */
public final class ConsumibleDef {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final boolean afectaAliados;
    private final boolean grupal;
    private final List<EfectoCombate> efectos;

    public ConsumibleDef(String id, String nombre, String descripcion,
            boolean afectaAliados, boolean grupal, List<EfectoCombate> efectos) {
        if (id == null || id.isBlank() || nombre == null || nombre.isBlank() || descripcion == null) {
            throw new IllegalArgumentException("Los datos del consumible son obligatorios");
        }
        if (efectos == null || efectos.isEmpty()) {
            throw new IllegalArgumentException("El consumible necesita al menos un efecto");
        }
        for (EfectoCombate efecto : efectos) {
            if (efecto == null) throw new IllegalArgumentException("Los efectos no pueden contener null");
        }
        this.id = id.strip();
        this.nombre = nombre.strip();
        this.descripcion = descripcion;
        this.afectaAliados = afectaAliados;
        this.grupal = grupal;
        this.efectos = List.copyOf(efectos);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean afectaAliados() { return afectaAliados; }
    public boolean esGrupal() { return grupal; }
    public List<EfectoCombate> getEfectos() { return efectos; }
}
