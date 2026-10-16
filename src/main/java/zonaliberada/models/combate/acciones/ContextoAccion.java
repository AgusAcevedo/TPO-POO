package zonaliberada.models.combate.acciones;

import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.inventario.Consumible;
import zonaliberada.models.personajes.Entidad;

/** Reúne los datos de una ejecución; el controlador lo crea a partir del actor actual. */
public class ContextoAccion {

    // Datos de una acción. El contexto no ejecuta sus efectos.
    private final Entidad origen;
    private final Entidad[] objetivos;
    private final Consumible item;
    private final Habilidad habilidad;
    private String mensaje = "";

    public ContextoAccion(
            Entidad origen,
            Entidad[] objetivos,
            Consumible item,
            Habilidad habilidad) {

        if (origen == null) {
            throw new IllegalArgumentException("El origen es obligatorio");
        }

        if (objetivos == null) {
            throw new IllegalArgumentException(
                    "Los objetivos no pueden ser null");
        }

        // Una acción usa una habilidad, un ítem o ninguno de los dos.
        if (item != null && habilidad != null) {
            throw new IllegalArgumentException(
                    "Una acción no puede usar un ítem y una habilidad a la vez");
        }

        // Copiamos el arreglo, no los personajes que contiene.
        Entidad[] objetivosPropios = objetivos.clone();

        for (Entidad objetivo : objetivosPropios) {
            if (objetivo == null) {
                throw new IllegalArgumentException(
                        "Los objetivos no pueden contener null");
            }
        }

        this.origen = origen;
        this.objetivos = objetivosPropios;
        this.item = item;
        this.habilidad = habilidad;
    }

    public Entidad getOrigen() {
        return origen;
    }

    public Entidad[] getObjetivos() {
        // Nadie puede reemplazar desde afuera los objetivos del contexto.
        return objetivos.clone();
    }

    public Consumible getItem() {
        return item;
    }

    public Habilidad getHabilidad() {
        return habilidad;
    }

    public String getMensaje() { return mensaje; }

    public void agregarMensaje(String texto) {
        if (!texto.isBlank()) mensaje += (mensaje.isEmpty() ? "" : " ") + texto;
    }
}
