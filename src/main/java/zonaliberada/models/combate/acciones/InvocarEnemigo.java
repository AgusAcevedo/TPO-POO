package zonaliberada.models.combate.acciones;

import zonaliberada.models.ReglasJuego;
import zonaliberada.models.combate.Batalla;
import zonaliberada.models.combate.EstadoBatalla;
import zonaliberada.models.personajes.Boss;
import zonaliberada.models.personajes.Enemigo;

/** Acción de boss: también se valida y paga mediante ControladorTurno. */
public final class InvocarEnemigo extends Accion {
    private final Batalla batalla;
    private final Enemigo enemigo;
    private final int posicion;

    public InvocarEnemigo(Batalla batalla, Enemigo enemigo, int posicion) {
        if (batalla == null || enemigo == null || posicion < 1 || posicion > ReglasJuego.CANTIDAD_POSICIONES) {
            throw new IllegalArgumentException("Invocación inválida");
        }
        this.batalla = batalla;
        this.enemigo = enemigo;
        this.posicion = posicion;
    }

    @Override
    public int costoPA(ContextoAccion contexto) { return 2; }

    @Override
    public boolean validar(ContextoAccion contexto) {
        return contexto != null && contexto.getOrigen() instanceof Boss boss
                && boss.esFinal() && boss.getFaseActual() == 2
                && !boss.haInvocado() && boss.estaVivo()
                && contexto.getOrigen() == batalla.obtenerActorActual()
                && contexto.getObjetivos().length == 0
                && contexto.getHabilidad() == null && contexto.getItem() == null
                && batalla.verificarResultado() == EstadoBatalla.EN_CURSO
                && batalla.enemigoEn(posicion) == null
                && enemigo.estaVivo() && !enemigo.estaEnBatalla();
    }

    @Override
    public void ejecutar(ContextoAccion contexto) {
        if (!validar(contexto)) throw new IllegalArgumentException("Contexto de invocación inválido");
        if (!batalla.agregarEnemigo(enemigo, posicion)) {
            throw new IllegalStateException("No se pudo incorporar el enemigo");
        }
        // Solo marcamos la invocación si el refuerzo se incorporó correctamente.
        ((Boss) contexto.getOrigen()).registrarInvocacion();
        contexto.agregarMensaje(contexto.getOrigen().getNombre() + " invocó a " + enemigo.getNombre() + ".");
    }
}
