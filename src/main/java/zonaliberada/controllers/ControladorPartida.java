package zonaliberada.controllers;

import java.util.*;
import zonaliberada.dtos.*;
import zonaliberada.factories.FabricaJuego;
import zonaliberada.models.Partida;
import zonaliberada.models.combate.*;
import zonaliberada.models.combate.acciones.*;
import zonaliberada.models.inventario.*;
import zonaliberada.models.mapa.*;
import zonaliberada.models.personajes.*;
import zonaliberada.persistence.PersistenciaArchivo;

/** Punto de entrada del juego. La UI solicita operaciones y recibe instantáneas. */
public class ControladorPartida {
    private Partida partida;
    private final PersistenciaArchivo persistencia;
    private final FabricaJuego fabrica;
    private final ControladorBatalla controladorBatalla;
    // Solo se respaldan cantidades; el equipo no puede cambiar durante el combate.
    private final Map<Consumible, Integer> consumiblesAntesBatalla = new LinkedHashMap<>();
    private ResultadoBatallaDTO ultimoResultado;
    private String ultimoMensaje = "";

    public ControladorPartida() {
        this(null, new PersistenciaArchivo(), new FabricaJuego(), new ControladorBatalla());
    }

    /** Permite reutilizar una partida y sus servicios, también en las pruebas. */
    public ControladorPartida(Partida partida, PersistenciaArchivo persistencia,
            FabricaJuego fabrica, ControladorBatalla controladorBatalla) {
        if (persistencia == null || fabrica == null || controladorBatalla == null
                || controladorBatalla.getBatalla() != null) {
            throw new IllegalArgumentException("Dependencias de partida inválidas");
        }
        this.partida = partida;
        this.persistencia = persistencia;
        this.fabrica = fabrica;
        this.controladorBatalla = controladorBatalla;
        if (partida != null && partida.hayCombateActivo()) {
            throw new IllegalArgumentException("La partida ya tiene un combate activo");
        }
    }

    public Partida getPartida() { return partida; }
    public boolean hayPartida() { return partida != null; }
    public String getUltimoMensaje() { return ultimoMensaje; }
    public ResultadoBatallaDTO getUltimoResultado() { return ultimoResultado; }
    public List<PersonajeDTO> getPartyDTO() {
        return partida == null ? List.of() : MapeadorDTO.roster(partida);
    }
    public InventarioDTO getInventarioDTO() {
        return partida == null ? new InventarioDTO(List.of()) : MapeadorDTO.inventario(partida);
    }
    public MapaDTO getMapaDTO() { return partida == null ? null : MapeadorDTO.mapa(partida); }
    public BatallaDTO getBatallaDTO() {
        // Detectamos un posible final antes de construir la información para la vista.
        controladorBatalla.estadoActual();
        return MapeadorDTO.batalla(controladorBatalla.getBatalla());
    }

    public EstadoBatalla estadoBatalla() { return controladorBatalla.estadoActual(); }

    public void nuevaPartida() {
        exigirSinBatalla();
        partida = fabrica.crearPartida();
        consumiblesAntesBatalla.clear();
        ultimoResultado = null;
        ultimoMensaje = "Partida nueva creada. El progreso se conserva solo durante esta sesión.";
    }

    public boolean guardarPartida() {
        if (!fueraDeBatalla()) return rechazar("Solo se puede guardar fuera de combate.");
        boolean guardado = persistencia.guardarCheckpoint(partida);
        ultimoMensaje = guardado ? "Checkpoint guardado." : persistencia.getUltimoError();
        return guardado;
    }

    public boolean cargarPartida() {
        if (controladorBatalla.getBatalla() != null) return rechazar("Procesá primero la batalla actual.");
        Partida cargada = persistencia.cargarCheckpoint();
        if (cargada == null) return rechazar(persistencia.getUltimoError());
        partida = cargada;
        consumiblesAntesBatalla.clear();
        ultimoResultado = null;
        ultimoMensaje = "Checkpoint recuperado.";
        return true;
    }

    public boolean editarFormacion(int numero, String id) {
        if (!fueraDeBatalla()) return false;
        try { return partida.editarFormacion(numero, resolverAliado(id)); }
        catch (IllegalArgumentException e) { return rechazar(e.getMessage()); }
    }

    public boolean quitarDeFormacion(String id) {
        return fueraDeBatalla() && partida.quitarDeFormacion(resolverAliado(id));
    }

    public boolean equipar(String aliadoId, String itemId) {
        if (!fueraDeBatalla()) return false;
        Item item = partida.getInventario().buscarPorId(itemId);
        return item instanceof Equipable equipo && partida.equipar(resolverAliado(aliadoId), equipo);
    }

    public boolean desequipar(String aliadoId, String itemId) {
        if (!fueraDeBatalla()) return false;
        Aliado aliado = resolverAliado(aliadoId);
        if (aliado == null) return false;
        return partida.desequipar(aliado, aliado.buscarEquipado(itemId));
    }

