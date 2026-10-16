package zonaliberada.dtos;
import java.util.List;
import zonaliberada.models.mapa.*;
/** Los DTO anidados reflejan la estructura mapa, zonas y ubicaciones para la vista. */
public final class MapaDTO {
    private final int indiceZonaActual;
    private final boolean partidaCompletada;
    private final List<ZonaDTO> zonas;

    public MapaDTO(
            int indiceZonaActual,
            boolean partidaCompletada,
            List<ZonaDTO> zonas) {
        zonas = List.copyOf(zonas);
        this.indiceZonaActual = indiceZonaActual;
        this.partidaCompletada = partidaCompletada;
        this.zonas = zonas;
    }

    public int getIndiceZonaActual() { return indiceZonaActual; }
    public boolean isPartidaCompletada() { return partidaCompletada; }
    public List<ZonaDTO> getZonas() { return zonas; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof MapaDTO)) return false;
        MapaDTO otro = (MapaDTO) objeto;
        return indiceZonaActual == otro.indiceZonaActual
                && partidaCompletada == otro.partidaCompletada
                && java.util.Objects.equals(zonas, otro.zonas);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(indiceZonaActual, partidaCompletada, zonas);
    }

    @Override
    public String toString() {
        return "MapaDTO["
                + "indiceZonaActual=" + indiceZonaActual
                + ", partidaCompletada=" + partidaCompletada
                + ", zonas=" + zonas + "]";
    }

    public static final class ZonaDTO {
        private final String nombre;
        private final boolean desbloqueada;
        private final boolean completada;
        private final List<UbicacionDTO> ubicaciones;

        public ZonaDTO(
                String nombre,
                boolean desbloqueada,
                boolean completada,
                List<UbicacionDTO> ubicaciones) {
            ubicaciones = List.copyOf(ubicaciones);
            this.nombre = nombre;
            this.desbloqueada = desbloqueada;
            this.completada = completada;
            this.ubicaciones = ubicaciones;
        }

        public String getNombre() { return nombre; }
        public boolean isDesbloqueada() { return desbloqueada; }
        public boolean isCompletada() { return completada; }
        public List<UbicacionDTO> getUbicaciones() { return ubicaciones; }

        // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
        @Override
        public boolean equals(Object objeto) {
            if (this == objeto) return true;
            if (!(objeto instanceof ZonaDTO)) return false;
            ZonaDTO otro = (ZonaDTO) objeto;
            return java.util.Objects.equals(nombre, otro.nombre)
                    && desbloqueada == otro.desbloqueada
                    && completada == otro.completada
                    && java.util.Objects.equals(ubicaciones, otro.ubicaciones);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(nombre, desbloqueada, completada, ubicaciones);
        }

        @Override
        public String toString() {
            return "ZonaDTO["
                    + "nombre=" + nombre
                    + ", desbloqueada=" + desbloqueada
                    + ", completada=" + completada
                    + ", ubicaciones=" + ubicaciones + "]";
        }

    }
    public static final class UbicacionDTO {
        private final String id;
        private final String nombre;
        private final TipoUbicacion tipo;
        private final EstadoUbicacion estado;
        private final boolean repetible;
        private final boolean puedeIniciarse;
        private final int experiencia;
        private final List<String> items;
        private final List<String> aliadosDesbloqueados;

        public UbicacionDTO(
                String id,
                String nombre,
                TipoUbicacion tipo,
                EstadoUbicacion estado,
                boolean repetible,
                boolean puedeIniciarse,
                int experiencia,
                List<String> items,
                List<String> aliadosDesbloqueados) {
            items = List.copyOf(items);
            aliadosDesbloqueados = List.copyOf(aliadosDesbloqueados);
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.estado = estado;
            this.repetible = repetible;
            this.puedeIniciarse = puedeIniciarse;
            this.experiencia = experiencia;
            this.items = items;
            this.aliadosDesbloqueados = aliadosDesbloqueados;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public TipoUbicacion getTipo() { return tipo; }
        public EstadoUbicacion getEstado() { return estado; }
        public boolean isRepetible() { return repetible; }
        public boolean isPuedeIniciarse() { return puedeIniciarse; }
        public int getExperiencia() { return experiencia; }
        public List<String> getItems() { return items; }
        public List<String> getAliadosDesbloqueados() { return aliadosDesbloqueados; }

        // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
        @Override
        public boolean equals(Object objeto) {
            if (this == objeto) return true;
            if (!(objeto instanceof UbicacionDTO)) return false;
            UbicacionDTO otro = (UbicacionDTO) objeto;
            return java.util.Objects.equals(id, otro.id)
                    && java.util.Objects.equals(nombre, otro.nombre)
                    && java.util.Objects.equals(tipo, otro.tipo)
                    && java.util.Objects.equals(estado, otro.estado)
                    && repetible == otro.repetible
                    && puedeIniciarse == otro.puedeIniciarse
                    && experiencia == otro.experiencia
                    && java.util.Objects.equals(items, otro.items)
                    && java.util.Objects.equals(aliadosDesbloqueados, otro.aliadosDesbloqueados);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(
                    id, nombre, tipo, estado, repetible, puedeIniciarse, experiencia, items,
                    aliadosDesbloqueados);
        }

        @Override
        public String toString() {
            return "UbicacionDTO["
                    + "id=" + id
                    + ", nombre=" + nombre
                    + ", tipo=" + tipo
                    + ", estado=" + estado
                    + ", repetible=" + repetible
                    + ", puedeIniciarse=" + puedeIniciarse
                    + ", experiencia=" + experiencia
                    + ", items=" + items
                    + ", aliadosDesbloqueados=" + aliadosDesbloqueados + "]";
        }

    }
}
