package zonaliberada.ui.batalla.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Botones de combate y accesos de prueba a las pantallas de resultado. */
public final class PanelAcciones extends PanelSeccion {
    public PanelAcciones(Consumer<PantallaId> navegar, Consumer<String> avisoPendiente) {
        super(new BorderLayout(0, 8));
        JPanel acciones = Estilo.panel(new GridLayout(1, 5, 10, 0));
        acciones.add(accion("ATACAR", Estilo.ROJO, avisoPendiente));
        acciones.add(accion("HABILIDAD", Estilo.AZUL, avisoPendiente));
        acciones.add(accion("DEFENDER", Estilo.AZUL, avisoPendiente));
        acciones.add(accion("OBJETO", Estilo.AZUL, avisoPendiente));
        acciones.add(accion("FINALIZAR TURNO", Estilo.GRIS, avisoPendiente));
        add(acciones, BorderLayout.CENTER);

        // Los resultados de prueba quedan separados de las acciones del juego.
        // FlowLayout alinea estos controles a la derecha con su tamaño natural.
        JPanel demostracion = Estilo.panel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JLabel prueba = Estilo.etiqueta("Resultados de prueba:", 12, Font.PLAIN);
        prueba.setForeground(Estilo.CREMA);
        demostracion.add(prueba);
        demostracion.add(ir("VICTORIA", Estilo.VERDE, PantallaId.VICTORIA, navegar));
        demostracion.add(ir("DERROTA", Estilo.ROJO, PantallaId.DERROTA, navegar));
        add(demostracion, BorderLayout.SOUTH);
    }

    private JButton accion(String nombre, Color color, Consumer<String> avisoPendiente) {
        JButton boton = Estilo.boton(nombre, color);
        // No restamos PA ni ejecutamos efectos desde la vista.
        // Más adelante este listener comunicará la acción al controlador.
        boton.addActionListener(evento -> avisoPendiente.accept("La acción " + nombre.toLowerCase()));
        return boton;
    }

    private JButton ir(String nombre, Color color, PantallaId destino,
            Consumer<PantallaId> navegar) {
        JButton boton = Estilo.boton(nombre, color);
        boton.addActionListener(evento -> navegar.accept(destino));
        return boton;
    }
}