    public boolean iniciarUbicacion(String id) {
        if (partida == null) return false;
        UbicacionMapa ubicacion = partida.getMapa().buscarPorId(id);
        if (!fueraDeBatalla() || !partida.puedeIniciar(ubicacion)) {
            return rechazar("La ubicación o la formación no permiten iniciar.");
        }
        boolean entrenamiento = ubicacion.getEstado() == EstadoUbicacion.COMPLETADA;
        if (!ubicacion.esCombate()) {
            // Las actividades sin combate aplican el premio y avanzan el mapa directamente.
            Recompensa recompensa = fabrica.crearRecompensa(ubicacion);
            partida.aplicarRecompensa(recompensa);
            List<String> desbloqueados = desbloquearAliados(ubicacion);
            partida.completarUbicacion(ubicacion);
            ultimoResultado = null;
            ultimoMensaje = "Ubicación completada: " + ubicacion.getNombre()
                    + "."
                    + (desbloqueados.isEmpty() ? "" : " Aliados desbloqueados: " + desbloqueados);
            return true;
        }

        Batalla batalla = fabrica.crearBatalla(ubicacion, partida.getParty());
        // Solo cambia la cantidad disponible, no la definición del consumible.
        consumiblesAntesBatalla.clear();
        consumiblesAntesBatalla.putAll(partida.getInventario().getCantidadesConsumibles());
        partida.iniciarCombate();
        if (!controladorBatalla.iniciarBatalla(batalla, partida.getInventario())) {
            partida.terminarCombate();
            consumiblesAntesBatalla.clear();
            return rechazar("No se pudo iniciar la batalla.");
        }
        ultimoResultado = null;
        ultimoMensaje = entrenamiento ? "Entrenamiento iniciado." : "Batalla iniciada.";
        return true;
    }

    /** Ofrece candidatos a la UI sin pagar recursos ni ejecutar la acción elegida. */
    public List<PersonajeDTO> objetivosValidos(Accion accion, String nombreHabilidad, String itemId) {
        Batalla batalla = controladorBatalla.getBatalla();
        if (batalla == null || accion == null
                || batalla.verificarResultado() != EstadoBatalla.EN_CURSO
                || !(batalla.obtenerActorActual() instanceof Aliado)
                || (nombreHabilidad != null && itemId != null)) return List.of();
        Habilidad habilidad = resolverHabilidad(nombreHabilidad);
        Consumible item = resolverConsumible(itemId);
        if ((nombreHabilidad != null && habilidad == null) || (itemId != null && item == null)
                || (accion instanceof UsarHabilidad && habilidad == null)
                || (accion instanceof UsarItem && item == null)
                || accion instanceof Defender) return List.of();
        Entidad actor = batalla.obtenerActorActual();
        ContextoAccion contexto = new ContextoAccion(actor, new Entidad[0], item, habilidad);
        Entidad[] objetivos = batalla.objetivosValidos(actor,
                accion.afectaAliados(contexto), accion.permiteDistancia(contexto));
        List<PersonajeDTO> candidatos = new ArrayList<>();
        for (Entidad objetivo : objetivos) {
            candidatos.add(MapeadorDTO.personaje(objetivo, batalla.idDe(objetivo),
                    batalla.posicionDe(objetivo)));
        }
        return List.copyOf(candidatos);
    }

    /** Los IDs de la UI se resuelven a los objetos reales antes de delegar. */
    public boolean ejecutarAccion(Accion accion, String[] objetivos,
            String nombreHabilidad, String itemId) {
        Batalla batalla = controladorBatalla.getBatalla();
        if (partida == null || !partida.hayCombateActivo() || batalla == null || objetivos == null) return false;
        Entidad[] entidades = new Entidad[objetivos.length];
        for (int i = 0; i < objetivos.length; i++) {
            entidades[i] = batalla.buscarParticipante(objetivos[i]);
            if (entidades[i] == null) return rechazar("Objetivo inexistente.");
        }
        Habilidad habilidad = resolverHabilidad(nombreHabilidad);
        Consumible item = resolverConsumible(itemId);
        if ((nombreHabilidad != null && habilidad == null) || (itemId != null && item == null)) {
            return rechazar("La habilidad o el consumible no están disponibles.");
        }
        boolean ejecutada = controladorBatalla.ejecutarAccion(accion, entidades, habilidad, item);
        ultimoMensaje = ejecutada ? batalla.getMensaje() : "La acción no es válida o faltan recursos.";
        return ejecutada;
    }

    public void finalizarTurno() { controladorBatalla.finalizarTurno(); }

