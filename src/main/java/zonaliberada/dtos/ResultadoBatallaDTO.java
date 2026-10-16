package zonaliberada.dtos;
import java.util.List;
import zonaliberada.models.combate.EstadoBatalla;
/** Resume un resultado ya procesado: mostrarlo no vuelve a entregar sus premios. */
public final class ResultadoBatallaDTO {
    private final EstadoBatalla resultado;
    private final String ubicacionId;
    private final String ubicacionNombre;
    private final boolean entrenamiento;
    private final int experienciaPorParticipante;
    private final List<ItemDTO> items;
    private final List<ProgresoAliadoDTO> aliados;
    private final List<String> desbloqueados;
    private final boolean inventarioRestaurado;
    private final boolean partidaCompletada;
    private final String mensaje;

    public ResultadoBatallaDTO(
            EstadoBatalla resultado,
            String ubicacionId,
            String ubicacionNombre,
            boolean entrenamiento,
            int experienciaPorParticipante,
            List<ItemDTO> items,
            List<ProgresoAliadoDTO> aliados,
            List<String> desbloqueados,
            boolean inventarioRestaurado,
            boolean partidaCompletada,
            String mensaje) {
        items = List.copyOf(items);
        aliados = List.copyOf(aliados);
        desbloqueados = List.copyOf(desbloqueados);
        this.resultado = resultado;
        this.ubicacionId = ubicacionId;
        this.ubicacionNombre = ubicacionNombre;
        this.entrenamiento = entrenamiento;
        this.experienciaPorParticipante = experienciaPorParticipante;
        this.items = items;
        this.aliados = aliados;
        this.desbloqueados = desbloqueados;
        this.inventarioRestaurado = inventarioRestaurado;
        this.partidaCompletada = partidaCompletada;
        this.mensaje = mensaje;
    }

    public EstadoBatalla getResultado() { return resultado; }
    public String getUbicacionId() { return ubicacionId; }
    public String getUbicacionNombre() { return ubicacionNombre; }
    public boolean isEntrenamiento() { return entrenamiento; }
    public int getExperienciaPorParticipante() { return experienciaPorParticipante; }
    public List<ItemDTO> getItems() { return items; }
    public List<ProgresoAliadoDTO> getAliados() { return aliados; }
    public List<String> getDesbloqueados() { return desbloqueados; }
    public boolean isInventarioRestaurado() { return inventarioRestaurado; }
    public boolean isPartidaCompletada() { return partidaCompletada; }
    public String getMensaje() { return mensaje; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof ResultadoBatallaDTO)) return false;
        ResultadoBatallaDTO otro = (ResultadoBatallaDTO) objeto;
        return java.util.Objects.equals(resultado, otro.resultado)
                && java.util.Objects.equals(ubicacionId, otro.ubicacionId)
                && java.util.Objects.equals(ubicacionNombre, otro.ubicacionNombre)
                && entrenamiento == otro.entrenamiento
                && experienciaPorParticipante == otro.experienciaPorParticipante
                && java.util.Objects.equals(items, otro.items)
                && java.util.Objects.equals(aliados, otro.aliados)
                && java.util.Objects.equals(desbloqueados, otro.desbloqueados)
                && inventarioRestaurado == otro.inventarioRestaurado
                && partidaCompletada == otro.partidaCompletada
                && java.util.Objects.equals(mensaje, otro.mensaje);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                resultado, ubicacionId, ubicacionNombre, entrenamiento,
                experienciaPorParticipante, items, aliados,
                desbloqueados, inventarioRestaurado, partidaCompletada, mensaje);
    }

    @Override
    public String toString() {
        return "ResultadoBatallaDTO["
                + "resultado=" + resultado
                + ", ubicacionId=" + ubicacionId
                + ", ubicacionNombre=" + ubicacionNombre
                + ", entrenamiento=" + entrenamiento
                + ", experienciaPorParticipante=" + experienciaPorParticipante
                + ", items=" + items
                + ", aliados=" + aliados
                + ", desbloqueados=" + desbloqueados
                + ", inventarioRestaurado=" + inventarioRestaurado
                + ", partidaCompletada=" + partidaCompletada
                + ", mensaje=" + mensaje + "]";
    }

    // Conserva ambos niveles para mostrar la subida sin volver a calcular experiencia.
    public static final class ProgresoAliadoDTO {
        private final String id;
        private final String nombre;
        private final int nivelAnterior;
        private final int nivelActual;

        public ProgresoAliadoDTO(
                String id,
                String nombre,
                int nivelAnterior,
                int nivelActual) {
            this.id = id;
            this.nombre = nombre;
            this.nivelAnterior = nivelAnterior;
            this.nivelActual = nivelActual;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public int getNivelAnterior() { return nivelAnterior; }
        public int getNivelActual() { return nivelActual; }

        // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
        @Override
        public boolean equals(Object objeto) {
            if (this == objeto) return true;
            if (!(objeto instanceof ProgresoAliadoDTO)) return false;
            ProgresoAliadoDTO otro = (ProgresoAliadoDTO) objeto;
            return java.util.Objects.equals(id, otro.id)
                    && java.util.Objects.equals(nombre, otro.nombre)
                    && nivelAnterior == otro.nivelAnterior
                    && nivelActual == otro.nivelActual;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(id, nombre, nivelAnterior, nivelActual);
        }

        @Override
        public String toString() {
            return "ProgresoAliadoDTO["
                    + "id=" + id
                    + ", nombre=" + nombre
                    + ", nivelAnterior=" + nivelAnterior
                    + ", nivelActual=" + nivelActual + "]";
        }

    }
}
