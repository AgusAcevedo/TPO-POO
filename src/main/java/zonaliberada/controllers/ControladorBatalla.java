package zonaliberada.controllers;

import zonaliberada.models.combate.Batalla;
import zonaliberada.models.combate.EstadoBatalla;
import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.combate.Recompensa;
import zonaliberada.models.combate.acciones.Accion;
import zonaliberada.models.inventario.Consumible;
import zonaliberada.models.inventario.Inventario;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.models.personajes.Entidad;

/** Alterna turnos y coordina el encuentro; el daño y los premios se resuelven en otras clases. */
public class ControladorBatalla {

    private Batalla batalla;
    private ControladorTurno controladorTurno;

    public Batalla getBatalla() { return batalla; }

    public EstadoBatalla estadoActual() {
        return batalla == null
                ? EstadoBatalla.NO_INICIADA
                : batalla.verificarResultado();
    }

    public boolean iniciarBatalla(
            Batalla nuevaBatalla,
            Inventario inventario) {

        if (nuevaBatalla == null || inventario == null
                || batalla != null) {
            return false;
        }

        // Batalla valida la formación y prepara a sus participantes.
        // Si falla, no vinculamos el encuentro al controlador.
        if (!nuevaBatalla.iniciar()) {
            return false;
        }

        batalla = nuevaBatalla;

        // Cada encuentro tiene su propio controlador de turno.
        controladorTurno = new ControladorTurno(batalla, inventario);

        siguienteTurno();
        return true;
    }

    public boolean ejecutarAccion(
            Accion accion,
            Entidad[] objetivos,
            Habilidad habilidad,
            Consumible item) {

        if (batalla == null
                || batalla.verificarResultado()
                != EstadoBatalla.EN_CURSO) {
            return false;
        }

        Entidad actor = batalla.obtenerActorActual();

        // Las solicitudes externas corresponden al jugador.
        // Los enemigos actúan dentro de ControladorTurno.
        if (!(actor instanceof Aliado) || !actor.estaVivo()) {
            return false;
        }

        boolean ejecutada = controladorTurno.ejecutarAccion(
                accion, objetivos, habilidad, item);

        // Una solicitud rechazada no hace avanzar los turnos.
        // Si al aliado le quedan PA, conservamos su turno.
        if (ejecutada) {
            siguienteTurno();
        }

        return ejecutada;
    }

    public void siguienteTurno() {
        if (batalla == null) {
            return;
        }

        while (batalla.verificarResultado()
                == EstadoBatalla.EN_CURSO) {

            Entidad actor = batalla.obtenerActorActual();

            if (actor != null && actor.estaVivo()
                    && actor.getPuntosAccion() > 0) {

                // Esperamos las decisiones del aliado:
                // no saltamos su turno ni volvemos a cargar sus PA.
                if (actor instanceof Aliado) {
                    return;
                }

                // Resuelve un turno enemigo que ya estuviera abierto.
                controladorTurno.iniciarTurno();
                continue;
            }

            // Limpia un actor derrotado o con PA agotados
            // si su turno no fue cerrado previamente.
            if (actor != null) {
                controladorTurno.finalizarTurno();
            }

            Entidad siguiente =
                    batalla.avanzarHastaSiguienteActor();

            if (siguiente == null) {
                return;
            }

            // Batalla abrió el turno; el controlador lo coordina.
            // Un aliado espera solicitudes.
            // Un enemigo juega automáticamente.
            controladorTurno.iniciarTurno();

            // Si el enemigo cerró su turno, seguimos buscando.
            // Si quedó un aliado activo, lo conservamos
            // en la próxima vuelta.
        }
    }

    public void finalizarTurno() {
        if (batalla == null
                || batalla.verificarResultado()
                != EstadoBatalla.EN_CURSO) {
            return;
        }

        // El jugador puede terminar antes de gastar todos sus PA.
        if (!(batalla.obtenerActorActual() instanceof Aliado)) {
            return;
        }

        controladorTurno.finalizarTurno();
        batalla.registrarMensaje("Turno finalizado; se descartaron los PA sobrantes.");
        siguienteTurno();
    }

    public Recompensa finalizarBatalla() {
        if (batalla == null) {
            throw new IllegalStateException(
                    "No hay una batalla asociada");
        }

        EstadoBatalla resultado = batalla.verificarResultado();

        if (resultado != EstadoBatalla.VICTORIA
                && resultado != EstadoBatalla.DERROTA) {
            throw new IllegalStateException(
                    "La batalla debe haber terminado");
        }

        if (resultado == EstadoBatalla.DERROTA) {
            return null;
        }

        // No aplicamos experiencia ni ítems desde este controlador.
        return batalla.entregarRecompensa();
    }

    public void abandonarBatalla() {
        if (batalla != null) {
            for (Entidad participante : batalla.participantes()) participante.terminarBatalla();
        }
        batalla = null;
        controladorTurno = null;
    }

    public void descartarBatalla() {
        if (batalla != null
                && batalla.verificarResultado()
                == EstadoBatalla.EN_CURSO) {
            throw new IllegalStateException(
                    "No se puede descartar una batalla en curso");
        }

        // Conservamos el resultado hasta que ControladorPartida
        // lo haya procesado. Después liberamos el encuentro.
        batalla = null;
        controladorTurno = null;
    }
}
