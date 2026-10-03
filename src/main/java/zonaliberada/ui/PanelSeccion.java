package zonaliberada.ui;

import java.awt.LayoutManager;
import javax.swing.JPanel;

/** Base visual para las secciones transparentes que componen una pantalla. */
public abstract class PanelSeccion extends JPanel {
    protected PanelSeccion(LayoutManager layout) {
        super(layout);
        // Igual que Estilo.panel: el fondo ilustrado de la pantalla sigue visible.
        setBackground(Estilo.CREMA);
        setOpaque(false);
    }
}
