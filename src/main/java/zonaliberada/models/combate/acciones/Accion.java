package zonaliberada.models.combate.acciones;

public abstract class Accion {
    protected Accion() {
    }

    public int costoPA(ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public int costoMana(ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean permiteDistancia(ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public abstract boolean validar(ContextoAccion contexto);

    public abstract void ejecutar(ContextoAccion contexto);
}
