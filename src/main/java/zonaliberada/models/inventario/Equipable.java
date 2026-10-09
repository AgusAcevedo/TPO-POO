package zonaliberada.models.inventario;

import java.util.List;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.models.personajes.TipoClase;

public class Equipable extends Item {
    private TipoEquipable tipo;
    private int bonusAtaque;
    private int bonusDefensa;
    private int bonusVida;
    private int bonusMana;
    private List<TipoClase> clasesPermitidas;

    public Equipable() {
    }

    public Equipable(
            TipoEquipable tipo,
            int bonusAtaque,
            int bonusDefensa,
            int bonusVida,
            int bonusMana,
            List<TipoClase> clasesPermitidas) {
    }

    public boolean esCompatible(Aliado aliado) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
