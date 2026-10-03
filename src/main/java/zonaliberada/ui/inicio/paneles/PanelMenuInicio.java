package zonaliberada.ui.inicio.paneles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PanelSeccion;

/** Título y botones del menú; no administra el cambio de pantallas. */
public final class PanelMenuInicio extends PanelSeccion {
    public PanelMenuInicio(Consumer<PantallaId> navegar) {
        super(null);
        // El PNG no contiene texto ni botones: todos estos elementos son Swing.
        // BoxLayout los coloca uno debajo del otro sobre el centro despejado.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // El espacio flexible centra el grupo; el strut separa dos botones.
        add(Box.createVerticalGlue());
        JLabel titulo = Estilo.etiqueta("ZONA LIBERADA", 46, Font.BOLD);
        titulo.setForeground(Estilo.VERDE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);
        add(Box.createVerticalStrut(28));
        add(botonMenu("NUEVA PARTIDA", Estilo.VERDE, PantallaId.MAPA_PRINCIPAL, navegar));
        add(Box.createVerticalStrut(14));
        JButton cargar = Estilo.boton("CARGAR PARTIDA", Estilo.GRIS);
        // No simulamos una carga inexistente: el botón queda deshabilitado.
        cargar.setEnabled(false);
        cargar.setToolTipText("Se habilitará al implementar la persistencia");
        cargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        cargar.setMaximumSize(new Dimension(320, 52));
        add(cargar);
        add(Box.createVerticalStrut(14));
        JButton salir = Estilo.boton("SALIR", Estilo.ROJO);
        salir.addActionListener(evento -> System.exit(0));
        salir.setAlignmentX(Component.CENTER_ALIGNMENT);
        salir.setMaximumSize(new Dimension(320, 52));
        add(salir);
        add(Box.createVerticalGlue());
    }

    private JButton botonMenu(String texto, Color color, PantallaId destino,
            Consumer<PantallaId> navegar) {
        JButton boton = Estilo.boton(texto, color);
        boton.addActionListener(evento -> navegar.accept(destino));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(320, 52));
        return boton;
    }
}
