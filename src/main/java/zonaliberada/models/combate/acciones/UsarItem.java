package zonaliberada.models.combate.acciones;

import zonaliberada.models.inventario.Consumible;
import zonaliberada.models.personajes.Entidad;

public class UsarItem extends Accion {

    // Heredamos de Accion el costo de 1 PA y 0 de maná.
    // El controlador también retirará una unidad del inventario.

    @Override
    public boolean afectaAliados(ContextoAccion contexto) {
        return obtenerItem(contexto).afectaAliados();
    }

    @Override
    public boolean esGrupal(ContextoAccion contexto) {
        return obtenerItem(contexto).esGrupal();
    }

    @Override
    public boolean permiteDistancia(ContextoAccion contexto) {
        // En esta versión, los consumibles alcanzan ambas filas.
        // No dependen del alcance del ataque básico del usuario.
        return true;
    }

    @Override
    public boolean validar(ContextoAccion contexto) {
        if (contexto == null || contexto.getItem() == null
                || contexto.getHabilidad() != null) {
            return false;
        }

        Consumible item = contexto.getItem();
        Entidad[] objetivos = contexto.getObjetivos();

        // Un consumible individual necesita exactamente un objetivo.
        if (objetivos.length == 0
                || (!item.esGrupal() && objetivos.length != 1)) {
            return false;
        }

        for (int i = 0; i < objetivos.length; i++) {
            // El modelo comprueba que el usuario y el objetivo estén vivos.
            if (!item.puedeUsarse(contexto.getOrigen(), objetivos[i])) {
                return false;
            }

            // Un mismo personaje no debe recibir dos veces el consumible.
            for (int j = 0; j < i; j++) {
                if (objetivos[i] == objetivos[j]) {
                    return false;
                }
            }
        }

        // ControladorTurno comprueba bando, pertenencia al combate,
        // grupo completo, actor actual, PA e inventario.
        // No comprobamos esos recursos aquí: al ejecutar,
        // el controlador ya los habrá descontado.
        return true;
    }

    @Override
    public void ejecutar(ContextoAccion contexto) {
        if (!validar(contexto)) {
            throw new IllegalArgumentException(
                    "El contexto no es válido para usar un consumible");
        }

        Consumible item = contexto.getItem();

        // Consumible aplica sus efectos sobre un objetivo.
        // La acción permite reutilizarlo para todos los objetivos del grupo.
        contexto.agregarMensaje(contexto.getOrigen().getNombre() + " usa " + item.getNombre() + ".");
        for (Entidad objetivo : contexto.getObjetivos()) {
            contexto.agregarMensaje(item.aplicarEfectos(contexto.getOrigen(), objetivo));
        }

        // No retiramos el ítem, no gastamos PA ni maná
        // y no seleccionamos al siguiente actor.
    }

    private Consumible obtenerItem(ContextoAccion contexto) {
        if (contexto == null || contexto.getItem() == null) {
            throw new IllegalArgumentException(
                    "El contexto debe contener un consumible");
        }

        return contexto.getItem();
    }
}
