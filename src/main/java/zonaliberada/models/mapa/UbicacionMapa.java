package zonaliberada.models.mapa;

import java.util.List;
import zonaliberada.models.ReglasJuego;
import zonaliberada.models.personajes.TipoEnemigo;

/** Configuración de una ubicación y su progreso; no crea enemigos ni ítems. */
public class UbicacionMapa {
    private final String id, nombre;
    private final TipoUbicacion tipo;
    private EstadoUbicacion estado;
    private final boolean repetible;
    private final List<TipoEnemigo> enemigos;
    private final String bossId;
    private final int experiencia, curacion;
    private final List<String> itemsRecompensa, aliadosDesbloqueados;

    public UbicacionMapa(String id, String nombre, TipoUbicacion tipo,
            EstadoUbicacion estado, boolean repetible, List<TipoEnemigo> enemigos,
            String bossId, int experiencia, List<String> itemsRecompensa,
            int curacion, List<String> aliadosDesbloqueados) {
        if (id == null || id.isBlank() || nombre == null || nombre.isBlank()
                || tipo == null || estado == null || enemigos == null
                || itemsRecompensa == null || aliadosDesbloqueados == null
                || experiencia < 0 || curacion < 0
                || enemigos.size() > ReglasJuego.CANTIDAD_POSICIONES || !idsValidos(itemsRecompensa)
                || !idsValidos(aliadosDesbloqueados)) {
            throw new IllegalArgumentException("Configuración de ubicación inválida");
        }
        for (TipoEnemigo enemigo : enemigos) {
            if (enemigo == null) throw new IllegalArgumentException("Los enemigos no pueden contener null");
        }
        boolean boss = tipo == TipoUbicacion.BOSS_ZONA || tipo == TipoUbicacion.BOSS_FINAL;
        if ((boss && (bossId == null || bossId.isBlank()))
                || (!boss && bossId != null) || (repetible && tipo != TipoUbicacion.BATALLA)
                || (tipo == TipoUbicacion.BATALLA && enemigos.isEmpty())
                || (boss && enemigos.size() >= ReglasJuego.CANTIDAD_POSICIONES)
                || (!boss && tipo != TipoUbicacion.BATALLA && !enemigos.isEmpty())) {
            throw new IllegalArgumentException("La actividad no coincide con su configuración");
        }
        this.id = id.strip();
        this.nombre = nombre.strip();
        this.tipo = tipo;
        this.estado = estado;
        this.repetible = repetible;
        this.enemigos = List.copyOf(enemigos);
        this.bossId = bossId;
        this.experiencia = experiencia;
        this.itemsRecompensa = List.copyOf(itemsRecompensa);
        this.curacion = curacion;
        this.aliadosDesbloqueados = List.copyOf(aliadosDesbloqueados);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoUbicacion getTipo() { return tipo; }
    public EstadoUbicacion getEstado() { return estado; }
    public boolean esRepetible() { return repetible; }
    public List<TipoEnemigo> getEnemigos() { return enemigos; }
    public String getBossId() { return bossId; }
    public int getExperiencia() { return experiencia; }
    public int getCuracion() { return curacion; }
    public List<String> getItemsRecompensa() { return itemsRecompensa; }
    public List<String> getAliadosDesbloqueados() { return aliadosDesbloqueados; }

    public boolean esCombate() {
        return tipo == TipoUbicacion.BATALLA || tipo == TipoUbicacion.BOSS_ZONA
                || tipo == TipoUbicacion.BOSS_FINAL;
    }

    public boolean puedeIniciarse() {
        return estado == EstadoUbicacion.DISPONIBLE
                || (estado == EstadoUbicacion.COMPLETADA && repetible);
    }

    public void habilitar() {
        if (estado == EstadoUbicacion.BLOQUEADA) estado = EstadoUbicacion.DISPONIBLE;
    }

    public void completar() {
        if (estado == EstadoUbicacion.BLOQUEADA) {
            throw new IllegalStateException("No se puede completar una ubicación bloqueada");
        }
        estado = EstadoUbicacion.COMPLETADA;
    }

    private static boolean idsValidos(List<String> ids) {
        for (String id : ids) {
            if (id == null || id.isBlank()) return false;
        }
        return true;
    }

}
