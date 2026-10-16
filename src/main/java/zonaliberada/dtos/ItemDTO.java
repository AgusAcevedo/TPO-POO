package zonaliberada.dtos;
import java.util.List;
import zonaliberada.models.personajes.TipoClase;
/** Una entrada visible: cantidad por consumible o una pieza de equipamiento. */
public final class ItemDTO {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final String tipo;
    private final int bonusAtaque;
    private final int bonusDefensa;
    private final int bonusVida;
    private final int bonusMana;
    private final int bonusVelocidad;
    private final int bonusPrecision;
    private final List<TipoClase> clasesPermitidas;
    private final boolean grupal;
    private final int cantidad;

    public ItemDTO(
            String id,
            String nombre,
            String descripcion,
            String tipo,
            int bonusAtaque,
            int bonusDefensa,
            int bonusVida,
            int bonusMana,
            int bonusVelocidad,
            int bonusPrecision,
            List<TipoClase> clasesPermitidas,
            boolean grupal, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad visible debe ser positiva");
        clasesPermitidas = List.copyOf(clasesPermitidas);
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.bonusAtaque = bonusAtaque;
        this.bonusDefensa = bonusDefensa;
        this.bonusVida = bonusVida;
        this.bonusMana = bonusMana;
        this.bonusVelocidad = bonusVelocidad;
        this.bonusPrecision = bonusPrecision;
        this.clasesPermitidas = clasesPermitidas;
        this.grupal = grupal;
        this.cantidad = cantidad;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return tipo; }
    public int getBonusAtaque() { return bonusAtaque; }
    public int getBonusDefensa() { return bonusDefensa; }
    public int getBonusVida() { return bonusVida; }
    public int getBonusMana() { return bonusMana; }
    public int getBonusVelocidad() { return bonusVelocidad; }
    public int getBonusPrecision() { return bonusPrecision; }
    public boolean isGrupal() { return grupal; }
    public int getCantidad() { return cantidad; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof ItemDTO)) return false;
        ItemDTO otro = (ItemDTO) objeto;
        return java.util.Objects.equals(id, otro.id)
                && java.util.Objects.equals(nombre, otro.nombre)
                && java.util.Objects.equals(descripcion, otro.descripcion)
                && java.util.Objects.equals(tipo, otro.tipo)
                && bonusAtaque == otro.bonusAtaque
                && bonusDefensa == otro.bonusDefensa
                && bonusVida == otro.bonusVida
                && bonusMana == otro.bonusMana
                && bonusVelocidad == otro.bonusVelocidad
                && bonusPrecision == otro.bonusPrecision
                && java.util.Objects.equals(clasesPermitidas, otro.clasesPermitidas)
                && grupal == otro.grupal
                && cantidad == otro.cantidad;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                id, nombre, descripcion, tipo, bonusAtaque, bonusDefensa, bonusVida, bonusMana,
                bonusVelocidad, bonusPrecision, clasesPermitidas, grupal, cantidad);
    }

    @Override
    public String toString() {
        return "ItemDTO["
                + "id=" + id
                + ", nombre=" + nombre
                + ", descripcion=" + descripcion
                + ", tipo=" + tipo
                + ", bonusAtaque=" + bonusAtaque
                + ", bonusDefensa=" + bonusDefensa
                + ", bonusVida=" + bonusVida
                + ", bonusMana=" + bonusMana
                + ", bonusVelocidad=" + bonusVelocidad
                + ", bonusPrecision=" + bonusPrecision
                + ", clasesPermitidas=" + clasesPermitidas
                + ", grupal=" + grupal + ", cantidad=" + cantidad + "]";
    }

    public boolean admiteClase(String rol) {
        if (clasesPermitidas.isEmpty()) return true;
        for (TipoClase clase : clasesPermitidas) {
            if (clase.name().equals(rol)) return true;
        }
        return false;
    }
}
