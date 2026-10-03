package zonaliberada.ui.personaje.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Objetos equipados y controles de muestra para equipar o desequipar. */
public final class PanelEquipamiento extends PanelSeccion {
    public PanelEquipamiento(Ficha ficha, Consumer<String> avisoPendiente) {
        super(new BorderLayout());
        JPanel contenido = Estilo.panel(new GridLayout(6, 1, 0, 10));
        contenido.add(Estilo.etiqueta("Arma: " + ficha.arma, 14, Font.PLAIN));
        contenido.add(Estilo.etiqueta("Ropa: sin equipar", 14, Font.PLAIN));
        contenido.add(Estilo.etiqueta("Accesorio: sin equipar", 14, Font.PLAIN));
        // JComboBox es un selector desplegable nativo, sin código de dibujo propio.
        // Sus opciones son de muestra; la compatibilidad por clase está pendiente.
        JComboBox<String> objetos = new JComboBox<>();
        objetos.addItem(ficha.arma);
        for (String nombre : DatosDemo.EQUIPABLES_DISPONIBLES) {
            objetos.addItem(nombre);
        }
        objetos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        contenido.add(objetos);
        JButton equipar = Estilo.boton("EQUIPAR", Estilo.VERDE);
        equipar.addActionListener(evento -> avisoPendiente.accept("Equipar " + objetos.getSelectedItem()));
        contenido.add(equipar);
        JButton desequipar = Estilo.boton("DESEQUIPAR", Estilo.GRIS);
        desequipar.addActionListener(evento -> avisoPendiente.accept("Desequipar un objeto"));
        contenido.add(desequipar);
        add(Estilo.tarjeta("EQUIPAMIENTO", contenido), BorderLayout.CENTER);
    }
}
