package zonaliberada.ui.party;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.PersonajeDTO;
import zonaliberada.models.ReglasJuego;
import zonaliberada.ui.*;
import zonaliberada.ui.party.paneles.*;

/** Edición de las seis posiciones y catálogo real de aliados. */
public final class PantallaParty extends PantallaBase {
    public PantallaParty(ContextoUI contexto) {
        super("PARTY Y PERSONAJES", "party.png", contexto);
        JPanel pie = Estilo.panel(new GridLayout(1, 3, 12, 0));
        pie.add(ir("INVENTARIO", Estilo.VERDE, PantallaId.INVENTARIO));
        pie.add(guardarPendiente("GUARDAR FORMACIÓN"));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
    }

    @Override public void actualizar() {
        List<PersonajeDTO> aliados = contexto.juego().getPartyDTO();
        int seleccionados = 0;
        for (PersonajeDTO aliado : aliados) {
            if (aliado.getPosicion() > 0) seleccionados++;
        }
        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 10));
        JLabel nota = Estilo.etiqueta("Seleccionados: " + seleccionados
                + "/" + ReglasJuego.MAXIMO_ALIADOS_ACTIVOS + " · Para combatir: "
                + ReglasJuego.MINIMO_ALIADOS_ACTIVOS + "–" + ReglasJuego.MAXIMO_ALIADOS_ACTIVOS
                + " · Frente: 1–3 · Retaguardia: 4–6", 14, Font.BOLD);
        cuerpo.add(Estilo.tarjeta(null, nota), BorderLayout.NORTH);
        cuerpo.add(Estilo.desplazable(new PanelAliadosActivos(aliados, contexto)), BorderLayout.CENTER);
        JScrollPane catalogo = Estilo.desplazable(new PanelCatalogoAliados(
                aliados, contexto.juego().getMapaDTO(), contexto));
        catalogo.setPreferredSize(new Dimension(0, 175));
        cuerpo.add(Estilo.tarjeta("CATÁLOGO DE ALIADOS", catalogo), BorderLayout.SOUTH);
        reemplazarCentro(cuerpo);
    }
}
