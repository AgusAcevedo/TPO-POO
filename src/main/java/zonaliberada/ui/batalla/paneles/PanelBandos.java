package zonaliberada.ui.batalla.paneles;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import zonaliberada.dtos.*;
import zonaliberada.models.ReglasJuego;
import zonaliberada.ui.*;

/** Seis casillas por bando; las dos líneas frontales se enfrentan en el centro. */
public final class PanelBandos extends PanelSeccion {
    public PanelBandos(BatallaDTO batalla) {
        super(new GridLayout(1, 2, 18, 0));
        setPreferredSize(new Dimension(970, 360));
        add(lado("ALIADOS", batalla.getAliados(), batalla.getActorActualId(), true));
        add(lado("ENEMIGOS", batalla.getEnemigos(), batalla.getActorActualId(), false));
    }

    private JPanel lado(String nombre, List<PersonajeDTO> miembros, String actorId, boolean aliado) {
        JPanel casillas = Estilo.panel(new GridLayout(3, 2, 7, 7));
        for (int fila = 1; fila <= ReglasJuego.CANTIDAD_FRONTALES; fila++) {
            int trasera = fila + ReglasJuego.CANTIDAD_FRONTALES;
            // Invertimos el orden visual para que los frentes de ambos bandos miren al centro.
            int[] posiciones = aliado ? new int[]{trasera, fila} : new int[]{fila, trasera};
            for (int posicion : posiciones) {
                PersonajeDTO ocupante = PersonajeDTO.buscarEnPosicion(miembros, posicion);
                casillas.add(ocupante == null ? vacio(posicion) : personaje(ocupante, ocupante.getId().equals(actorId)));
            }
        }
        JPanel cuerpo = Estilo.panel(new BorderLayout(0, 7));
        JPanel columnas = Estilo.panel(new GridLayout(1, 2));
        for (String texto : aliado ? new String[]{"RETAGUARDIA · 4–6", "FRENTE · 1–3"}
                : new String[]{"FRENTE · 1–3", "RETAGUARDIA · 4–6"}) {
            JLabel etiqueta = Estilo.etiqueta(texto, 12, Font.BOLD);
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            columnas.add(etiqueta);
        }
        cuerpo.add(columnas, BorderLayout.NORTH);
        cuerpo.add(casillas, BorderLayout.CENTER);
        return Estilo.tarjeta(nombre, cuerpo);
    }

    private JPanel personaje(PersonajeDTO ficha, boolean activo) {
        JPanel tarjeta = Estilo.panel(new BorderLayout(0, 4));
        tarjeta.setOpaque(true);
        tarjeta.setBackground(ficha.isVivo() ? Estilo.CREMA : Estilo.GRIS);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(activo ? Estilo.DORADO : Estilo.VERDE, activo ? 3 : 1),
                new EmptyBorder(5, 7, 5, 7)));
        tarjeta.add(Estilo.etiqueta(ficha.getPosicion() + " · " + ficha.getNombre()
                + (activo ? " · ACTÚA" : ""), 12, Font.BOLD), BorderLayout.NORTH);
        JLabel figura = Imagenes.personaje(ficha, 42, 63);
        figura.setEnabled(ficha.isVivo());
        tarjeta.add(figura, BorderLayout.WEST);
        JPanel barras = Estilo.panel(new GridLayout(3, 1, 0, 3));
        barras.add(Estilo.barra("HP", ficha.getVida(), ficha.getVidaMaxima(), Estilo.ROJO));
        barras.add(Estilo.barra("MP", ficha.getMana(), ficha.getManaMaximo(), Estilo.AZUL));
        barras.add(Estilo.barra("INI", ficha.getIniciativa(), ReglasJuego.UMBRAL_INICIATIVA, Estilo.DORADO));
        tarjeta.add(barras, BorderLayout.CENTER);
        String estados = "";
        for (EstadoDTO estado : ficha.getEstados()) {
            if (!estados.isEmpty()) estados += ", ";
            estados += TextosUI.estado(estado.getTipo()) + " (" + estado.getTurnosRestantes() + ")";
        }
        String detalle = "ATQ " + TextosUI.numero(ficha.getAtaque()) + " · DEF "
                + TextosUI.numero(ficha.getDefensa()) + " · VEL " + ficha.getVelocidad();
        if (ficha.getFaseBoss() > 0) detalle += "\nBoss · fase " + ficha.getFaseBoss();
        if (!ficha.getAtributoEspecial().isBlank()) detalle += "\n"
                + ficha.getAtributoEspecial() + ": " + ficha.getValorEspecial();
        if (!estados.isEmpty()) detalle += "\n" + estados;
        tarjeta.setToolTipText("<html>" + TextosUI.html(detalle) + "</html>");
        if (!ficha.isVivo() || !estados.isEmpty() || ficha.getFaseBoss() > 0) {
            JLabel estado = Estilo.etiqueta(!ficha.isVivo() ? "DERROTADO" : ficha.getFaseBoss() > 0
                    ? "FASE " + ficha.getFaseBoss() + (estados.isEmpty() ? "" : " · " + estados) : estados,
                    10, Font.PLAIN);
            tarjeta.add(estado, BorderLayout.SOUTH);
        }
        tarjeta.setName("participante:" + ficha.getId());
        return tarjeta;
    }

    private JPanel vacio(int posicion) {
        JPanel panel = Estilo.panel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createDashedBorder(Estilo.GRIS));
        JLabel texto = Estilo.etiqueta("Posición " + posicion + " vacía", 12, Font.PLAIN);
        texto.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(texto);
        return panel;
    }
}
