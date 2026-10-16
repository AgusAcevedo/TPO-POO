package zonaliberada.ui.inicio.paneles;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.ui.*;

/** Opciones del menú; continuar reutiliza la sesión que ya existe en memoria. */
public final class PanelMenuInicio extends PanelSeccion {
    public PanelMenuInicio(ContextoUI contexto) {
        super(null);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());
        JLabel titulo = Estilo.etiqueta("ZONA LIBERADA", 46, Font.BOLD);
        titulo.setForeground(Estilo.VERDE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titulo);
        add(Box.createVerticalStrut(24));
        if (contexto.hayPartida()) {
            agregarBoton("CONTINUAR", Estilo.VERDE,
                    () -> contexto.mostrar(PantallaId.MAPA_PRINCIPAL), true);
        }
        agregarBoton("NUEVA PARTIDA", Estilo.VERDE, contexto::nuevaPartida, true);
        JButton cargar = agregarBoton("CARGAR PARTIDA", Estilo.GRIS, contexto::cargarPartida,
                false);
        cargar.setToolTipText("Cargar partidas está pendiente de implementar");
        JLabel aviso = Estilo.etiqueta("El progreso no se guarda al cerrar el juego.", 13, Font.PLAIN);
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(aviso);
        add(Box.createVerticalStrut(12));
        agregarBoton("SALIR", Estilo.ROJO, contexto::salir, true);
        add(Box.createVerticalGlue());
    }

    private JButton agregarBoton(String texto, Color color, Runnable accion, boolean disponible) {
        JButton boton = Estilo.boton(texto, color);
        // Cada botón recibe qué hacer; el helper solo conecta esa operación con el clic.
        boton.addActionListener(evento -> accion.run());
        boton.setEnabled(disponible);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(340, 52));
        add(boton);
        add(Box.createVerticalStrut(12));
        return boton;
    }
}
