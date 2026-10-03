package zonaliberada.ui.party.paneles;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.Imagenes;
import zonaliberada.ui.PanelSeccion;

/** Cuatro tarjetas de los aliados activos de la party de muestra. */
public final class PanelAliadosActivos extends PanelSeccion {
    public PanelAliadosActivos(Consumer<Ficha> verDetalle) {
        super(new GridLayout(1, 4, 10, 0));
        // Reservamos espacio para el catálogo dentro del área útil de la ventana.
        setPreferredSize(new Dimension(0, 265));
        for (Ficha ficha : DatosDemo.partyActiva()) {
            add(tarjetaActiva(ficha, verDetalle));
        }
    }

    private JPanel tarjetaActiva(Ficha ficha, Consumer<Ficha> verDetalle) {
        JPanel resumen = Estilo.panel(new BorderLayout(8, 0));
        // La imagen ocupa un espacio fijo; las barras usan el ancho restante.
        resumen.add(new JLabel(Imagenes.icono(ficha.imagen, 62, 93)), BorderLayout.WEST);
        JPanel indicadores = Estilo.panel(new GridLayout(4, 1, 0, 3));
        indicadores.add(Estilo.etiqueta("Nivel " + ficha.nivel + " · " + ficha.rol, 13, Font.BOLD));
        indicadores.add(Estilo.barra("HP", ficha.vida, ficha.vidaMaxima, Estilo.ROJO));
        indicadores.add(Estilo.barra("MP", ficha.mana, ficha.manaMaximo, Estilo.AZUL));
        indicadores.add(Estilo.barra("XP", ficha.experiencia, ficha.experienciaSiguiente,
                Estilo.DORADO));
        resumen.add(indicadores, BorderLayout.CENTER);

        JPanel datos = Estilo.panel(new GridLayout(3, 1, 0, 3));
        datos.add(Estilo.etiqueta("Arma: " + ficha.arma, 12, Font.PLAIN));
        datos.add(Estilo.etiqueta(ficha.habilidadPrincipal + " · disponible", 11, Font.PLAIN));
        JLabel bloqueada = Estilo.etiqueta(ficha.habilidadSecundaria + " · bloqueada", 11, Font.PLAIN);
        bloqueada.setForeground(Estilo.TEXTO_SECUNDARIO);
        datos.add(bloqueada);

        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 8));
        cuerpo.add(resumen, BorderLayout.NORTH);
        cuerpo.add(datos, BorderLayout.CENTER);
        JButton detalle = Estilo.boton("VER DETALLE", Estilo.VERDE);
        detalle.addActionListener(evento -> verDetalle.accept(ficha));
        cuerpo.add(detalle, BorderLayout.SOUTH);
        return Estilo.tarjeta(ficha.nombre, cuerpo);
    }
}
