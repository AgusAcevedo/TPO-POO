package zonaliberada.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import zonaliberada.PantallaId;

/** Fondo, navegación y refresco comunes; las reglas siguen en el controlador. */
public abstract class PantallaBase extends JPanel {
    protected final ContextoUI contexto;
    private BufferedImage fondo;
    private final JLabel titulo;

    protected PantallaBase(String nombre, String archivoFondo, ContextoUI contexto) {
        super(new BorderLayout(12, 12));
        this.contexto = contexto;
        cambiarFondo(archivoFondo);
        setBackground(Estilo.CREMA);
        setBorder(new EmptyBorder(14, 16, 14, 16));
        titulo = nombre == null ? null : Estilo.titulo(nombre);
        if (titulo != null) add(titulo, BorderLayout.NORTH);
    }

    /** La ventana lo llama cada vez que entra: nunca conserva un DTO viejo. */
    public abstract void actualizar();

    protected void titulo(String texto) {
        if (titulo != null) titulo.setText(texto);
    }

    protected final void cambiarFondo(String archivo) {
        fondo = Imagenes.cargar("/imagenes/fondos/" + archivo);
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        // Dibujamos sobre una copia para no alterar el contexto gráfico que Swing comparte.
        Graphics2D dibujo = (Graphics2D) graficos.create();
        dibujo.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        dibujo.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        dibujo.dispose();
    }

    protected JButton ir(String texto, Color color, PantallaId destino) {
        JButton boton = Estilo.boton(texto, color);
        // El listener se ejecuta al hacer clic, no durante la creación del botón.
        boton.addActionListener(evento -> mostrar(destino));
        return boton;
    }

    protected JButton guardarPendiente(String texto) {
        JButton boton = Estilo.boton(texto, Estilo.AZUL);
        boton.setEnabled(false);
        boton.setToolTipText("Guardar partidas está pendiente de implementar");
        return boton;
    }

    protected void mostrar(PantallaId destino) { contexto.mostrar(destino); }

    protected void reemplazarCentro(Component componente) {
        Component anterior = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (anterior != null) remove(anterior);
        add(componente, BorderLayout.CENTER);
        // revalidate recalcula la distribución; repaint solicita volver a dibujar.
        revalidate();
        repaint();
    }
}
