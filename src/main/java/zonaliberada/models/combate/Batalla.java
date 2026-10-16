package zonaliberada.models.combate;

import java.util.ArrayList;
import java.util.List;
import zonaliberada.models.Party;
import zonaliberada.models.mapa.UbicacionMapa;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.models.personajes.Enemigo;
import zonaliberada.models.personajes.Entidad;

/** Conserva participantes, iniciativa y resultado; no conoce botones ni pantallas. */
public class Batalla {

    // Cada batalla conserva su party, sus enemigos y su ubicación.
    private final Party party;
    private final FormacionCombate formacionEnemiga;
    private final UbicacionMapa ubicacion;
    private FormacionCombate formacionAliada;

    // Estos valores cambian durante el combate.
    private Entidad actorActual;
    private EstadoBatalla estado;
    private final Recompensa recompensa;
    private boolean recompensaEntregada;
    private final List<String> mensajes = new ArrayList<>();

    public Batalla(
            Party party,
            FormacionCombate formacionEnemiga,
            UbicacionMapa ubicacion,
            Recompensa recompensa) {

        if (party == null) {
            throw new IllegalArgumentException(
                    "La party es obligatoria");
        }

        if (formacionEnemiga == null
                || formacionEnemiga.getBando() != TipoBando.ENEMIGO) {

            throw new IllegalArgumentException(
                    "La batalla necesita una formación enemiga");
        }

        if (ubicacion == null) {
            throw new IllegalArgumentException(
                    "La ubicación es obligatoria");
        }

        if (recompensa == null) {
            throw new IllegalArgumentException(
                    "La recompensa es obligatoria");
        }

        this.party = party;

        // Copiamos las posiciones, no los objetos Enemigo.
        this.formacionEnemiga = formacionEnemiga.copia();
        this.ubicacion = ubicacion;
        this.recompensa = recompensa;

        this.actorActual = null;
        this.estado = EstadoBatalla.NO_INICIADA;
        this.recompensaEntregada = false;
    }

    public EstadoBatalla getEstado() {
        return estado;
    }

    public String getMensaje() { return String.join("\n", mensajes); }

    /** Historial breve: conserva también las acciones automáticas de los enemigos. */
    public void registrarMensaje(String mensaje) {
        if (mensaje.isBlank()) return;
        mensajes.add(mensaje);
        if (mensajes.size() > 10) mensajes.remove(0);
    }

    public UbicacionMapa getUbicacion() {
        return ubicacion;
    }

    public Entidad obtenerActorActual() {
        // Será null hasta que seleccionemos al primer actor.
        return actorActual;
    }

    public FormacionCombate formacionAliadaActual() {
        return formacionAliada == null ? party.formacionActual() : formacionAliada.copia();
    }

    public FormacionCombate getFormacionEnemiga() { return formacionEnemiga.copia(); }

    public Aliado[] aliados() {
        // Solo los seleccionados, no todo el roster.
        Entidad[] seleccionados = formacionAliadaInterna().participantes();
        Aliado[] aliados = new Aliado[seleccionados.length];

        for (int i = 0; i < seleccionados.length; i++) {
            aliados[i] = (Aliado) seleccionados[i];
        }

        return aliados;
    }

    public Enemigo[] enemigos() {
        Entidad[] seleccionados = formacionEnemiga.participantes();
        Enemigo[] enemigos = new Enemigo[seleccionados.length];

        for (int i = 0; i < seleccionados.length; i++) {
            // También admite Boss, que hereda de Enemigo.
            enemigos[i] = (Enemigo) seleccionados[i];
        }

        return enemigos;
    }

    public Entidad[] participantes() {
        Aliado[] aliados = aliados();
        Enemigo[] enemigos = enemigos();

        Entidad[] participantes =
                new Entidad[aliados.length + enemigos.length];

        int indice = 0;

        for (Aliado aliado : aliados) {
            participantes[indice++] = aliado;
        }

        for (Enemigo enemigo : enemigos) {
            participantes[indice++] = enemigo;
        }

        // Incluye derrotados: siguen siendo participantes del encuentro.
        return participantes;
    }

    public boolean iniciar() {
        // No permite reiniciar una batalla que ya comenzó.
        if (estado != EstadoBatalla.NO_INICIADA) {
            return false;
        }

        // Validamos antes de modificar a los participantes.
        // No exigimos aliados vivos: se recuperarán al prepararlos.
        if (!party.formacionValida()
                || !formacionEnemiga.hayMiembrosVivos()) {
            return false;
        }

        for (Entidad participante : participantes()) {
            if (participante.estaEnBatalla()) return false;
        }

        // La selección queda congelada para este encuentro y sus recompensas.
        formacionAliada = party.formacionActual();

        for (Entidad participante : participantes()) {
            // Java ejecuta la preparación correspondiente a cada clase.
            // Los aliados también recuperan toda su vida y su maná.
            participante.prepararParaBatalla();
            participante.comenzarBatalla();
        }

        estado = EstadoBatalla.EN_CURSO;
        return true;
    }

