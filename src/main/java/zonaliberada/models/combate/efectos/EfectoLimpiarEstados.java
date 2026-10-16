package zonaliberada.models.combate.efectos;

import zonaliberada.models.personajes.Entidad;

/** Alta médica elimina penalizaciones y conserva las bonificaciones. */
public final class EfectoLimpiarEstados extends EfectoCombate {

    @Override
    public String descripcion() { return "Elimina estados negativos y conserva los beneficios"; }

    @Override
    public String aplicar(Entidad usuario, Entidad objetivo) {
        if (usuario == null || objetivo == null) {
            throw new IllegalArgumentException("Usuario y objetivo son obligatorios");
        }
        if (!objetivo.estaVivo()) return "";
        int anteriores = objetivo.getEstados().size();
        objetivo.eliminarEstadosNegativos();
        return objetivo.getNombre() + ": " + (anteriores - objetivo.getEstados().size())
                + " estado(s) negativo(s) eliminado(s).";
    }
}
