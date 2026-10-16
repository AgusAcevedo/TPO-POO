package zonaliberada.models.combate.acciones;

import zonaliberada.models.combate.efectos.EfectoDanio;
import zonaliberada.models.personajes.Entidad;

public class Atacar extends Accion {

    // El ataque básico utiliza el 100 % del ataque total.
    // El efecto puede reutilizarse porque su configuración no cambia.
    private final EfectoDanio efectoDanio = new EfectoDanio(100);

    @Override
    public boolean validar(ContextoAccion contexto) {
        if (contexto == null) {
            return false;
        }

        // Un ataque básico no utiliza habilidades ni consumibles.
        if (contexto.getItem() != null || contexto.getHabilidad() != null) {
            return false;
        }

        Entidad origen = contexto.getOrigen();
        Entidad[] objetivos = contexto.getObjetivos();

        // El ataque básico afecta a un único objetivo.
        if (objetivos.length != 1) {
            return false;
        }

        Entidad objetivo = objetivos[0];

        // ControladorTurno comprobará además el bando,
        // el alcance y que el origen sea el actor actual.
        return origen.estaVivo()
                && objetivo.estaVivo()
                && origen != objetivo;
    }

    @Override
    public void ejecutar(ContextoAccion contexto) {
        if (!validar(contexto)) {
            throw new IllegalArgumentException(
                    "El contexto no es válido para un ataque básico");
        }

        // El efecto compartido calcula el daño y sortea el crítico.
        contexto.agregarMensaje(contexto.getOrigen().getNombre() + " ataca.");
        contexto.agregarMensaje(efectoDanio.aplicar(
                contexto.getOrigen(),
                contexto.getObjetivos()[0]));

        // ControladorTurno ya pagó los recursos y comprobará
        // el resultado de la batalla y el cierre del turno.
    }
}
