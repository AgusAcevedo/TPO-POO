package zonaliberada.models.combate.acciones;

public class UsarHabilidad extends Accion {
    public UsarHabilidad() {
    }

    @Override
    public int costoPA(ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public int costoMana(ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public boolean permiteDistancia(ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public boolean validar(ContextoAccion contexto) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @Override
    public void ejecutar(ContextoAccion contexto) {
    }
}
