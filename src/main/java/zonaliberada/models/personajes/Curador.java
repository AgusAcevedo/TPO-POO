package zonaliberada.models.personajes;

import java.util.List;

import zonaliberada.models.combate.Habilidad;

/** Rol de apoyo: la curación y la limpieza de estados se definen en sus habilidades. */
public class Curador extends Aliado {

    public Curador(
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
    }

    @Override
    public TipoClase tipoClase() {
        return TipoClase.CURADOR;
    }
}
