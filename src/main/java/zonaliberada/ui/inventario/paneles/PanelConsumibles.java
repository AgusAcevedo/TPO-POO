package zonaliberada.ui.inventario.paneles;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import zonaliberada.dtos.ItemDTO;
import zonaliberada.ui.*;

/** Muestra las cantidades que informa el inventario; no vuelve a agrupar en la UI. */
public final class PanelConsumibles extends PanelSeccion {
    public PanelConsumibles(List<ItemDTO> items) {
        super(new BorderLayout());
        JPanel lista = Estilo.panel(new GridLayout(0, 1, 0, 10));
        if (items.isEmpty()) lista.add(Estilo.etiqueta("No hay consumibles disponibles.", 14, Font.PLAIN));
        for (ItemDTO item : items) {
            JPanel descripcion = Estilo.panel(new BorderLayout());
            descripcion.add(Estilo.descripcion(item.getDescripcion()));
            lista.add(Estilo.tarjeta(item.getNombre() + " × " + item.getCantidad(), descripcion));
        }
        JPanel contenido = Estilo.panel(new BorderLayout(0, 12));
        contenido.add(Estilo.desplazableVertical(lista), BorderLayout.CENTER);
        // Una nota de dos líneas evita que un JTextArea sin ancho inicial
        // reclame todo el alto y oculte la lista en el primer renderizado.
        contenido.add(Estilo.etiqueta("<html>Se usan solo durante el combate.<br>"
                + "Elegí OBJETO para consumir una unidad.</html>", 12, Font.PLAIN), BorderLayout.SOUTH);
        add(Estilo.tarjeta("CONSUMIBLES", contenido));
    }
}
