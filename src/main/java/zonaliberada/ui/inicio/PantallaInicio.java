package zonaliberada.ui.inicio;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import zonaliberada.PantallaId;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.inicio.paneles.PanelMenuInicio;

/** Menú inicial: imagen de fondo, título y tres botones Swing. */
public final class PantallaInicio extends PantallaBase {
    public PantallaInicio(Consumer<PantallaId> navegar) {
        super(null, "inicio.png", navegar);

        // El menú vive en su propio panel; la pantalla conserva el fondo y la navegación.
        add(new PanelMenuInicio(this::mostrar), BorderLayout.CENTER);
    }
}
