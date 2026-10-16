package zonaliberada.factories;

import zonaliberada.catalogo.CatalogoJuego;
import zonaliberada.catalogo.EnemigoDef;
import zonaliberada.models.personajes.*;

/** Cada llamada crea un enemigo con recursos completos e independiente de los demás. */
public final class EnemigoFactory {
    public Enemigo crear(TipoEnemigo tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de enemigo es obligatorio");
        EnemigoDef definicion = CatalogoJuego.getInstancia().buscarEnemigo(tipo);
        return new Enemigo(definicion.getNombre(), definicion.getVidaMaxima(), 40,
                definicion.getAtaque(), definicion.getDefensa(), definicion.getVelocidad(),
                0, definicion.getHabilidades(), false, tipo);
    }
}
