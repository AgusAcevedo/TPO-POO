package zonaliberada.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import zonaliberada.PantallaId;

/** Fondo ilustrado, márgenes y navegación compartidos por las pantallas. */
public abstract class PantallaBase extends JPanel {
    // Consumer es una función que recibe un PantallaId y no devuelve nada.
    // La ventana nos la entrega para que el panel no necesite conocer el JFrame.
    private final Consumer<PantallaId> navegar;
    private final BufferedImage fondo;

    protected PantallaBase(String titulo, String archivoFondo, Consumer<PantallaId> navegar) {
        // BorderLayout reserva zonas arriba, abajo y en el centro, entre otras.
        // Los dos 12 indican la separación horizontal y vertical entre zonas.
        super(new BorderLayout(12, 12));
        this.navegar = navegar;
        fondo = Imagenes.cargar("/imagenes/fondos/" + archivoFondo);
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(14, 16, 14, 16));
        // Un título null permite ubicar el nombre dentro del contenido, como
        // hacemos en el menú. No está escrito dentro del PNG.
        if (titulo != null) {
            add(Estilo.titulo(titulo), BorderLayout.NORTH);
        }
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        // Solo dibujamos una imagen de fondo. Botones y textos siguen siendo
        // componentes Swing normales, colocados por los layouts.
        Graphics2D dibujo = (Graphics2D) graficos.create();
        dibujo.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        dibujo.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        dibujo.dispose();
    }

    protected JButton ir(String texto, Color color, PantallaId destino) {
        JButton boton = Estilo.boton(texto, color);
        // El listener se registra ahora, pero se ejecuta recién al hacer clic.
        // La expresión evento -> ... es una lambda de Java.
        boton.addActionListener(evento -> mostrar(destino));
        return boton;
    }

    protected void mostrar(PantallaId destino) {
        // accept ejecuta la función que recibimos en el constructor.
        navegar.accept(destino);
    }

    protected void avisoPendiente(String accion) {
        // Este aviso marca las acciones que aún no tienen modelo/controlador.
        JOptionPane.showMessageDialog(this,
                accion + " se conectará al modelo y a los controladores más adelante.",
                "Vista de demostración", JOptionPane.INFORMATION_MESSAGE);
    }
}
