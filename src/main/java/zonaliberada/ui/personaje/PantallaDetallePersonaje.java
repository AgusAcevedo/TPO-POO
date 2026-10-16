package zonaliberada.ui.personaje;

import java.awt.*;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.PersonajeDTO;
import zonaliberada.ui.*;
import zonaliberada.ui.personaje.paneles.*;

/** La ficha se consulta de nuevo después de cada cambio de equipamiento. */
public final class PantallaDetallePersonaje extends PantallaBase {
    public PantallaDetallePersonaje(ContextoUI contexto) {
        super("DETALLE DEL PERSONAJE", "party.png", contexto);
        JPanel pie = Estilo.panel(new GridLayout(1, 3, 12, 0));
        pie.add(ir("VOLVER A PARTY", Estilo.VERDE, PantallaId.PARTY));
        pie.add(ir("INVENTARIO", Estilo.AZUL, PantallaId.INVENTARIO));
        pie.add(ir("VOLVER A ZONAS", Estilo.GRIS, PantallaId.MAPA_PRINCIPAL));
        add(pie, BorderLayout.SOUTH);
    }

    @Override public void actualizar() {
        PersonajeDTO personaje = contexto.personajeSeleccionado();
        if (personaje == null) {
            reemplazarCentro(Estilo.etiqueta("Elegí un aliado desde la party o el inventario.", 16, Font.PLAIN));
            return;
        }
        titulo(personaje.getNombre().toUpperCase(java.util.Locale.ROOT));
        JPanel columnas = Estilo.panel(new GridLayout(1, 3, 12, 0));
        columnas.add(new PanelFichaPersonaje(personaje));
        columnas.add(new PanelEquipamiento(personaje, contexto));
        columnas.add(new PanelEstadisticas(personaje));
        columnas.setPreferredSize(new Dimension(1000, 380));
        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 12));
        cuerpo.add(Estilo.desplazableVertical(columnas), BorderLayout.CENTER);
        JScrollPane habilidades = Estilo.desplazableVertical(new PanelHabilidades(personaje));
        habilidades.setPreferredSize(new Dimension(0, 160));
        cuerpo.add(habilidades, BorderLayout.SOUTH);
        reemplazarCentro(cuerpo);
    }
}
