package zonaliberada.controllers;

import zonaliberada.models.combate.Batalla;
import zonaliberada.models.combate.EstadoBatalla;
import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.combate.acciones.Accion;
import zonaliberada.models.combate.acciones.ContextoAccion;
import zonaliberada.models.inventario.Consumible;
import zonaliberada.models.inventario.Inventario;
import zonaliberada.models.personajes.Enemigo;
import zonaliberada.models.personajes.Entidad;

/** Construye el contexto, valida y paga una acción antes de delegar su ejecución. */
public class ControladorTurno {

    private final Batalla batalla;
    private final Inventario inventario;

    public ControladorTurno(Batalla batalla, Inventario inventario) {
        if (batalla == null || inventario == null) {
            throw new IllegalArgumentException(
                    "La batalla y el inventario son obligatorios");
        }

        this.batalla = batalla;
        this.inventario = inventario;
    }

    public void iniciarTurno() {
        Entidad actor = batalla.obtenerActorActual();

        if (batalla.verificarResultado() != EstadoBatalla.EN_CURSO
                || actor == null || !actor.estaVivo()) {
            throw new IllegalStateException(
                    "La batalla debe haber seleccionado un actor vivo");
        }

        // Batalla ya abrió el turno: no volvemos a cargar PA
        // ni a incrementar la concentración del mago.
        if (actor instanceof Enemigo enemigo) {
            ejecutarTurnoEnemigo(enemigo);
        }

        // Si es un aliado, esperamos sus solicitudes de acción.
    }

    public boolean ejecutarAccion(
            Accion accion,
            Entidad[] objetivos,
            Habilidad habilidad,
            Consumible item) {

        // Una solicitud incompleta se rechaza sin gastar recursos.
        if (accion == null || objetivos == null
                || (habilidad != null && item != null)) {
            return false;
        }

        for (Entidad objetivo : objetivos) {
            if (objetivo == null) {
                return false;
            }
        }

        if (batalla.verificarResultado() != EstadoBatalla.EN_CURSO
                || batalla.obtenerActorActual() == null) {
            return false;
        }

        // El origen siempre es el actor actual de la batalla.
        // Quien solicita la acción no puede elegir otro origen.
        Entidad actor = batalla.obtenerActorActual();
        ContextoAccion contexto = new ContextoAccion(actor, objetivos, item, habilidad);

        if (!actor.estaVivo() || !accion.validar(contexto)) {
            return false;
        }

        // El controlador comprueba bando, alcance y pertenencia
        // sin preguntar qué clase concreta de acción recibió.
        if (!objetivosValidos(accion, contexto)) {
            return false;
        }

        int costoPA = accion.costoPA(contexto);
        int costoMana = accion.costoMana(contexto);

        if (costoPA < 0 || costoMana < 0) {
            throw new IllegalStateException(
                    "Una acción no puede tener costos negativos");
        }

        // Ambos recursos se comprueban antes de descontar cualquiera.
        if (!actor.tienePA(costoPA) || !actor.tieneMana(costoMana)) {
            return false;
        }

        // No gastamos PA ni maná si el consumible no pudo retirarse.
        if (item != null && !inventario.retirarItem(item)) {
            return false;
        }

        actor.gastarPA(costoPA);
        actor.gastarMana(costoMana);

        // Java ejecuta el comportamiento de la acción recibida.
        // La acción no vuelve a pagar PA, maná ni consumibles.
        accion.ejecutar(contexto);
        batalla.registrarMensaje(contexto.getMensaje());

        if (batalla.verificarResultado() == EstadoBatalla.EN_CURSO
                && (!actor.estaVivo() || actor.getPuntosAccion() == 0)) {
            finalizarTurno();
        }

        // ControladorBatalla seleccionará al siguiente actor.
        return true;
    }

    public void finalizarTurno() {
        batalla.finalizarTurno();
    }

    private void ejecutarTurnoEnemigo(Enemigo enemigo) {
        while (batalla.verificarResultado() == EstadoBatalla.EN_CURSO
                && batalla.obtenerActorActual() == enemigo
                && enemigo.estaVivo()
                && enemigo.getPuntosAccion() > 0) {

            int paAntes = enemigo.getPuntosAccion();

            // Las decisiones pertenecen al enemigo.
            Accion accion = enemigo.elegirAccion(batalla);

            if (accion == null) {
                break;
            }

            // Volvemos a elegir después de cada acción:
            // un objetivo pudo ser derrotado y dejar libre la retaguardia.
            Entidad[] objetivos =
                    enemigo.elegirObjetivos(accion, batalla);

            // Habilidades y acciones de boss usan la misma validación y pago.
            if (!ejecutarAccion(accion, objetivos, enemigo.getHabilidadElegida(), null)) {
                break;
            }
            enemigo.alEjecutarAccion();

            // Evita repetir indefinidamente una acción sin gasto de PA.
            if (enemigo.getPuntosAccion() >= paAntes) {
                break;
            }
        }

        // Si la acción no cerró el turno, descartamos los PA sobrantes.
        // No seleccionamos al siguiente actor: lo hará ControladorBatalla.
        if (batalla.obtenerActorActual() == enemigo) {
            finalizarTurno();
        }
    }

    private boolean objetivosValidos(
            Accion accion,
            ContextoAccion contexto) {

        Entidad[] elegidos = contexto.getObjetivos();
        boolean grupal = accion.esGrupal(contexto);

        // Algunas acciones, como defenderse, no necesitan
        // un objetivo externo. Accion.validar decide si esto es correcto.
        if (elegidos.length == 0) {
            return !grupal;
        }

        Entidad[] candidatos = batalla.objetivosValidos(
                contexto.getOrigen(),
                accion.afectaAliados(contexto),
                accion.permiteDistancia(contexto));

        // Una acción grupal debe incluir a todos los candidatos.
        if (grupal && elegidos.length != candidatos.length) {
            return false;
        }

        for (int i = 0; i < elegidos.length; i++) {
            boolean encontrado = false;

            for (Entidad candidato : candidatos) {
                // Comparamos los objetos reales, no sus identificadores.
                if (elegidos[i] == candidato) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                return false;
            }

            // Un personaje no puede recibir dos veces la misma acción
            // por haber sido incluido dos veces en el arreglo.
            for (int j = 0; j < i; j++) {
                if (elegidos[i] == elegidos[j]) {
                    return false;
                }
            }
        }

        return true;
    }
}