    public void finalizarBatalla() {
        Batalla batalla = controladorBatalla.getBatalla();
        if (batalla == null) return; // Una segunda solicitud no vuelve a premiar.
        EstadoBatalla resultado = batalla.verificarResultado();
        if (resultado != EstadoBatalla.VICTORIA && resultado != EstadoBatalla.DERROTA) {
            throw new IllegalStateException("La batalla todavía no terminó");
        }
        UbicacionMapa ubicacion = batalla.getUbicacion();
        boolean entrenamiento = ubicacion.getEstado() == EstadoUbicacion.COMPLETADA;
        Aliado[] participantes = batalla.aliados();

        if (resultado == EstadoBatalla.DERROTA) {
            controladorBatalla.descartarBatalla();
            restaurarIntento(participantes);
            ultimoMensaje = "Derrota. Se devolvieron los objetos y los aliados recuperaron todos sus recursos.";
            ultimoResultado = new ResultadoBatallaDTO(resultado, ubicacion.getId(), ubicacion.getNombre(),
                    entrenamiento, 0, List.of(), List.of(), List.of(), true,
                    partida.estaCompletada(), ultimoMensaje);
            return;
        }

        // El arreglo mantiene el mismo orden que los participantes.
        int[] nivelesAnteriores = new int[participantes.length];
        for (int i = 0; i < participantes.length; i++) {
            nivelesAnteriores[i] = participantes[i].getNivel();
        }
        Recompensa recompensa = controladorBatalla.finalizarBatalla();
        if (recompensa == null) throw new IllegalStateException("La recompensa ya fue retirada del encuentro");
        partida.terminarCombate();
        partida.aplicarRecompensa(recompensa, participantes);
        List<String> desbloqueados = entrenamiento ? List.of() : desbloquearAliados(ubicacion);
        partida.completarUbicacion(ubicacion);
        List<ResultadoBatallaDTO.ProgresoAliadoDTO> progreso = new ArrayList<>();
        for (int i = 0; i < participantes.length; i++) {
            Aliado aliado = participantes[i];
            progreso.add(new ResultadoBatallaDTO.ProgresoAliadoDTO(
                    "aliado:" + aliado.getIdPersonaje(), aliado.getNombre(), nivelesAnteriores[i],
                    aliado.getNivel()));
        }
        List<ItemDTO> items = new ArrayList<>();
        for (Item item : recompensa.getItems()) items.add(MapeadorDTO.item(item, recompensa.getCantidad(item)));

        controladorBatalla.descartarBatalla();
        // Al ganar, los objetos consumidos siguen gastados.
        consumiblesAntesBatalla.clear();
        ultimoMensaje = (entrenamiento ? "Entrenamiento ganado." : "Victoria.")
                + " Progreso conservado solo durante esta sesión.";
        ultimoResultado = new ResultadoBatallaDTO(resultado, ubicacion.getId(), ubicacion.getNombre(),
                entrenamiento, recompensa.getExperiencia(), items,
                progreso, desbloqueados, false, partida.estaCompletada(), ultimoMensaje);
    }

    public boolean reintentarBatalla() {
        return ultimoResultado != null && ultimoResultado.getResultado() == EstadoBatalla.DERROTA
                && iniciarUbicacion(ultimoResultado.getUbicacionId());
    }

    public boolean abandonarBatalla() {
        Batalla batalla = controladorBatalla.getBatalla();
        if (batalla == null) return false;
        Aliado[] participantes = batalla.aliados();
        // No se alteran archivos ni se otorgan premios al abandonar.
        controladorBatalla.abandonarBatalla();
        restaurarIntento(participantes);
        ultimoResultado = null;
        ultimoMensaje = "Intento abandonado. Objetos devueltos y aliados con recursos completos.";
        return true;
    }

    private Aliado resolverAliado(String id) {
        if (partida == null || id == null) return null;
        return partida.getParty().buscarPorId(id.startsWith("aliado:") ? id.substring(7) : id);
    }

    private Habilidad resolverHabilidad(String nombre) {
        Batalla batalla = controladorBatalla.getBatalla();
        if (nombre == null || batalla == null || batalla.obtenerActorActual() == null) return null;
        for (Habilidad habilidad : batalla.obtenerActorActual().habilidadesDisponibles()) {
            if (habilidad.getNombre().equals(nombre)) return habilidad;
        }
        return null;
    }

    private Consumible resolverConsumible(String id) {
        if (partida == null || id == null) return null;
        Item item = partida.getInventario().buscarPorId(id);
        return item instanceof Consumible consumible ? consumible : null;
    }

    private List<String> desbloquearAliados(UbicacionMapa ubicacion) {
        List<String> ids = new ArrayList<>();
        for (String id : ubicacion.getAliadosDesbloqueados()) {
            if (partida.getParty().buscarPorId(id) == null) {
                partida.getParty().desbloquearAliado(fabrica.crearAliado(id));
                ids.add(id);
            }
        }
        return List.copyOf(ids);
    }

    private void restaurarIntento(Aliado[] participantes) {
        partida.terminarCombate();
        partida.getInventario().restaurarConsumibles(consumiblesAntesBatalla);
        for (Aliado aliado : participantes) {
            // Recupera vida/maná y reinicia PA, iniciativa, estados y pasivas temporales.
            aliado.prepararParaBatalla();
        }
        consumiblesAntesBatalla.clear();
    }

    private boolean fueraDeBatalla() {
        return partida != null && !partida.hayCombateActivo() && controladorBatalla.getBatalla() == null;
    }

    private void exigirSinBatalla() {
        if (controladorBatalla.getBatalla() != null) {
            throw new IllegalStateException("Procesá o abandoná primero la batalla actual");
        }
    }

    private boolean rechazar(String mensaje) { ultimoMensaje = mensaje; return false; }
}
