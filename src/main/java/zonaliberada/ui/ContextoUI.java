package zonaliberada.ui;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import zonaliberada.PantallaId;
import zonaliberada.controllers.ControladorPartida;
import zonaliberada.dtos.*;
import zonaliberada.models.combate.EstadoBatalla;

/** Comparte el controlador y la selección visual, no las reglas del juego. */
public final class ContextoUI {
    private final ControladorPartida juego;
    // Consumer recibe un dato; Predicate devuelve boolean; Runnable ejecuta sin argumentos.
    private final Consumer<PantallaId> navegar;
    private final Consumer<String> informar;
    private final Predicate<String> confirmar;
    private final Runnable salir;
    private int zonaSeleccionada;
    private String personajeSeleccionado;

    public ContextoUI(ControladorPartida juego, Consumer<PantallaId> navegar,
            Consumer<String> informar, Predicate<String> confirmar, Runnable salir) {
        this.juego = Objects.requireNonNull(juego);
        this.navegar = Objects.requireNonNull(navegar);
        this.informar = Objects.requireNonNull(informar);
        this.confirmar = Objects.requireNonNull(confirmar);
        this.salir = Objects.requireNonNull(salir);
    }

    public ControladorPartida juego() { return juego; }
    public boolean hayPartida() { return juego.hayPartida(); }

    public int zonaSeleccionada() {
        MapaDTO mapa = juego.getMapaDTO();
        if (mapa != null && (zonaSeleccionada < 0 || zonaSeleccionada >= mapa.getZonas().size())) {
            zonaSeleccionada = mapa.getIndiceZonaActual();
        }
        return zonaSeleccionada;
    }

    public PersonajeDTO personajeSeleccionado() {
        // Se conserva el ID seleccionado, pero la ficha se consulta de nuevo para evitar datos viejos.
        return PersonajeDTO.buscarPorId(juego.getPartyDTO(), personajeSeleccionado);
    }
    public void avisar(String mensaje) { informar.accept(mensaje); }
    public boolean confirmar(String mensaje) { return confirmar.test(mensaje); }

    public void mostrar(PantallaId pantalla) {
        if (!hayPartida() && pantalla != PantallaId.INICIO) {
            avisar("Creá una partida nueva primero.");
            return;
        }
        if (juego.estadoBatalla() != EstadoBatalla.NO_INICIADA
                && pantalla != PantallaId.BATALLA) {
            avisar("Terminá o abandoná el intento antes de salir del combate.");
            return;
        }
        navegar.accept(pantalla);
    }

    public void nuevaPartida() {
        if (juego.estadoBatalla() != EstadoBatalla.NO_INICIADA) {
            avisar("Terminá o abandoná la batalla antes de crear otra partida.");
            return;
        }
        if (hayPartida()
                && !confirmar("Iniciar una partida nueva elimina el progreso de esta sesión. ¿Continuar?")) return;
        juego.nuevaPartida();
        reiniciarSeleccion();
        mostrar(PantallaId.MAPA_PRINCIPAL);
    }

    public void cargarPartida() {
        juego.cargarPartida();
        avisar(juego.getUltimoMensaje());
    }

    public void volverAlMenu() {
        if (juego.estadoBatalla() != EstadoBatalla.NO_INICIADA) {
            avisar("Usá Abandonar para salir de un combate en curso.");
            return;
        }
        // Volver al menú no descarta la sesión: CONTINUAR permite retomarla.
        mostrar(PantallaId.INICIO);
    }

    public void verZona(int indice) {
        MapaDTO mapa = juego.getMapaDTO();
        if (mapa == null || indice < 0 || indice >= mapa.getZonas().size()
                || !mapa.getZonas().get(indice).isDesbloqueada()) return;
        zonaSeleccionada = indice;
        mostrar(PantallaId.MAPA_ZONA);
    }

    public void verZonaActual() {
        if (hayPartida()) verZona(juego.getMapaDTO().getIndiceZonaActual());
    }

    public void verPersonaje(String id) {
        if (PersonajeDTO.buscarPorId(juego.getPartyDTO(), id) == null) return;
        personajeSeleccionado = id;
        mostrar(PantallaId.DETALLE_PERSONAJE);
    }

    public void iniciarUbicacion(String id) {
        if (!juego.iniciarUbicacion(id)) { avisar(juego.getUltimoMensaje()); return; }
        if (juego.estadoBatalla() != EstadoBatalla.NO_INICIADA) {
            actualizarCombate();
        } else {
            avisar(juego.getUltimoMensaje());
            mostrar(PantallaId.MAPA_ZONA);
        }
    }

    /** La vista solicita procesar el resultado; el controlador entrega/restaura. */
    public void actualizarCombate() {
        EstadoBatalla estado = juego.estadoBatalla();
        if (estado == EstadoBatalla.VICTORIA || estado == EstadoBatalla.DERROTA) {
            juego.finalizarBatalla();
            mostrar(estado == EstadoBatalla.VICTORIA ? PantallaId.VICTORIA : PantallaId.DERROTA);
        } else {
            mostrar(PantallaId.BATALLA);
        }
    }

    public void reintentarBatalla() {
        if (!juego.reintentarBatalla()) { avisar(juego.getUltimoMensaje()); return; }
        actualizarCombate();
    }

    public void abandonarBatalla() {
        if (!confirmar("Se devolverán los objetos usados y los aliados recuperarán vida y maná, "
                + "sin recompensas. ¿Abandonar?")) return;
        if (juego.abandonarBatalla()) mostrar(PantallaId.MAPA_ZONA);
    }

    /** También se usa al cerrar la ventana: no se pierde una edición silenciosamente. */
    public boolean prepararSalida() {
        if (hayPartida() && !confirmar("El guardado todavía no está implementado. "
                + "Al cerrar se perderá el progreso de esta sesión. ¿Salir?")) return false;
        if (juego.estadoBatalla() != EstadoBatalla.NO_INICIADA) {
            if (!confirmar("Salir también abandona el intento actual. ¿Continuar?")) return false;
            juego.abandonarBatalla();
        }
        return true;
    }
    public void salir() { salir.run(); }

    private void reiniciarSeleccion() {
        zonaSeleccionada = juego.getMapaDTO().getIndiceZonaActual();
        personajeSeleccionado = null;
    }
}
