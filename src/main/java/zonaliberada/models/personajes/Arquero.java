package zonaliberada.models.personajes;

import java.util.List;

import zonaliberada.models.combate.Habilidad;

/** La precisión mejora el daño y la probabilidad de crítico; combina base y equipo. */
public class Arquero extends Aliado {

    private static final int PRECISION_MAXIMA = 50;

    private int precisionBase;

    public Arquero(
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
            int experiencia,
            int precisionBase) {

        // Aliado guarda y valida el identificador.
        super(idPersonaje, nombre, vidaMaxima, manaMaximo,
                ataqueBase, defensaBase, velocidadBase, iniciativa,
                habilidades, ataqueDistancia, nivel, experiencia);

        if (precisionBase < 0 || precisionBase > PRECISION_MAXIMA) {
            throw new IllegalArgumentException(
                    "La precisión base debe estar entre 0 y "
                            + PRECISION_MAXIMA);
        }

        this.precisionBase = precisionBase;
    }

    @Override
    public TipoClase tipoClase() {
        return TipoClase.ARQUERO;
    }

    public int getPrecisionBase() {
        return precisionBase;
    }

    public int getPrecision() {
        return (int) Math.min(PRECISION_MAXIMA, (long) precisionBase + bonusPrecision());
    }

    @Override
    public double getProbabilidadCritico() {
        // Precisión 20: 20 %, expresado como 0.20.
        return getPrecision() / 100.0;
    }

    @Override
    public double ataqueTotal() {
        double ataqueSinPrecision = super.ataqueTotal();

        double bonificacion =
                ataqueSinPrecision * getPrecision() / 100.0;

        return ataqueSinPrecision + bonificacion;
    }

    public void mejorarPrecision(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException(
                    "La mejora de precisión no puede ser negativa");
        }

        // Mejora permanente; también se aplica al subir de nivel.
        // Limitamos antes de sumar para evitar overflow.
        int espacioHastaMaximo = PRECISION_MAXIMA - precisionBase;

        precisionBase += Math.min(cantidad, espacioHastaMaximo);
    }

    @Override
    protected void alSubirNivel() {
        mejorarPrecision(2);
    }
}
