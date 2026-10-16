package zonaliberada.models.combate.efectos;

import zonaliberada.models.personajes.Entidad;

public class EfectoCuracion extends EfectoCombate {

    private final int potencia;

    public EfectoCuracion(int potencia) {
        if (potencia <= 0) {
            throw new IllegalArgumentException(
                    "La potencia de curación debe ser positiva");
        }

        // Cantidad de vida que intentará recuperar.
        this.potencia = potencia;
    }

    @Override
    public String descripcion() { return "Recupera " + potencia + " de vida; no revive"; }

    @Override
    public String aplicar(Entidad usuario, Entidad objetivo) {
        if (usuario == null || objetivo == null) {
            throw new IllegalArgumentException(
                    "El usuario y el objetivo son obligatorios");
        }

        // Entidad ya controla la vida máxima y evita revivir derrotados.
        int vidaAnterior = objetivo.getVida();
        objetivo.curar(potencia);

        // El efecto no gasta PA ni maná.
        // Eso corresponde a ControladorTurno.
        return objetivo.getNombre() + " recuperó " + (objetivo.getVida() - vidaAnterior) + " de vida.";
    }
}
