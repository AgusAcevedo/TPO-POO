package zonaliberada.ui.batalla.paneles;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import zonaliberada.dtos.*;
import zonaliberada.models.combate.acciones.*;
import zonaliberada.ui.*;

/** Selección visual; la solicitud pasa por ejecutarAccion del backend. */
public final class PanelAcciones extends PanelSeccion {
    private final ContextoUI contexto;
    private final BatallaDTO batalla;
    private final PersonajeDTO actor;
    private final List<ItemDTO> consumibles;
    private final JComboBox<OpcionUI> habilidades = new JComboBox<>();
    private final JComboBox<OpcionUI> items = new JComboBox<>();
    private final JComboBox<OpcionUI> objetivos = new JComboBox<>();
    private final JButton ejecutar = Estilo.boton("EJECUTAR", Estilo.VERDE);
    private final JTextArea descripcion = Estilo.descripcion("");
    private final JButton atacar = Estilo.boton("ATACAR", Estilo.ROJO);
    private final JButton usarHabilidad = Estilo.boton("HABILIDAD", Estilo.AZUL);
    private final JButton defender = Estilo.boton("DEFENDER", Estilo.AZUL);
    private final JButton usarObjeto = Estilo.boton("OBJETO", Estilo.AZUL);
    private final JButton terminar = Estilo.boton("FINALIZAR TURNO", Estilo.GRIS);
    private final JButton abandonar = Estilo.boton("ABANDONAR", Estilo.ROJO);
    private List<PersonajeDTO> candidatos = List.of();
    private Modo modo = Modo.ATACAR;
    // Un panel representa una solicitud; al enviarla se reemplaza por una vista actualizada.
    private boolean solicitudEnviada;

    public PanelAcciones(ContextoUI contexto, BatallaDTO batalla) {
        super(new BorderLayout(0, 6));
        this.contexto = contexto;
        this.batalla = batalla;
        actor = batalla.aliadoActual();
        consumibles = contexto.juego().getInventarioDTO().consumibles();
        inicializarComponentes();
        inicializarEventos();
        actualizarSelecciones();
    }

    /** Construye y acomoda la vista; todavía no conecta los clics. */
    private void inicializarComponentes() {
        JPanel botones = Estilo.panel(new GridLayout(1, 5, 8, 0));
        atacar.setEnabled(actor != null && actor.getPuntosAccion() >= 1);
        usarHabilidad.setEnabled(tieneHabilidadDesbloqueada());
        defender.setEnabled(actor != null && actor.getPuntosAccion() >= 1);
        defender.setToolTipText("1 PA · +50 % de defensa hasta el inicio del próximo turno · no acumula");
        usarObjeto.setEnabled(actor != null && !consumibles.isEmpty());
        terminar.setEnabled(actor != null);
        botones.add(atacar);
        botones.add(usarHabilidad);
        botones.add(defender);
        botones.add(usarObjeto);
        botones.add(terminar);
        add(botones, BorderLayout.NORTH);

        habilidades.setName("habilidades");
        habilidades.getAccessibleContext().setAccessibleName("Habilidad");
        if (actor != null) for (HabilidadDTO h : actor.getHabilidades()) {
            habilidades.addItem(new OpcionUI(h.getNombre(), h.getNombre() + " · " + h.getCostoPA()
                    + " PA / " + h.getCostoMana() + " MP" + (h.isDesbloqueada() ? "" : " · nivel " + h.getNivelDesbloqueo())));
        }
        items.setName("consumibles");
        items.getAccessibleContext().setAccessibleName("Consumible");
        for (ItemDTO item : consumibles) {
            items.addItem(new OpcionUI(item.getId(), item.getNombre() + " × " + item.getCantidad()));
        }
        objetivos.setName("objetivos");
        objetivos.getAccessibleContext().setAccessibleName("Objetivo");
        JPanel selectores = Estilo.panel(new GridLayout(1, 3, 10, 0));
        selectores.add(selector("HABILIDAD", habilidades));
        selectores.add(selector("CONSUMIBLE", items));
        selectores.add(selector("OBJETIVO", objetivos));
        JPanel eleccion = Estilo.panel(new BorderLayout(0, 5));
        eleccion.add(selectores, BorderLayout.NORTH);
        eleccion.add(descripcion, BorderLayout.CENTER);
        add(Estilo.tarjeta(null, eleccion), BorderLayout.CENTER);
        JPanel pie = Estilo.panel(new BorderLayout(8, 0));
        pie.add(ejecutar, BorderLayout.CENTER);
        pie.add(abandonar, BorderLayout.EAST);
        add(pie, BorderLayout.SOUTH);
    }

    /** Los eventos solicitan operaciones al controlador; no implementan reglas del juego. */
    private void inicializarEventos() {
        conectarModo(atacar, Modo.ATACAR);
        conectarModo(usarHabilidad, Modo.HABILIDAD);
        conectarModo(usarObjeto, Modo.OBJETO);
        defender.addActionListener(e -> enviar(new Defender(), new String[0], null, null));
        terminar.addActionListener(e -> {
            if (!solicitudVigente()) return;
            solicitudEnviada = true;
            contexto.juego().finalizarTurno();
            contexto.actualizarCombate();
        });
        habilidades.addActionListener(e -> actualizarSelecciones());
        items.addActionListener(e -> actualizarSelecciones());
        ejecutar.addActionListener(e -> ejecutarSeleccion());
        abandonar.addActionListener(e -> contexto.abandonarBatalla());
    }

    private JPanel selector(String texto, JComboBox<OpcionUI> selector) {
        JPanel panel = Estilo.panel(new BorderLayout(0, 3));
        panel.add(Estilo.etiqueta(texto, 11, Font.BOLD), BorderLayout.NORTH);
        panel.add(selector, BorderLayout.CENTER);
        return panel;
    }

