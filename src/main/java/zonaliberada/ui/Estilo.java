package zonaliberada.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.JTextArea;
import zonaliberada.dtos.PersonajeDTO;
import javax.swing.border.EmptyBorder;

/** Métodos de ayuda para crear componentes Swing con un estilo consistente. */
public final class Estilo {
    // Las constantes compartidas permiten cambiar la paleta en un solo lugar.
    public static final Color CREMA = new Color(247, 237, 211);
    public static final Color VERDE = new Color(29, 67, 43);
    public static final Color ROJO = new Color(143, 56, 42);
    public static final Color AZUL = new Color(42, 82, 117);
    public static final Color DORADO = new Color(214, 168, 61);
    public static final Color TINTA = new Color(42, 47, 37);
    public static final Color GRIS = new Color(212, 209, 191);
    public static final Color TEXTO_SECUNDARIO = new Color(94, 100, 85);

    private static final Font TEXTO = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    private static final Font TITULO = new Font(Font.MONOSPACED, Font.BOLD, 28);

    private Estilo() {
        // No creamos objetos Estilo: sus métodos son static y se usan directamente.
    }

    /** Crea un contenedor; el layout determina cómo se acomodan sus componentes. */
    public static JPanel panel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(CREMA);
        // Los contenedores dejan ver el fondo de la pantalla. Las tarjetas
        // vuelven a ser opacas para que el texto se lea con claridad.
        panel.setOpaque(false);
        return panel;
    }

    /** Envuelve un panel ya construido con borde, márgenes y un título opcional. */
    public static JPanel tarjeta(String titulo, Component contenido) {
        JPanel tarjeta = panel(new BorderLayout(8, 8));
        tarjeta.setOpaque(true);
        // El borde compuesto combina una línea visible con espacio interior.
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE, 2), new EmptyBorder(10, 10, 10, 10)));
        if (titulo != null) {
            JLabel encabezado = etiqueta(titulo, 17, Font.BOLD);
            encabezado.setForeground(VERDE);
            tarjeta.add(encabezado, BorderLayout.NORTH);
        }
        // Recibimos el contenido directamente: no hay que buscarlo luego
        // por índice con getComponent ni hacer conversiones de tipo.
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    /** Centra una fila de tarjetas verticalmente sin fijar coordenadas x/y. */
    public static JPanel centrarFila(JPanel fila, int alto) {
        JPanel contenedor = panel(null);
        // BoxLayout acomoda en un eje; los espacios flexibles centran la fila en vertical.
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        fila.setPreferredSize(new Dimension(0, alto));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, alto));
        // Los espacios flexibles absorben el lugar sobrante arriba y abajo.
        contenedor.add(Box.createVerticalGlue());
        contenedor.add(fila);
        contenedor.add(Box.createVerticalGlue());
        return contenedor;
    }

    public static JLabel etiqueta(String texto, int tamano, int peso) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font(Font.MONOSPACED, peso, tamano));
        etiqueta.setForeground(TINTA);
        return etiqueta;
    }

    public static JLabel titulo(String texto) {
        JLabel titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(TITULO);
        titulo.setForeground(CREMA);
        // Una JLabel solo pinta su fondo si es opaca.
        titulo.setOpaque(true);
        titulo.setBackground(VERDE);
        titulo.setBorder(new EmptyBorder(10, 12, 10, 12));
        return titulo;
    }

    public static JButton boton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setName(texto);
        boton.setFont(TEXTO.deriveFont(Font.BOLD));
        boton.setBackground(fondo);
        // En botones neutros usamos texto oscuro para mantener el contraste.
        boton.setForeground(fondo.equals(GRIS) ? TINTA : Color.WHITE);
        // Conservamos el indicador de foco nativo para navegar con el teclado.
        boton.setFocusPainted(true);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TINTA, 2), new EmptyBorder(10, 14, 10, 14)));
        return boton;
    }

    public static JScrollPane desplazable(Component contenido) {
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return scroll;
    }

    /** Las listas verticales se ajustan al ancho: no esconden botones a la derecha. */
    public static JScrollPane desplazableVertical(Component contenido) {
        return desplazable(new ContenedorVertical(contenido));
    }

    public static JTextArea descripcion(String texto) {
        JTextArea area = new JTextArea(texto, 2, 20);
        area.setFont(TEXTO.deriveFont(13f));
        area.setForeground(TINTA);
        area.setOpaque(false);
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    public static JPanel experiencia(PersonajeDTO personaje) {
        if (personaje.getExperienciaSiguiente() > 0) {
            return barra("XP", personaje.getExperiencia(), personaje.getExperienciaSiguiente(), DORADO);
        }
        JPanel panel = panel(new BorderLayout());
        panel.add(etiqueta("Nivel máximo alcanzado", 12, Font.BOLD));
        return panel;
    }

    public static JPanel barra(String nombre, int valor, int maximo, Color color) {
        JPanel fila = panel(null);
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        JLabel etiqueta = etiqueta(nombre, 12, Font.BOLD);
        // La barra solo representa un número: no calcula vida ni avanza sola.
        JProgressBar barra = new JProgressBar(0, maximo);
        barra.setValue(valor);
        barra.setForeground(color);
        barra.setBackground(GRIS);
        barra.setString(valor + "/" + maximo);
        barra.setStringPainted(true);
        barra.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        // La barra puede crecer en ancho, pero no en alto. Así no se convierte
        // en un bloque gigante si la tarjeta que la contiene tiene espacio libre.
        barra.setPreferredSize(new Dimension(100, 20));
        barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        fila.add(etiqueta);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(barra);
        return fila;
    }

    // Scrollable permite ajustar el ancho al área visible y conservar el desplazamiento vertical.
    private static final class ContenedorVertical extends JPanel implements Scrollable {
        private ContenedorVertical(Component contenido) {
            super(new BorderLayout());
            setOpaque(false);
            add(contenido);
        }

        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle visible, int orientacion, int direccion) {
            return 18;
        }
        @Override public int getScrollableBlockIncrement(Rectangle visible, int orientacion, int direccion) {
            return Math.max(18, (orientacion == SwingConstants.VERTICAL ? visible.height : visible.width) - 18);
        }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport viewport && getPreferredSize().height <= viewport.getHeight();
        }
    }
}
