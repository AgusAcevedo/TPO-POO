package zonaliberada.ui.party.paneles;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import zonaliberada.PantallaId;
import zonaliberada.dtos.PersonajeDTO;
import zonaliberada.models.ReglasJuego;
import zonaliberada.ui.*;

/** Seis casillas; asignar, mover y quitar usan únicamente IDs. */
public final class PanelAliadosActivos extends PanelSeccion {
    public PanelAliadosActivos(List<PersonajeDTO> aliados, ContextoUI contexto) {
        super(new GridLayout(2, 3, 10, 10));
        setPreferredSize(new Dimension(980, 335));
        for (int posicion = 1; posicion <= ReglasJuego.CANTIDAD_POSICIONES; posicion++) {
            PersonajeDTO aliado = PersonajeDTO.buscarEnPosicion(aliados, posicion);
            add(casilla(posicion, aliado, aliados, contexto));
        }
    }

    private JPanel casilla(int numero, PersonajeDTO aliado, List<PersonajeDTO> roster, ContextoUI contexto) {
        JPanel contenido = Estilo.panel(new BorderLayout(8, 6));
        String titulo = numero + " · " + (numero <= ReglasJuego.CANTIDAD_FRONTALES ? "Frente" : "Retaguardia");
        if (aliado == null) {
            JComboBox<OpcionUI> opciones = new JComboBox<>();
            opciones.setName("formacion:" + numero);
            opciones.getAccessibleContext().setAccessibleName("Aliado para posición " + numero);
            opciones.addItem(new OpcionUI("", "Elegí un aliado"));
            // También se ofrecen activos: el modelo los mueve, no crea otro personaje.
            for (PersonajeDTO a : roster) opciones.addItem(new OpcionUI(a.getId(), a.getNombre()
                    + (a.getPosicion() == 0 ? " · reserva" : " · posición " + a.getPosicion())));
            contenido.add(Estilo.etiqueta("Posición vacía", 14, Font.PLAIN), BorderLayout.NORTH);
            contenido.add(opciones, BorderLayout.CENTER);
            JButton asignar = Estilo.boton("ASIGNAR / MOVER", Estilo.VERDE);
            asignar.setName("asignar:" + numero);
            asignar.addActionListener(e -> {
                OpcionUI opcion = (OpcionUI) opciones.getSelectedItem();
                if (opcion == null || opcion.getId().isEmpty()) return;
                if (!contexto.juego().editarFormacion(numero, opcion.getId())) {
                    contexto.avisar("No se pudo asignar: la formación admite hasta cuatro aliados únicos.");
                } else contexto.mostrar(PantallaId.PARTY);
            });
            contenido.add(asignar, BorderLayout.SOUTH);
        } else {
            titulo += " · " + aliado.getNombre();
            contenido.add(Imagenes.personaje(aliado, 42, 63), BorderLayout.WEST);
            JPanel datos = Estilo.panel(new GridLayout(3, 1, 0, 3));
            datos.add(Estilo.etiqueta("Nivel " + aliado.getNivel() + " · " + TextosUI.legible(aliado.getRol()), 12, Font.BOLD));
            datos.add(Estilo.barra("HP", aliado.getVida(), aliado.getVidaMaxima(), Estilo.ROJO));
            datos.add(Estilo.barra("MP", aliado.getMana(), aliado.getManaMaximo(), Estilo.AZUL));
            contenido.add(datos, BorderLayout.CENTER);
            JPanel botones = Estilo.panel(new GridLayout(1, 2, 6, 0));
            JButton detalle = Estilo.boton("DETALLE", Estilo.VERDE);
            detalle.setName("detalle:" + aliado.getId());
            detalle.addActionListener(e -> contexto.verPersonaje(aliado.getId()));
            botones.add(detalle);
            JButton quitar = Estilo.boton("QUITAR", Estilo.GRIS);
            quitar.setName("quitar:" + aliado.getId());
            quitar.addActionListener(e -> {
                if (contexto.juego().quitarDeFormacion(aliado.getId())) contexto.mostrar(PantallaId.PARTY);
                else contexto.avisar("No se pudo quitar al aliado de la formación.");
            });
            botones.add(quitar);
            contenido.add(botones, BorderLayout.SOUTH);
        }
        return Estilo.tarjeta("<html>" + TextosUI.html(titulo) + "</html>", contenido);
    }
}
