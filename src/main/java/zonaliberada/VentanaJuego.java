package zonaliberada;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.*;

import zonaliberada.controllers.ControladorPartida;
import zonaliberada.ui.*;
import zonaliberada.ui.batalla.PantallaBatalla;
import zonaliberada.ui.derrota.PantallaDerrota;
import zonaliberada.ui.inicio.PantallaInicio;
import zonaliberada.ui.inventario.PantallaInventario;
import zonaliberada.ui.mapa.PantallaMapaPrincipal;
import zonaliberada.ui.party.PantallaParty;
import zonaliberada.ui.personaje.PantallaDetallePersonaje;
import zonaliberada.ui.victoria.PantallaVictoria;
import zonaliberada.ui.zona.PantallaMapaZona;

/** Una ventana y una sesión compartida; CardLayout no crea otra partida al navegar. */
public final class VentanaJuego extends JFrame {
    // CardLayout conserva las pantallas y muestra una por vez, identificada por PantallaId.
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final Map<PantallaId, PantallaBase> pantallas = new EnumMap<>(PantallaId.class);
    private final ContextoUI contexto;

    public VentanaJuego() { this(new ControladorPartida()); }

    /** Permite usar un controlador existente y conservar su misma sesión. */
    public VentanaJuego(ControladorPartida controlador) {
        super("Zona Liberada");
        // Las referencias this::... y las lambdas se guardan para ejecutarlas cuando se soliciten.
        contexto = new ContextoUI(controlador, this::mostrar,
                mensaje -> JOptionPane.showMessageDialog(this, mensaje, "Zona Liberada",
                        JOptionPane.INFORMATION_MESSAGE),
                mensaje -> JOptionPane.showConfirmDialog(this, mensaje, "Zona Liberada",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION,
                this::cerrarJuego);

        inicializarComponentes();
        inicializarEventos();
        mostrar(PantallaId.INICIO);
    }

    private void inicializarComponentes() {
        // El cierre debe pasar por la confirmación de pérdida de progreso.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 700));
        setSize(1280, 800);
        setLocationRelativeTo(null);
        agregar(PantallaId.INICIO, new PantallaInicio(contexto));
        agregar(PantallaId.MAPA_PRINCIPAL, new PantallaMapaPrincipal(contexto));
        agregar(PantallaId.MAPA_ZONA, new PantallaMapaZona(contexto));
        agregar(PantallaId.PARTY, new PantallaParty(contexto));
        agregar(PantallaId.INVENTARIO, new PantallaInventario(contexto));
        agregar(PantallaId.DETALLE_PERSONAJE, new PantallaDetallePersonaje(contexto));
        agregar(PantallaId.BATALLA, new PantallaBatalla(contexto));
        agregar(PantallaId.VICTORIA, new PantallaVictoria(contexto));
        agregar(PantallaId.DERROTA, new PantallaDerrota(contexto));

        setContentPane(contenido);
    }

    private void inicializarEventos() {
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent evento) { cerrarJuego(); }
        });
    }

    public void mostrar(PantallaId id) {
        PantallaBase pantalla = pantallas.get(id);
        // Se reconstruye la información visible antes de mostrar una pantalla ya existente.
        pantalla.actualizar();
        tarjetas.show(contenido, id.name());
        contenido.revalidate();
        contenido.repaint();
    }

    private void agregar(PantallaId id, PantallaBase pantalla) {
        pantallas.put(id, pantalla);
        contenido.add(pantalla, id.name());
    }

    private void cerrarJuego() {
        if (contexto.prepararSalida()) dispose();
    }
}
