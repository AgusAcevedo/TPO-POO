package zonaliberada.ui.personaje.paneles;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.*;
import zonaliberada.ui.*;

/** Cada selector conserva el ID de unidad, aunque dos objetos se llamen igual. */
public final class PanelEquipamiento extends PanelSeccion {
    private final PersonajeDTO ficha;
    private final ContextoUI contexto;
    private final JComboBox<OpcionUI> disponibles = new JComboBox<>();
    private final JComboBox<OpcionUI> equipados = new JComboBox<>();
    private final JButton equipar = Estilo.boton("EQUIPAR / REEMPLAZAR", Estilo.VERDE);
    private final JButton quitar = Estilo.boton("DESEQUIPAR", Estilo.GRIS);

    public PanelEquipamiento(PersonajeDTO ficha, ContextoUI contexto) {
        super(new BorderLayout());
        this.ficha = ficha;
        this.contexto = contexto;
        inicializarComponentes();
        inicializarEventos();
    }

    private void inicializarComponentes() {
        JPanel contenido = Estilo.panel(new GridLayout(0, 1, 0, 7));
        for (String tipo : new String[]{"ARMA", "ROPA", "ACCESORIO"}) {
            String nombre = "sin equipar";
            for (ItemDTO equipado : ficha.getEquipados()) {
                if (equipado.getTipo().equals(tipo)) {
                    nombre = equipado.getNombre();
                    break;
                }
            }
            contenido.add(Estilo.etiqueta(TextosUI.legible(tipo) + ": " + nombre, 13, Font.PLAIN));
        }
        disponibles.setName("equipoDisponible");
        disponibles.getAccessibleContext().setAccessibleName("Equipable del inventario");
        for (ItemDTO item : contexto.juego().getInventarioDTO().equipables()) {
            if (item.admiteClase(ficha.getRol())) {
                disponibles.addItem(new OpcionUI(item.getId(), item.getNombre() + " · " + TextosUI.legible(item.getTipo())));
            }
        }
        disponibles.setEnabled(disponibles.getItemCount() > 0);
        contenido.add(disponibles);
        equipar.setEnabled(disponibles.getItemCount() > 0);
        contenido.add(equipar);
        equipados.setName("equipoActual");
        equipados.getAccessibleContext().setAccessibleName("Objeto equipado para retirar");
        for (ItemDTO item : ficha.getEquipados()) {
            equipados.addItem(new OpcionUI(item.getId(), item.getNombre() + " · " + TextosUI.legible(item.getTipo())));
        }
        equipados.setEnabled(equipados.getItemCount() > 0);
        contenido.add(equipados);
        quitar.setEnabled(equipados.getItemCount() > 0);
        contenido.add(quitar);
        add(Estilo.tarjeta("EQUIPAMIENTO", Estilo.desplazableVertical(contenido)));
    }

    private void inicializarEventos() {
        equipar.addActionListener(e -> {
            OpcionUI elegido = (OpcionUI) disponibles.getSelectedItem();
            if (elegido != null && contexto.juego().equipar(ficha.getId(), elegido.getId())) {
                // Consultar de nuevo la ficha actualiza los bonus y el inventario.
                contexto.mostrar(PantallaId.DETALLE_PERSONAJE);
            } else contexto.avisar("No se pudo equipar el objeto seleccionado.");
        });
        quitar.addActionListener(e -> {
            OpcionUI elegido = (OpcionUI) equipados.getSelectedItem();
            if (elegido != null && contexto.juego().desequipar(ficha.getId(), elegido.getId())) {
                contexto.mostrar(PantallaId.DETALLE_PERSONAJE);
            } else contexto.avisar("No se pudo desequipar el objeto seleccionado.");
        });
    }
}
