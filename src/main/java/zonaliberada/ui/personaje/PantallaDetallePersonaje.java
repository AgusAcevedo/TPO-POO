package zonaliberada.ui.personaje;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.personaje.paneles.PanelEquipamiento;
import zonaliberada.ui.personaje.paneles.PanelEstadisticas;
import zonaliberada.ui.personaje.paneles.PanelFichaPersonaje;
import zonaliberada.ui.personaje.paneles.PanelHabilidades;

/** Ficha, estadísticas y equipamiento del aliado seleccionado. */
public final class PantallaDetallePersonaje extends PantallaBase {
    // Guardamos una referencia para reemplazar su contenido al elegir otro aliado.
    private final JPanel cuerpo = Estilo.panel(new BorderLayout(0, 12));

    public PantallaDetallePersonaje(Consumer<PantallaId> navegar) {
        super("DETALLE DEL PERSONAJE", "party.png", navegar);
        add(cuerpo, BorderLayout.CENTER);

        JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));
        pie.add(ir("VOLVER A PARTY", Estilo.VERDE, PantallaId.PARTY));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
        mostrarFicha(DatosDemo.ALIADOS.get(0));
    }

    /** La ventana llama a este método antes de mostrar el detalle de otro aliado. */
    public void mostrarFicha(Ficha ficha) {
        // Quitamos las tarjetas anteriores, no la ventana ni los botones del pie.
        cuerpo.removeAll();
        JPanel columnas = Estilo.panel(new GridLayout(1, 3, 12, 0));
        columnas.add(new PanelFichaPersonaje(ficha));
        columnas.add(new PanelEquipamiento(ficha, this::avisoPendiente));
        columnas.add(new PanelEstadisticas(ficha));
        cuerpo.add(Estilo.centrarFila(columnas, 350), BorderLayout.CENTER);

        cuerpo.add(new PanelHabilidades(ficha), BorderLayout.SOUTH);
        // revalidate recalcula la distribución; repaint solicita volver a dibujar.
        cuerpo.revalidate();
        cuerpo.repaint();
    }
}
