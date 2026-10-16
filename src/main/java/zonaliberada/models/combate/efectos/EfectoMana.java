package zonaliberada.models.combate.efectos;

import zonaliberada.models.personajes.Entidad;

/** Cantidad positiva recupera maná; negativa lo drena, sin bajar de cero. */
public final class EfectoMana extends EfectoCombate {
    private final int cantidad;

    public EfectoMana(int cantidad) {
        // MIN_VALUE no puede negarse dentro de int; se excluye para drenar con seguridad.
        if (cantidad == 0 || cantidad == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("La cantidad de maná es inválida");
        }
        this.cantidad = cantidad;
    }

    @Override
    public String descripcion() {
        return cantidad > 0 ? "Recupera " + cantidad + " de maná"
                : "Drena " + (-cantidad) + " de maná";
    }

    @Override
    public String aplicar(Entidad usuario, Entidad objetivo) {
        if (usuario == null || objetivo == null) {
            throw new IllegalArgumentException("Usuario y objetivo son obligatorios");
        }
        if (!objetivo.estaVivo()) return "";
        int manaAnterior = objetivo.getMana();
        if (cantidad > 0) objetivo.recuperarMana(cantidad);
        else objetivo.gastarMana(Math.min(objetivo.getMana(), -cantidad));
        int diferencia = objetivo.getMana() - manaAnterior;
        return objetivo.getNombre() + (diferencia >= 0 ? " recuperó " : " perdió ")
                + Math.abs(diferencia) + " de maná.";
    }
}