    public Entidad avanzarHastaSiguienteActor() {
        // No seleccionamos actores antes del inicio ni después del resultado.
        if (verificarResultado() != EstadoBatalla.EN_CURSO) {
            return null;
        }

        if (actorActual != null) {
            // Consultar otra vez no abre otro turno ni vuelve a cargar PA.
            if (actorActual.estaVivo()) {
                return actorActual;
            }

            // Si murió durante su turno, lo cerramos antes de continuar.
            finalizarTurno();
        }

        Entidad[] participantes = participantes();

        // La etiqueta permite retomar la búsqueda si un estado deja al actor sin poder actuar.
        buscarActor: while (true) {
            // Primero atendemos a quienes ya tienen la barra completa.
            // El orden del arreglo resuelve los empates.
            for (Entidad participante : participantes) {
                if (participante.puedeActuar()) {
                    if (!participante.iniciarTurno()) {
                        // Detecta un turno abierto fuera del flujo de Batalla.
                        throw new IllegalStateException(
                                "El participante ya tiene un turno abierto");
                    }

                    actorActual = participante;
                    // El veneno puede derrotarlo al abrir su turno.
                    if (verificarResultado() != EstadoBatalla.EN_CURSO) return null;
                    if (!participante.estaVivo() || participante.getPuntosAccion() == 0) {
                        finalizarTurno();
                        continue buscarActor;
                    }
                    return actorActual;
                }
            }

            // Si nadie está listo, todas las barras avanzan juntas.
            // Entidad ya evita cargar iniciativa a los derrotados.
            for (Entidad participante : participantes) {
                participante.actualizarIniciativa();
            }
        }
    }

    public void finalizarTurno() {
        if (actorActual != null) {
            // Entidad elimina PA sobrantes y vuelve su iniciativa a cero.
            actorActual.finalizarTurno();
            actorActual = null;
        }

        // Cerrar un turno no selecciona automáticamente al siguiente actor.
        verificarResultado();
    }

    public EstadoBatalla verificarResultado() {
        // Una batalla terminada conserva su resultado.
        if (estado != EstadoBatalla.EN_CURSO) {
            return estado;
        }

        if (!formacionAliadaInterna().hayMiembrosVivos()) {
            // Si ambos bandos quedan sin vivos, priorizamos la derrota.
            estado = EstadoBatalla.DERROTA;
        } else if (!formacionEnemiga.hayMiembrosVivos()) {
            estado = EstadoBatalla.VICTORIA;
        }

        if (estado != EstadoBatalla.EN_CURSO && actorActual != null) {
            // Al terminar la batalla no debe quedar un turno abierto.
            actorActual.finalizarTurno();
            actorActual = null;
        }

        if (estado != EstadoBatalla.EN_CURSO) {
            for (Entidad participante : participantes()) {
                participante.terminarBatalla();
            }
        }

        return estado;
    }

    public Recompensa entregarRecompensa() {

        // Consultar antes de ganar no entrega ni consume el premio.
        if (verificarResultado() != EstadoBatalla.VICTORIA
                || recompensaEntregada) {

            return null;
        }

        // Impide entregar nuevamente el premio de este encuentro.
        recompensaEntregada = true;
        return recompensa;
    }

    public Entidad[] objetivosValidos(
            Entidad origen,
            boolean afectaAliados,
            boolean aDistancia) {

        // No ofrecemos objetivos fuera de una batalla en curso.
        if (verificarResultado() != EstadoBatalla.EN_CURSO
                || origen == null
                || !origen.estaVivo()) {
            return new Entidad[0];
        }

        FormacionCombate formacionAliada = formacionAliadaInterna();
        FormacionCombate formacionObjetivo;

        if (formacionAliada.contiene(origen)) {
            // Para un aliado, su propio equipo es la formación aliada.
            formacionObjetivo = afectaAliados
                    ? formacionAliada
                    : formacionEnemiga;

        } else if (formacionEnemiga.contiene(origen)) {
            // Para un enemigo, sus aliados son los otros enemigos.
            formacionObjetivo = afectaAliados
                    ? formacionEnemiga
                    : formacionAliada;

        } else {
            // No admite reservas ni entidades ajenas a este encuentro.
            return new Entidad[0];
        }

        // La formación ya conoce las filas y filtra a los derrotados.
        // Devolvemos todos los candidatos, no elegimos uno automáticamente.
        return formacionObjetivo.objetivosAlcanzables(aDistancia);
    }

    public Entidad enemigoEn(int posicion) {
        return formacionEnemiga.ocupanteEn(posicion);
    }

    public int posicionDe(Entidad entidad) {
        if (entidad instanceof Aliado) return formacionAliadaInterna().posicionDe(entidad);
        return formacionEnemiga.posicionDe(entidad);
    }

    public String idDe(Entidad entidad) {
        // Los aliados tienen identidad permanente; los enemigos se identifican por su posición.
        int posicion = posicionDe(entidad);
        if (posicion == 0) return null;
        if (entidad instanceof Aliado aliado) {
            return "aliado:" + aliado.getIdPersonaje();
        }
        return "enemigo:" + posicion;
    }

    public Entidad buscarParticipante(String id) {
        if (id == null) return null;
        for (Entidad participante : participantes()) {
            if (id.equals(idDe(participante))) return participante;
        }
        return null;
    }

    public boolean agregarEnemigo(Enemigo enemigo, int posicion) {
        if (verificarResultado() != EstadoBatalla.EN_CURSO || enemigo == null
                || !enemigo.estaVivo() || enemigo.estaEnBatalla()
                || !formacionEnemiga.colocar(enemigo, posicion)) return false;
        enemigo.prepararParaBatalla();
        enemigo.comenzarBatalla();
        return true;
    }

    private FormacionCombate formacionAliadaInterna() {
        // Dentro de Batalla usamos la formación congelada, sin copiarla en cada consulta.
        return formacionAliada == null ? party.formacionActual() : formacionAliada;
    }
}
