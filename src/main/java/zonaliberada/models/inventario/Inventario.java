package zonaliberada.models.inventario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Guarda cantidades de consumibles y piezas individuales de equipamiento. */
public class Inventario {
    // La clave es la configuración compartida: no creamos una instancia por dosis.
    private final Map<Consumible, Integer> consumibles = new LinkedHashMap<>();
    private final List<Equipable> equipables = new ArrayList<>();

    public Inventario(List<Item> items) {
        if (items == null) throw new IllegalArgumentException("La lista de ítems es obligatoria");
        for (Item item : items) {
            if (!agregarItem(item)) {
                throw new IllegalArgumentException("Los ítems deben ser válidos y los equipables únicos");
            }
        }
    }

    /** Una entrada por tipo de consumible y una por cada pieza equipable. */
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>(consumibles.keySet());
        items.addAll(equipables);
        return List.copyOf(items);
    }

    public int getCantidad(Item item) {
        if (item instanceof Consumible) return consumibles.getOrDefault(item, 0);
        return contiene(item) ? 1 : 0;
    }

    /** Copia solo los contadores; las definiciones de los objetos no cambian. */
    public Map<Consumible, Integer> getCantidadesConsumibles() {
        return new LinkedHashMap<>(consumibles);
    }

    public Item buscarPorId(String id) {
        for (Item item : getItems()) {
            if (item.getId().equals(id)) return item;
        }
        return null;
    }

    public boolean contiene(Item item) {
        // Un objeto ajeno con el mismo ID no puede reemplazar la referencia real.
        return item != null && buscarPorId(item.getId()) == item;
    }

    public boolean puedeAgregar(Item item) { return puedeAgregar(item, 1); }

    public boolean puedeAgregar(Item item, int cantidad) {
        if (item == null || cantidad <= 0) return false;
        Item existente = buscarPorId(item.getId());
        if (item instanceof Consumible consumible) {
            return (existente == null || existente == item)
                    && (long) getCantidad(consumible) + cantidad <= Integer.MAX_VALUE;
        }
        return item instanceof Equipable && cantidad == 1 && existente == null;
    }

    public boolean agregarItem(Item item) { return agregarItem(item, 1); }

    public boolean agregarItem(Item item, int cantidad) {
        if (!puedeAgregar(item, cantidad)) return false;
        if (item instanceof Consumible consumible) {
            consumibles.put(consumible, getCantidad(consumible) + cantidad);
        } else {
            equipables.add((Equipable) item);
        }
        return true;
    }

    /** Retira una dosis o una pieza; al llegar a cero el tipo deja de mostrarse. */
    public boolean retirarItem(Item item) {
        if (!contiene(item)) return false;
        if (item instanceof Consumible consumible) {
            int restante = getCantidad(consumible) - 1;
            if (restante == 0) consumibles.remove(consumible);
            else consumibles.put(consumible, restante);
        } else {
            equipables.remove(item);
        }
        return true;
    }

    public void restaurarConsumibles(Map<Consumible, Integer> cantidades) {
        if (cantidades == null) throw new IllegalArgumentException("Las cantidades son obligatorias");
        // Validamos antes de tocar el inventario; no se modifica el equipamiento.
        Inventario validado = new Inventario(List.of());
        for (Map.Entry<Consumible, Integer> entrada : cantidades.entrySet()) {
            if (entrada.getValue() == null || !validado.agregarItem(entrada.getKey(), entrada.getValue())) {
                throw new IllegalArgumentException("Las cantidades deben ser positivas y los tipos únicos");
            }
            Item propio = buscarPorId(entrada.getKey().getId());
            if (propio != null && propio != entrada.getKey()) {
                throw new IllegalArgumentException("El tipo no corresponde al objeto del inventario");
            }
        }
        consumibles.clear();
        consumibles.putAll(validado.consumibles);
    }
}
