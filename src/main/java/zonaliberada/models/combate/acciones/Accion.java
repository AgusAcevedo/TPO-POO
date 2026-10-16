package zonaliberada.models.combate.acciones;

/** Contrato de acciones intercambiables: el controlador trabaja con esta base común. */
public abstract class Accion {

    public int costoPA(ContextoAccion contexto) {
        // Cada acción puede sobrescribir su costo.
        return 1;
    }

    public int costoMana(ContextoAccion contexto) {
        return 0;
    }

    public boolean afectaAliados(ContextoAccion contexto) {
        // "Aliados" significa el propio equipo de quien actúa.
        // Por defecto, la acción se dirige al equipo contrario.
        return false;
    }

    public boolean esGrupal(ContextoAccion contexto) {
        // Una acción grupal afecta a todos los objetivos válidos.
        return false;
    }

    public boolean permiteDistancia(ContextoAccion contexto) {
        // Por defecto, se respeta el alcance del actor.
        return contexto != null
                && contexto.getOrigen().isAtaqueDistancia();
    }

    // Comprueba las reglas particulares de la acción.
    // No debe gastar recursos ni aplicar efectos.
    public abstract boolean validar(ContextoAccion contexto);

    // Aplica los efectos. El controlador ya descontó los recursos.
    public abstract void ejecutar(ContextoAccion contexto);
}
