package zonaliberada.models.inventario;

import java.util.List;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.models.personajes.TipoClase;

/** Los bonus pertenecen al objeto; Aliado calcula sus valores efectivos. */
public class Equipable extends Item {
    private final TipoEquipable tipo;
    private final int bonusAtaque, bonusDefensa, bonusVida, bonusMana;
    private final int bonusVelocidad, bonusPrecision;
    private final List<TipoClase> clasesPermitidas;

    public Equipable(String id, String nombre, String descripcion, TipoEquipable tipo,
            int bonusAtaque, int bonusDefensa, int bonusVida, int bonusMana,
            int bonusVelocidad, int bonusPrecision, List<TipoClase> clasesPermitidas) {
        super(id, nombre, descripcion);
        if (tipo == null || clasesPermitidas == null) {
            throw new IllegalArgumentException("El tipo y las clases permitidas son obligatorios");
        }
        for (TipoClase clase : clasesPermitidas) {
            if (clase == null) throw new IllegalArgumentException("Las clases permitidas no pueden contener null");
        }
        if (bonusAtaque < 0 || bonusDefensa < 0 || bonusVida < 0 || bonusMana < 0
                || bonusVelocidad < 0 || bonusPrecision < 0) {
            throw new IllegalArgumentException("Las bonificaciones no pueden ser negativas");
        }
        this.tipo = tipo;
        this.bonusAtaque = bonusAtaque;
        this.bonusDefensa = bonusDefensa;
        this.bonusVida = bonusVida;
        this.bonusMana = bonusMana;
        this.bonusVelocidad = bonusVelocidad;
        this.bonusPrecision = bonusPrecision;
        this.clasesPermitidas = List.copyOf(clasesPermitidas);
    }

    public TipoEquipable getTipo() { return tipo; }
    public int getBonusAtaque() { return bonusAtaque; }
    public int getBonusDefensa() { return bonusDefensa; }
    public int getBonusVida() { return bonusVida; }
    public int getBonusMana() { return bonusMana; }
    public int getBonusVelocidad() { return bonusVelocidad; }
    public int getBonusPrecision() { return bonusPrecision; }
    public List<TipoClase> getClasesPermitidas() { return clasesPermitidas; }

    public boolean esCompatible(Aliado aliado) {
        // Lista vacía: objeto genérico compatible con todos los roles.
        return aliado != null && (clasesPermitidas.isEmpty()
                || clasesPermitidas.contains(aliado.tipoClase()));
    }

}
