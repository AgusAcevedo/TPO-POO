package zonaliberada.models;

import java.util.HashSet;
import java.util.Set;
import zonaliberada.models.combate.Recompensa;
import zonaliberada.models.inventario.Consumible;
import zonaliberada.models.inventario.Equipable;
import zonaliberada.models.inventario.Inventario;
import zonaliberada.models.inventario.Item;
import zonaliberada.models.mapa.Mapa;
import zonaliberada.models.mapa.UbicacionMapa;
import zonaliberada.models.personajes.Aliado;

/** Estado de la sesión del juego. No contiene una batalla ni componentes Swing. */
public class Partida {
    private final Party party;
    private final Inventario inventario;
    private final Mapa mapa;
    // El conjunto recuerda cada premio aplicado para impedir entregas repetidas.
    private final Set<String> recompensasAplicadas = new HashSet<>();
    // El intento sigue pendiente hasta procesar el resultado, aunque ya no haya turnos.
    private boolean combateActivo;

    public Partida(Party party, Inventario inventario, Mapa mapa) {
        if (party == null || inventario == null || mapa == null) {
            throw new IllegalArgumentException("Party, inventario y mapa son obligatorios");
        }
        this.party = party;
        this.inventario = inventario;
        this.mapa = mapa;
        if (party.hayCombateActivo()) {
            throw new IllegalStateException("Esta operación solo se permite fuera de combate");
        }
        // Los constructores ya validaron los datos de cada objeto.
        // Aquí comprobamos que ninguna unidad tenga dos propietarios.
        idsDeItemsPropios();
    }

    public Party getParty() { return party; }
    public Inventario getInventario() { return inventario; }
    public Mapa getMapa() { return mapa; }
    public boolean hayCombateActivo() { return combateActivo || party.hayCombateActivo(); }
    public boolean estaCompletada() { return mapa.estaCompletado(); }

    public boolean editarFormacion(int numero, Aliado aliado) {
        return !hayCombateActivo() && party.editarFormacion(numero, aliado);
    }

    public boolean quitarDeFormacion(Aliado aliado) {
        return !hayCombateActivo() && party.quitarDeFormacion(aliado);
    }

    public boolean equipar(Aliado aliado, Equipable item) {
        if (hayCombateActivo() || aliado == null || item == null) return false;
        Aliado propio = party.buscarPorId(aliado.getIdPersonaje());
        if (propio == null || !inventario.contiene(item) || !item.esCompatible(propio)) return false;
        Equipable anterior = propio.equipadoEn(item.getTipo());
        if (anterior != null && !inventario.puedeAgregar(anterior)) return false;

        // Cambiar un objeto devuelve el anterior al inventario.
        if (!propio.reemplazarEquipable(item)) return false;
        inventario.retirarItem(item);
        if (anterior != null) inventario.agregarItem(anterior);
        return true;
    }

    public boolean desequipar(Aliado aliado, Equipable item) {
        if (hayCombateActivo() || aliado == null || item == null) return false;
        Aliado propio = party.buscarPorId(aliado.getIdPersonaje());
        if (propio == null || !propio.tieneEquipado(item) || !inventario.puedeAgregar(item)) return false;
        if (!propio.desequipar(item)) return false;
        inventario.agregarItem(item);
        return true;
    }
    public boolean puedeIniciar(UbicacionMapa ubicacion) {
        return !hayCombateActivo() && mapa.puedeAcceder(ubicacion)
                && (!ubicacion.esCombate() || party.formacionValida());
    }

    public void iniciarCombate() {
        if (hayCombateActivo()) throw new IllegalStateException("Ya hay un combate activo");
        combateActivo = true;
    }

    public void terminarCombate() { combateActivo = false; }

    public void completarUbicacion(UbicacionMapa ubicacion) {
        exigirFueraDeCombate();
        if (!mapa.puedeAcceder(ubicacion)) {
            throw new IllegalArgumentException("La ubicación no está disponible en esta partida");
        }
        // Entrenamiento: completar otra vez no altera el recorrido.
        ubicacion.completar();
        mapa.actualizarProgreso();
    }

    public void aplicarRecompensa(Recompensa recompensa) {
        aplicarRecompensa(recompensa, party.seleccionados());
    }

    public void aplicarRecompensa(Recompensa recompensa, Aliado[] participantes) {
        exigirFueraDeCombate();
        if (recompensa == null || participantes == null) {
            throw new IllegalArgumentException("Recompensa y participantes son obligatorios");
        }
        if (recompensasAplicadas.contains(recompensa.getId())) return;

        // Validamos todo antes de cambiar XP, recursos o inventario.
        Set<String> ids = new HashSet<>();
        for (Aliado aliado : participantes) {
            if (aliado == null || party.buscarPorId(aliado.getIdPersonaje()) != aliado
                    || aliado.estaEnBatalla() || !ids.add(aliado.getIdPersonaje())) {
                throw new IllegalArgumentException("Los participantes deben pertenecer a la partida");
            }
        }
        Set<String> unidades = idsDeItemsPropios();
        for (Item item : recompensa.getItems()) {
            boolean tipoDisponible = item instanceof Consumible
                    && inventario.contiene(item);
            if (!inventario.puedeAgregar(item, recompensa.getCantidad(item))
                    || (unidades.contains(item.getId()) && !tipoDisponible)) {
                throw new IllegalArgumentException("Una unidad premiada ya pertenece a la partida");
            }
        }

        // Incluye KO: todos los participantes reciben la misma XP.
        // curar() no revive y no modifica a las reservas.
        for (Aliado aliado : participantes) {
            aliado.ganarExperiencia(recompensa.getExperiencia());
            aliado.curar(recompensa.getCuracion());
        }
        for (Item item : recompensa.getItems()) inventario.agregarItem(item, recompensa.getCantidad(item));
        recompensasAplicadas.add(recompensa.getId());
    }

    private void exigirFueraDeCombate() {
        if (hayCombateActivo()) {
            throw new IllegalStateException("Esta operación solo se permite fuera de combate");
        }
    }

    private Set<String> idsDeItemsPropios() {
        Set<String> ids = new HashSet<>();
        for (Item item : inventario.getItems()) registrarItem(ids, item);
        for (Aliado aliado : party.getRoster()) {
            for (Equipable item : aliado.getEquipados()) registrarItem(ids, item);
        }
        return ids;
    }

    private static void registrarItem(Set<String> ids, Item item) {
        if (!ids.add(item.getId())) {
            throw new IllegalStateException("Una unidad de ítem no puede tener dos propietarios");
        }
    }
}
