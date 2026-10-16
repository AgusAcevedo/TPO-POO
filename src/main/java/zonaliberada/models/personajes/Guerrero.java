package zonaliberada.models.personajes;

import java.util.List;

import zonaliberada.models.combate.Habilidad;

/** Acumula furia al recibir daño y la convierte en bonus porcentual de ataque. */
public class Guerrero extends Aliado {

    private static final int FURIA_MAXIMA = 50;
    private static final int FURIA_POR_DANIO_RECIBIDO = 10;

    private int furia;

    public Guerrero(
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

        super(idPersonaje, nombre, vidaMaxima, manaMaximo,
                ataqueBase, defensaBase, velocidadBase, iniciativa,
                habilidades, ataqueDistancia, nivel, experiencia);

        // La furia es temporal: un guerrero nuevo siempre comienza en cero.
    }

    @Override
    public TipoClase tipoClase() {
        return TipoClase.GUERRERO;
    }

    public int getFuria() {
        return furia;
    }

    @Override
    public void prepararParaBatalla() {
        super.prepararParaBatalla();
        furia = 0;
    }

    @Override
    public double ataqueTotal() {
        double ataqueSinFuria = super.ataqueTotal();

        double bonificacion = ataqueSinFuria * furia / 100.0;
        return ataqueSinFuria + bonificacion;
    }

    @Override
    public void recibirDanio(int danio) {
        int vidaAnterior = getVida();

        super.recibirDanio(danio);

        // Solo gana furia si perdió vida y sobrevivió.
        if (estaVivo() && getVida() < vidaAnterior) {
            furia = Math.min(FURIA_MAXIMA, furia + FURIA_POR_DANIO_RECIBIDO);
        }
    }

}
