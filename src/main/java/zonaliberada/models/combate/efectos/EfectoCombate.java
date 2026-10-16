package zonaliberada.models.combate.efectos;

import java.util.List;
import zonaliberada.models.personajes.Entidad;

/** Componente reutilizable: una habilidad o un consumible puede combinar varios efectos. */
public abstract class EfectoCombate {

    /** Explica la configuración sin aplicar el efecto ni calcular un resultado. */
    public abstract String descripcion();
    /** Aplica una sola vez y devuelve el resultado real para mostrarlo en combate. */
    public abstract String aplicar(Entidad usuario, Entidad objetivo);

    public static String aplicarTodos(List<EfectoCombate> efectos, Entidad usuario, Entidad objetivo) {
        String mensaje = "";
        for (EfectoCombate efecto : efectos) {
            String resultado = efecto.aplicar(usuario, objetivo);
            if (!resultado.isBlank()) mensaje += (mensaje.isEmpty() ? "" : " ") + resultado;
        }
        return mensaje;
    }

    /** Habilidades y consumibles muestran sus efectos con el mismo formato. */
    public static String describir(List<EfectoCombate> efectos) {
        String descripcion = "";
        for (int i = 0; i < efectos.size(); i++) {
            if (i > 0) descripcion += "; ";
            descripcion += efectos.get(i).descripcion();
        }
        return descripcion;
    }
}
