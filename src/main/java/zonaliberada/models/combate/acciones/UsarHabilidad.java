package zonaliberada.models.combate.acciones;

import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.personajes.Entidad;

public class UsarHabilidad extends Accion {

    // Los costos y las reglas de selección pertenecen a la habilidad elegida.
    // El controlador consulta estos métodos sin conocer la acción concreta.
    @Override
    public int costoPA(ContextoAccion contexto) {
        return obtenerHabilidad(contexto).getCostoPA();
    }

    @Override
    public int costoMana(ContextoAccion contexto) {
        return obtenerHabilidad(contexto).getCostoMana();
    }

    @Override
    public boolean afectaAliados(ContextoAccion contexto) {
        return obtenerHabilidad(contexto).afectaAliados();
    }

    @Override
    public boolean esGrupal(ContextoAccion contexto) {
        return obtenerHabilidad(contexto).esGrupal();
    }

    @Override
    public boolean permiteDistancia(ContextoAccion contexto) {
        // Una habilidad tiene alcance propio: no depende del ataque básico.
        return obtenerHabilidad(contexto).permiteDistancia();
    }

    @Override
    public boolean validar(ContextoAccion contexto) {
        if (contexto == null || contexto.getHabilidad() == null
                || contexto.getItem() != null) {
            return false;
        }

        Habilidad habilidad = contexto.getHabilidad();

        // Comprueba que el usuario esté vivo y tenga disponible la habilidad.
        // Para los aliados, esto también considera el nivel de desbloqueo.
        if (!habilidad.puedeUsarse(contexto.getOrigen())) {
            return false;
        }

        Entidad[] objetivos = contexto.getObjetivos();

        // Una habilidad individual necesita exactamente un objetivo.
        if (objetivos.length == 0
                || (!habilidad.esGrupal() && objetivos.length != 1)) {
            return false;
        }

        // Todos deben estar vivos y no pueden aparecer dos veces.
        for (int i = 0; i < objetivos.length; i++) {
            if (!objetivos[i].estaVivo()) {
                return false;
            }

            for (int j = 0; j < i; j++) {
                if (objetivos[i] == objetivos[j]) {
                    return false;
                }
            }
        }

        // ControladorTurno comprobará además bando, alcance,
        // grupo completo, turno actual y recursos disponibles.
        return true;
    }

    @Override
    public void ejecutar(ContextoAccion contexto) {
        if (!validar(contexto)) {
            throw new IllegalArgumentException(
                    "El contexto no es válido para usar una habilidad");
        }

        Habilidad habilidad = contexto.getHabilidad();

        // ControladorTurno ya pagó los costos.
        // Aquí solamente aplicamos los efectos configurados.
        contexto.agregarMensaje(contexto.getOrigen().getNombre() + " usa " + habilidad.getNombre() + ".");
        contexto.agregarMensaje(habilidad.aplicarEfectos(
                contexto.getOrigen(), contexto.getObjetivos()));
    }

    private Habilidad obtenerHabilidad(ContextoAccion contexto) {
        // Evita repetir esta comprobación en los cinco métodos de consulta.
        if (contexto == null || contexto.getHabilidad() == null) {
            throw new IllegalArgumentException(
                    "El contexto debe contener una habilidad");
        }

        return contexto.getHabilidad();
    }
}
