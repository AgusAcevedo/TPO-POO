package zonaliberada;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import zonaliberada.ui.DatosDemo.Ficha;
import zonaliberada.ui.batalla.PantallaBatalla;
import zonaliberada.ui.derrota.PantallaDerrota;
import zonaliberada.ui.inicio.PantallaInicio;
import zonaliberada.ui.inventario.PantallaInventario;
import zonaliberada.ui.mapa.PantallaMapaPrincipal;
import zonaliberada.ui.party.PantallaParty;
import zonaliberada.ui.personaje.PantallaDetallePersonaje;
import zonaliberada.ui.victoria.PantallaVictoria;
import zonaliberada.ui.zona.PantallaMapaZona;

/** Una sola ventana que alterna entre paneles; todavía no es un controlador del juego. */
public final class VentanaJuego extends JFrame {
    // CardLayout funciona como un mazo: todos los paneles están registrados,
    // pero solo uno es visible. Cambiar de pantalla no crea otra ventana.
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final PantallaDetallePersonaje detalle;

    public VentanaJuego() {
        super("Zona Liberada");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
        setSize(1280, 720);
        // null indica que la ventana debe centrarse en la pantalla del equipo.
        setLocationRelativeTo(null);

        // Creamos cada vista una sola vez. this::mostrar pasa a los paneles
        // la función que deben llamar cuando quieran navegar a otra pantalla.
        detalle = new PantallaDetallePersonaje(this::mostrar);
        agregar(PantallaId.INICIO, new PantallaInicio(this::mostrar));
        agregar(PantallaId.MAPA_PRINCIPAL, new PantallaMapaPrincipal(this::mostrar));
        agregar(PantallaId.MAPA_ZONA, new PantallaMapaZona(this::mostrar));
        agregar(PantallaId.PARTY, new PantallaParty(this::mostrar, this::verDetalle));
        agregar(PantallaId.INVENTARIO, new PantallaInventario(this::mostrar));
        agregar(PantallaId.DETALLE_PERSONAJE, detalle);
        agregar(PantallaId.BATALLA, new PantallaBatalla(this::mostrar));
        agregar(PantallaId.VICTORIA, new PantallaVictoria(this::mostrar));
        agregar(PantallaId.DERROTA, new PantallaDerrota(this::mostrar));

        // Instalamos el panel principal como contenido de la ventana.
        setContentPane(contenido);
        mostrar(PantallaId.INICIO);
    }

    private void agregar(PantallaId id, JPanel pantalla) {
        // name() convierte el identificador del enum en la clave de CardLayout.
        contenido.add(pantalla, id.name());
    }

    public void mostrar(PantallaId id) {
        tarjetas.show(contenido, id.name());
    }

    private void verDetalle(Ficha ficha) {
        // Solo actualizamos datos de muestra de la vista: no modificamos la party.
        detalle.mostrarFicha(ficha);
        mostrar(PantallaId.DETALLE_PERSONAJE);
    }
}
