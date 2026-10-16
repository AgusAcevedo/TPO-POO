package zonaliberada.ui.derrota;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.ResultadoBatallaDTO;
import zonaliberada.ui.*;
import zonaliberada.ui.derrota.paneles.PanelMensajeDerrota;

/** La restauración ya ocurrió; reintentar inicia un encuentro nuevo desde ella. */
public final class PantallaDerrota extends PantallaBase {
    public PantallaDerrota(ContextoUI contexto) {
        super("DERROTA", "derrota.png", contexto);
        JPanel pie = Estilo.panel(new GridLayout(1, 3, 12, 0));
        JButton reintentar = Estilo.boton("REINTENTAR BATALLA", Estilo.VERDE);
        reintentar.addActionListener(e -> contexto.reintentarBatalla());
        pie.add(reintentar);
        pie.add(ir("REORGANIZAR PARTY", Estilo.AZUL, PantallaId.PARTY));
        JButton menu = Estilo.boton("VOLVER AL MENÚ", Estilo.GRIS);
        menu.addActionListener(e -> contexto.volverAlMenu());
        pie.add(menu);
        add(pie, BorderLayout.SOUTH);
    }

    @Override public void actualizar() {
        ResultadoBatallaDTO resultado = contexto.juego().getUltimoResultado();
        if (resultado != null) reemplazarCentro(new PanelMensajeDerrota(resultado));
    }
}
