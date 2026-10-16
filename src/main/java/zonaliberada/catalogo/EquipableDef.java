package zonaliberada.catalogo;

import java.util.List;
import zonaliberada.models.inventario.TipoEquipable;
import zonaliberada.models.personajes.TipoClase;

/** Valores comunes de un equipable; cada pieza creada recibe su propio ID. */
public final class EquipableDef {
    private final String nombre;
    private final TipoEquipable tipo;
    private final int ataque;
    private final int defensa;
    private final int vida;
    private final int mana;
    private final int velocidad;
    private final int precision;
    private final List<TipoClase> clases;

    public EquipableDef(String nombre, TipoEquipable tipo, int ataque, int defensa,
            int vida, int mana, int velocidad, int precision, List<TipoClase> clases) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ataque = ataque;
        this.defensa = defensa;
        this.vida = vida;
        this.mana = mana;
        this.velocidad = velocidad;
        this.precision = precision;
        this.clases = List.copyOf(clases);
    }

    public String getNombre() { return nombre; }
    public TipoEquipable getTipo() { return tipo; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVida() { return vida; }
    public int getMana() { return mana; }
    public int getVelocidad() { return velocidad; }
    public int getPrecision() { return precision; }
    public List<TipoClase> getClases() { return clases; }
}
