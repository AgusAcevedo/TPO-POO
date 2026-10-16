package zonaliberada.factories;

import java.util.UUID;
import zonaliberada.catalogo.CatalogoJuego;
import zonaliberada.catalogo.EquipableDef;
import zonaliberada.models.inventario.*;

/** Usa el catálogo: comparte tipos de consumibles y crea piezas equipables nuevas. */
public final class ItemFactory {
    public Item crear(String tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de ítem es obligatorio");
        CatalogoJuego catalogo = CatalogoJuego.getInstancia();
        Consumible consumible = catalogo.buscarConsumible(tipo);
        if (consumible != null) return consumible;
        EquipableDef definicion = catalogo.buscarEquipable(tipo);
        if (definicion == null) throw new IllegalArgumentException("Ítem desconocido: " + tipo);
        return new Equipable(tipo + "-" + UUID.randomUUID(), definicion.getNombre(),
                "Equipable: " + definicion.getNombre(), definicion.getTipo(),
                definicion.getAtaque(), definicion.getDefensa(), definicion.getVida(),
                definicion.getMana(), definicion.getVelocidad(), definicion.getPrecision(),
                definicion.getClases());
    }
}
