package zonaliberada.ui.derrota.paneles;

import java.awt.*;
import javax.swing.*;
import zonaliberada.dtos.ResultadoBatallaDTO;
import zonaliberada.ui.*;

/** Muestra la ubicación perdida y si se devolvieron los objetos del inventario. */
public final class PanelMensajeDerrota extends PanelSeccion {
    public PanelMensajeDerrota(ResultadoBatallaDTO resultado) {
        super(new BorderLayout(0, 20));
        JLabel titulo = Estilo.etiqueta("EL EQUIPO FUE DERROTADO", 28, Font.BOLD);
        titulo.setForeground(Estilo.ROJO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel texto = Estilo.panel(new BorderLayout(0, 18));
        texto.add(titulo, BorderLayout.NORTH);
        texto.add(Estilo.descripcion(resultado.getUbicacionNombre() + "\n\n" + resultado.getMensaje()
                + "\n\nNo se entregó experiencia. Podés reorganizar la party o reintentar; "
                + "los aliados seleccionados comenzarán de nuevo con vida y maná completos."),
                BorderLayout.CENTER);
        add(Estilo.centrarFila(Estilo.tarjeta("INVENTARIO "
                + (resultado.isInventarioRestaurado() ? "RESTAURADO" : "NO RESTAURADO"), texto), 250));
    }
}
