package zonaliberada.models.personajes;

import java.util.List;

import zonaliberada.models.combate.Habilidad;

/** Gana concentración al iniciar turnos; recibir daño reduce ese bonus de ataque. */
public class Mago extends Aliado {

    private static final int CONCENTRACION_MAXIMA = 50;
    private static final int CONCENTRACION_POR_TURNO = 10;
    private static final int CONCENTRACION_PERDIDA_POR_DANIO = 20;

    private int concentracion;

    public Mago(
            String idPersonaje,
            String nombre,
            int vidaMaxima,
            int manaMaximo,
            int ataqueBase,
            int defensaBase,
            int velocidadBase,
            int iniciativa,
            List<Habilidad> habilidades,
            boolean ataqueDistancia,
            int nivel,
            int experiencia) {

        // Aliado guarda y valida el identificador.
        super(idPersonaje, nombre, vidaMaxima, manaMaximo,
                ataqueBase, defensaBase, velocidadBase, iniciativa,
                habilidades, ataqueDistancia, nivel, experiencia);

        // La concentración se gana durante los turnos, no al crear al mago.
    }

    @Override
    public TipoClase tipoClase() {
        return TipoClase.MAGO;
    }

    public int getConcentracion() {
        return concentracion;
    }

    @Override
    public void prepararParaBatalla() {
        super.prepararParaBatalla();
        concentracion = 0;
    }

    @Override
    public boolean iniciarTurno() {
        if (!super.iniciarTurno()) {
            return false;
        }

        // El veneno puede derrotarlo al abrir el turno.
        if (!estaVivo()) return true;

        // Se gana concentración una sola vez por turno.
        concentracion = Math.min(
                CONCENTRACION_MAXIMA,
                concentracion + CONCENTRACION_POR_TURNO);

        return true;
    }

    @Override
    public double ataqueTotal() {
        double ataqueSinConcentracion = super.ataqueTotal();

        double bonificacion =
                ataqueSinConcentracion * concentracion / 100.0;

        return ataqueSinConcentracion + bonificacion;
    }

    @Override
    public void recibirDanio(int danio) {
        int vidaAnterior = getVida();

        super.recibirDanio(danio);

        // Solo pierde concentración si perdió vida.
        if (getVida() >= vidaAnterior) {
            return;
        }

        // Una derrota elimina toda la concentración.
        if (!estaVivo()) {
            concentracion = 0;
            return;
        }

        concentracion = Math.max(
                0, concentracion - CONCENTRACION_PERDIDA_POR_DANIO);
    }

}