    private void conectarModo(JButton boton, Modo seleccion) {
        // Cambiar el modo solo actualiza opciones; todavía no paga ni ejecuta acciones.
        boton.addActionListener(e -> { modo = seleccion; actualizarSelecciones(); });
    }

    private String elegido(JComboBox<OpcionUI> selector) {
        OpcionUI opcion = (OpcionUI) selector.getSelectedItem();
        return opcion == null ? null : opcion.getId();
    }

    private HabilidadDTO habilidad() {
        if (actor == null) return null;
        String nombre = elegido(habilidades);
        for (HabilidadDTO habilidad : actor.getHabilidades()) {
            if (habilidad.getNombre().equals(nombre)) return habilidad;
        }
        return null;
    }

    private ItemDTO item() {
        String id = elegido(items);
        for (ItemDTO item : consumibles) {
            if (item.getId().equals(id)) return item;
        }
        return null;
    }

    private Accion accion() {
        if (modo == Modo.HABILIDAD) return new UsarHabilidad();
        if (modo == Modo.OBJETO) return new UsarItem();
        return new Atacar();
    }

    private boolean grupal() {
        if (modo == Modo.HABILIDAD) {
            HabilidadDTO habilidad = habilidad();
            return habilidad != null && habilidad.isGrupal();
        }
        if (modo == Modo.OBJETO) {
            ItemDTO item = item();
            return item != null && item.isGrupal();
        }
        return false;
    }

    private boolean tieneHabilidadDesbloqueada() {
        if (actor == null) return false;
        for (HabilidadDTO habilidad : actor.getHabilidades()) {
            if (habilidad.isDesbloqueada()) return true;
        }
        return false;
    }

    private void actualizarSelecciones() {
        String objetivoAnterior = elegido(objetivos);
        String habilidad = modo == Modo.HABILIDAD ? elegido(habilidades) : null;
        String item = modo == Modo.OBJETO ? elegido(items) : null;
        // El backend decide el alcance y el bando; la UI solo muestra sus candidatos.
        candidatos = contexto.juego().objetivosValidos(accion(), habilidad, item);
        objetivos.removeAllItems();
        boolean grupal = grupal();
        if (grupal && !candidatos.isEmpty()) {
            objetivos.addItem(new OpcionUI("", "Todo el grupo (" + candidatos.size() + " objetivos)"));
        } else for (PersonajeDTO candidato : candidatos) {
            OpcionUI opcion = new OpcionUI(candidato.getId(), candidato.getNombre() + " · HP "
                    + candidato.getVida() + "/" + candidato.getVidaMaxima() + " · posición " + candidato.getPosicion());
            objetivos.addItem(opcion);
            if (candidato.getId().equals(objetivoAnterior)) objetivos.setSelectedItem(opcion);
        }
        habilidades.setEnabled(modo == Modo.HABILIDAD && habilidades.getItemCount() > 0);
        items.setEnabled(modo == Modo.OBJETO && items.getItemCount() > 0);
        objetivos.setEnabled(!grupal && !candidatos.isEmpty());
        boolean pagable = actor != null && actor.getPuntosAccion() >= 1;
        String detalle = "ATACAR · 1 PA · Elegí un objetivo y pulsá EJECUTAR.";
        if (modo == Modo.HABILIDAD) {
            HabilidadDTO h = habilidad();
            pagable = h != null && h.isPagable();
            detalle = h == null ? "No hay habilidades disponibles." : h.getNombre() + " · "
                    + h.getCostoPA() + " PA / " + h.getCostoMana() + " MP · " + h.getDescripcion();
            if (h != null && !h.isDesbloqueada()) detalle = "Bloqueada hasta nivel " + h.getNivelDesbloqueo() + ". " + detalle;
            else if (h != null && !h.isPagable()) detalle = "Faltan PA o maná. " + detalle;
        } else if (modo == Modo.OBJETO) {
            ItemDTO i = item();
            pagable &= i != null;
            detalle = i == null ? "No hay consumibles disponibles." : i.getNombre() + " · 1 PA · " + i.getDescripcion();
        }
        descripcion.setText(detalle);
        ejecutar.setEnabled(pagable && !candidatos.isEmpty() && !solicitudEnviada);
    }

    private void ejecutarSeleccion() {
        String[] ids;
        if (grupal()) {
            // Una acción grupal envía todos los candidatos, no un único seleccionado.
            ids = new String[candidatos.size()];
            for (int i = 0; i < candidatos.size(); i++) ids[i] = candidatos.get(i).getId();
        } else {
            String elegido = elegido(objetivos);
            ids = elegido == null ? new String[0] : new String[]{elegido};
        }
        enviar(accion(), ids, modo == Modo.HABILIDAD ? elegido(habilidades) : null,
                modo == Modo.OBJETO ? elegido(items) : null);
    }

    private boolean solicitudVigente() {
        // El actor y la ubicación deben seguir coincidiendo con los mostrados por este panel.
        BatallaDTO actual = contexto.juego().getBatallaDTO();
        return !solicitudEnviada && actor != null && actor.getId().equals(actual.getActorActualId())
                && batalla.getUbicacionId().equals(actual.getUbicacionId());
    }

    private void enviar(Accion accion, String[] ids, String habilidad, String item) {
        if (!solicitudVigente()) return;
        solicitudEnviada = true; // Un botón viejo no envía otra acción sobre el siguiente actor.
        ejecutar.setEnabled(false);
        if (!contexto.juego().ejecutarAccion(accion, ids, habilidad, item)) {
            contexto.avisar(contexto.juego().getUltimoMensaje());
        }
        contexto.actualizarCombate();
    }
    private enum Modo { ATACAR, HABILIDAD, OBJETO }
}
