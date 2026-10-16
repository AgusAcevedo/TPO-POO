package zonaliberada.ui.inventario.paneles;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.*;
import zonaliberada.ui.*;

/** Equipar/desequipar mantiene una sola propiedad de cada unidad. */
public final class PanelEquipables extends PanelSeccion {
    public PanelEquipables(ContextoUI contexto) {
        super(new BorderLayout());
        JPanel lista = Estilo.panel(new GridLayout(0, 1, 0, 10));
        List<ItemDTO> libres = contexto.juego().getInventarioDTO().equipables();
        List<PersonajeDTO> aliados = contexto.juego().getPartyDTO();
        lista.add(Estilo.etiqueta("DISPONIBLES PARA EQUIPAR", 14, Font.BOLD));
        if (libres.isEmpty()) lista.add(Estilo.etiqueta("No hay equipables libres.", 13, Font.PLAIN));
        for (ItemDTO item : libres) {
            JPanel contenido = Estilo.panel(new BorderLayout(0, 8));
            contenido.add(Estilo.descripcion(TextosUI.bonus(item)), BorderLayout.CENTER);
            JPanel controles = Estilo.panel(new BorderLayout(6, 0));
            JComboBox<OpcionUI> destinos = new JComboBox<>();
            destinos.setName("destinoEquipo:" + item.getId());
            destinos.getAccessibleContext().setAccessibleName("Aliado para " + item.getNombre());
            // Este filtro orienta al usuario; el modelo vuelve a comprobar la compatibilidad.
            for (PersonajeDTO a : aliados) {
                if (item.admiteClase(a.getRol())) {
                    destinos.addItem(new OpcionUI(a.getId(), a.getNombre()));
                }
            }
            controles.add(destinos, BorderLayout.CENTER);
            JButton equipar = Estilo.boton("EQUIPAR", Estilo.VERDE);
            equipar.setName("equipar:" + item.getId());
            equipar.setEnabled(destinos.getItemCount() > 0);
            equipar.addActionListener(e -> {
                OpcionUI destino = (OpcionUI) destinos.getSelectedItem();
                if (destino != null && contexto.juego().equipar(destino.getId(), item.getId())) {
                    contexto.mostrar(PantallaId.INVENTARIO);
                } else contexto.avisar("No se pudo equipar esa unidad en el aliado elegido.");
            });
            controles.add(equipar, BorderLayout.EAST);
            contenido.add(controles, BorderLayout.SOUTH);
            lista.add(Estilo.tarjeta(item.getNombre() + " · " + TextosUI.legible(item.getTipo()), contenido));
        }
        lista.add(Estilo.etiqueta("EQUIPADOS EN LOS ALIADOS", 14, Font.BOLD));
        int cantidad = 0;
        for (PersonajeDTO a : aliados) for (ItemDTO item : a.getEquipados()) {
            cantidad++;
            JPanel contenido = Estilo.panel(new BorderLayout(0, 8));
            contenido.add(Estilo.descripcion(TextosUI.bonus(item)), BorderLayout.CENTER);
            JPanel controles = Estilo.panel(new GridLayout(1, 2, 8, 0));
            JButton ficha = Estilo.boton("VER ALIADO", Estilo.VERDE);
            ficha.addActionListener(e -> contexto.verPersonaje(a.getId()));
            controles.add(ficha);
            JButton quitar = Estilo.boton("DESEQUIPAR", Estilo.GRIS);
            quitar.setName("desequipar:" + item.getId());
            quitar.addActionListener(e -> {
                if (contexto.juego().desequipar(a.getId(), item.getId())) contexto.mostrar(PantallaId.INVENTARIO);
                else contexto.avisar("No se pudo devolver ese equipable al inventario.");
            });
            controles.add(quitar);
            contenido.add(controles, BorderLayout.SOUTH);
            lista.add(Estilo.tarjeta(item.getNombre() + " · " + a.getNombre(), contenido));
        }
        if (cantidad == 0) lista.add(Estilo.etiqueta("Todavía no hay objetos equipados.", 13, Font.PLAIN));
        add(Estilo.tarjeta("EQUIPABLES", Estilo.desplazableVertical(lista)));
    }
}
