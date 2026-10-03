package zonaliberada.ui.batalla.paneles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import zonaliberada.ui.DatosDemo;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.Estilo;
import zonaliberada.ui.Imagenes;
import zonaliberada.ui.PanelSeccion;

/** Tablero de seis casillas por bando, con las líneas frontales enfrentadas. */
public final class PanelBandos extends PanelSeccion {
    public PanelBandos() {
        super(new GridLayout(1, 2, 18, 0));
        add(aliados());
        add(enemigos());
    }

    private JPanel aliados() {
        // GridLayout llena cada fila de izquierda a derecha:
        // Twittero     | Gaucho
        // Vacío        | Médico
        // Electricista | Vacío
        JPanel casillas = Estilo.panel(new GridLayout(3, 2, 7, 7));
        casillas.add(personaje(DatosDemo.ALIADOS.get(2), false));
        casillas.add(personaje(DatosDemo.ALIADOS.get(0), true));
        casillas.add(vacio());
        casillas.add(personaje(DatosDemo.ALIADOS.get(1), false));
        casillas.add(personaje(DatosDemo.ALIADOS.get(3), false));
        casillas.add(vacio());
        return lado("ALIADOS", "LÍNEA TRASERA", "LÍNEA FRONTAL", casillas);
    }

    private JPanel enemigos() {
        // En el enemigo invertimos las columnas para que su frente quede
        // junto al frente de los aliados, en el centro del tablero.
        JPanel casillas = Estilo.panel(new GridLayout(3, 2, 7, 7));
        casillas.add(personaje(DatosDemo.ENEMIGOS.get(0), false));
        casillas.add(personaje(DatosDemo.ENEMIGOS.get(3), false));
        casillas.add(personaje(DatosDemo.ENEMIGOS.get(1), false));
        casillas.add(vacio());
        casillas.add(personaje(DatosDemo.ENEMIGOS.get(2), false));
        casillas.add(vacio());
        return lado("ENEMIGOS", "LÍNEA FRONTAL", "LÍNEA TRASERA", casillas);
    }

    /** Agrega los nombres de las dos líneas encima de una grilla ya construida. */
    private JPanel lado(String titulo, String columnaIzquierda, String columnaDerecha,
            JPanel casillas) {
        JPanel lado = Estilo.panel(new BorderLayout(0, 6));
        JPanel encabezado = Estilo.panel(new GridLayout(2, 1));
        JLabel nombre = Estilo.etiqueta(titulo, 16, Font.BOLD);
        nombre.setHorizontalAlignment(SwingConstants.CENTER);
        encabezado.add(nombre);
        JPanel columnas = Estilo.panel(new GridLayout(1, 2));
        JLabel izquierda = Estilo.etiqueta(columnaIzquierda, 12, Font.BOLD);
        JLabel derecha = Estilo.etiqueta(columnaDerecha, 12, Font.BOLD);
        izquierda.setHorizontalAlignment(SwingConstants.CENTER);
        derecha.setHorizontalAlignment(SwingConstants.CENTER);
        columnas.add(izquierda);
        columnas.add(derecha);
        encabezado.add(columnas);
        lado.add(encabezado, BorderLayout.NORTH);
        lado.add(casillas, BorderLayout.CENTER);
        return lado;
    }

    private JPanel personaje(Ficha ficha, boolean activo) {
        JPanel tarjeta = Estilo.panel(new BorderLayout(0, 4));
        tarjeta.setOpaque(true);
        // Un borde más grueso y dorado identifica al personaje que está actuando.
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(activo ? Estilo.DORADO : Estilo.VERDE, activo ? 3 : 1),
                new EmptyBorder(5, 7, 5, 7)));
        tarjeta.add(Estilo.etiqueta(ficha.nombre + (activo ? " · ACTÚA" : ""), 13, Font.BOLD),
                BorderLayout.NORTH);
        // El sprite es solo visual: la posición y los valores siguen siendo
        // datos de muestra, no un sistema de combate implementado.
        JLabel figura = new JLabel(Imagenes.icono(ficha.imagen, 54, 81));
        figura.setHorizontalAlignment(SwingConstants.CENTER);
        tarjeta.add(figura, BorderLayout.WEST);
        JPanel barras = Estilo.panel(new GridLayout(3, 1, 0, 3));
        barras.add(Estilo.barra("HP", ficha.vida, ficha.vidaMaxima, Estilo.ROJO));
        barras.add(Estilo.barra("MP", ficha.mana, ficha.manaMaximo, Estilo.AZUL));
        JPanel iniciativa = Estilo.barra("INI", ficha.iniciativa, 100, Estilo.DORADO);
        iniciativa.setToolTipText("Iniciativa: al llegar a 100 comienza el turno. Valor de muestra.");
        barras.add(iniciativa);
        tarjeta.add(barras, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel vacio() {
        // Conservamos la casilla aunque no haya un personaje: son seis por bando.
        JPanel tarjeta = Estilo.panel(new BorderLayout());
        tarjeta.setOpaque(true);
        tarjeta.setBorder(BorderFactory.createDashedBorder(Estilo.GRIS));
        JLabel texto = Estilo.etiqueta("Posición vacía", 12, Font.PLAIN);
        texto.setHorizontalAlignment(SwingConstants.CENTER);
        tarjeta.add(texto, BorderLayout.CENTER);
        return tarjeta;
    }
}
