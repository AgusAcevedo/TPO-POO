package zonaliberada.ui;

/** El selector muestra texto, pero las solicitudes siempre envían el ID real. */
public final class OpcionUI {
    private final String id;
    private final String texto;

    public OpcionUI(
            String id,
            String texto) {
        this.id = id;
        this.texto = texto;
    }

    public String getId() { return id; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof OpcionUI)) return false;
        OpcionUI otro = (OpcionUI) objeto;
        return java.util.Objects.equals(id, otro.id)
                && java.util.Objects.equals(texto, otro.texto);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, texto);
    }

    // JComboBox llama a toString para decidir qué texto mostrar en cada opción.
    @Override public String toString() { return texto; }
}
