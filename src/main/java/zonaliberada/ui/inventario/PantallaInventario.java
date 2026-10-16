package zonaliberada.ui.inventario;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.ui.*;
import zonaliberada.ui.inventario.paneles.*;

/** Inventario real: consultar consumibles y transferir equipo a un aliado. */
public final class PantallaInventario extends PantallaBase {
    public PantallaInventario(ContextoUI contexto) {
        super("INVENTARIO", "inventario.png", contexto);
        JPanel pie = Estilo.panel(new GridLayout(1, 3, 12, 0));
        pie.add(ir("VOLVER A PARTY", Estilo.VERDE, PantallaId.PARTY));
        pie.add(guardarPendiente("GUARDAR PARTIDA"));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
    }

    @Override public void actualizar() {
        JPanel columnas = Estilo.panel(new GridLayout(1, 2, 12, 0));
        columnas.add(new PanelConsumibles(contexto.juego().getInventarioDTO().consumibles()));
        columnas.add(new PanelEquipables(contexto));
        reemplazarCentro(columnas);
    }
}
