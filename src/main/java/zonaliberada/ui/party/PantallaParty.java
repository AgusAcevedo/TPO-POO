package zonaliberada.ui.party;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import zonaliberada.PantallaId;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.PantallaBase;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.party.paneles.PanelAliadosActivos;
import zonaliberada.ui.party.paneles.PanelCatalogoAliados;

/** Muestra la party de ejemplo y permite consultar la ficha de cada aliado. */
public final class PantallaParty extends PantallaBase {
    public PantallaParty(Consumer<PantallaId> navegar, Consumer<Ficha> verDetalle) {
        super("PARTY Y PERSONAJES", "party.png", navegar);

        // Dos funciones distintas: navegar cambia de pantalla; verDetalle
        // comunica además qué ficha queremos mostrar, sin crear un controlador.
        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 15));
        cuerpo.add(new PanelAliadosActivos(verDetalle), BorderLayout.NORTH);
        cuerpo.add(Estilo.tarjeta("CATÁLOGO DE ALIADOS", new PanelCatalogoAliados(verDetalle)),
                BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER);

        // No ofrecemos "Confirmar" porque todavía no se puede editar la party.
        JPanel pie = Estilo.panel(new GridLayout(1, 2, 12, 0));
        pie.add(ir("INVENTARIO", Estilo.VERDE, PantallaId.INVENTARIO));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
    }
}
