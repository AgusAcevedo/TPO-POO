package zonaliberada.dtos;
import java.util.List;
import java.util.ArrayList;
/** Separa los datos visibles por categoría sin retirar unidades del inventario real. */
public final class InventarioDTO {
    private final List<ItemDTO> items;

    public InventarioDTO(
            List<ItemDTO> items) {
        items = List.copyOf(items);
        this.items = items;
    }

    public List<ItemDTO> getItems() { return items; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof InventarioDTO)) return false;
        InventarioDTO otro = (InventarioDTO) objeto;
        return java.util.Objects.equals(items, otro.items);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(items);
    }

    @Override
    public String toString() {
        return "InventarioDTO["
                + "items=" + items + "]";
    }

    public List<ItemDTO> consumibles() {
        List<ItemDTO> consumibles = new ArrayList<>();
        for (ItemDTO item : items) {
            if (item.getTipo().equals("CONSUMIBLE")) consumibles.add(item);
        }
        return List.copyOf(consumibles);
    }
    public List<ItemDTO> equipables() {
        List<ItemDTO> equipables = new ArrayList<>();
        for (ItemDTO item : items) {
            if (!item.getTipo().equals("CONSUMIBLE")) equipables.add(item);
        }
        return List.copyOf(equipables);
    }
}
