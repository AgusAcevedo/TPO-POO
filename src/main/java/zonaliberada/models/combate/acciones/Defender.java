package zonaliberada.models.combate.acciones;

import zonaliberada.models.combate.TipoEstado;
import zonaliberada.models.combate.efectos.EfectoEstado;
import zonaliberada.models.personajes.Entidad;

public class Defender extends Accion {

    private static final int BONUS_DEFENSA = 50;
    private static final int DURACION = 1;
    private final EfectoEstado efecto = new EfectoEstado(
            TipoEstado.AUMENTO_DEFENSA, DURACION, BONUS_DEFENSA, 1.0);

    @Override
    public boolean afectaAliados(ContextoAccion contexto) {
        // Afecta al propio personaje, sea aliado o enemigo.
        return true;
    }

    @Override
    public boolean validar(ContextoAccion contexto) {
        return contexto != null
                && contexto.getOrigen().estaVivo()
                && contexto.getObjetivos().length == 0
                && contexto.getHabilidad() == null
                && contexto.getItem() == null;
    }

    @Override
    public void ejecutar(ContextoAccion contexto) {
        if (!validar(contexto)) {
            throw new IllegalArgumentException(
                    "El contexto no es válido para defenderse");
        }

        Entidad origen = contexto.getOrigen();

        contexto.agregarMensaje(origen.getNombre() + " se defiende.");
        contexto.agregarMensaje(efecto.aplicar(origen, origen));

        // Heredamos el costo de 1 PA y 0 maná.
        // ControladorTurno ya descontó esos recursos.
    }
}
