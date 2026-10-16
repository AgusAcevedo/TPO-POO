package zonaliberada.models.personajes;

import java.util.List;
import java.util.ArrayList;

import zonaliberada.models.ReglasJuego;
import zonaliberada.models.combate.EstadoAlterado;
import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.combate.TipoEstado;


/** Base común de aliados y enemigos; las subclases agregan sus reglas particulares. */
public abstract class Entidad {

    private final String nombre;

    private int vida;
    private int vidaMaxima;
    private int mana;
    private int manaMaximo;

    private int ataqueBase;
    private int defensaBase;

    private int velocidadBase;
    // La precarga se conserva; la iniciativa actual cambia durante el combate.
    private final int iniciativaInicial;
    private int iniciativa;
    private final boolean ataqueADistancia;

    private int puntosAccion;
    private boolean turnoActivo; // Indica si la entidad ya inició su turno y todavía no lo terminó.
    private boolean enBatalla;

    private final List<Habilidad> habilidades;
    private final List<EstadoAlterado> estados = new ArrayList<>();

    /** Inicializa los datos comunes; las subclases lo llaman con super(...). */
    protected Entidad(
            String nombre,
            int vidaMaxima,
            int manaMaximo,
            int ataqueBase,
            int defensaBase,
            int velocidadBase,
            int iniciativa,
            List<Habilidad> habilidades,
            boolean ataqueDistancia) {

        // Si los datos son inválidos, no permitimos crear la entidad.
        if (nombre == null || nombre.isBlank()) { throw new IllegalArgumentException("El nombre es obligatorio"); }
        if (vidaMaxima <= 0) { throw new IllegalArgumentException("La vida máxima debe ser positiva"); }
        if (manaMaximo <= 0) { throw new IllegalArgumentException("El maná máximo debe ser positivo"); }

        // La velocidad base debe permitir que avance la iniciativa.
        if (ataqueBase < 0 || defensaBase < 0 || velocidadBase <= 0) { throw new IllegalArgumentException("Las estadísticas son inválidas"); }

        // Una precarga de 100 permite estar listo desde el comienzo.
        if (iniciativa < 0 || iniciativa > ReglasJuego.UMBRAL_INICIATIVA) {
            throw new IllegalArgumentException("La iniciativa debe estar entre 0 y " + ReglasJuego.UMBRAL_INICIATIVA);
        }

        // Sin habilidades se pasa una lista vacía, no null.
        if (habilidades == null) { throw new IllegalArgumentException("La lista de habilidades no puede ser null"); }

        for (Habilidad habilidad : habilidades) {
            if (habilidad == null) {
                throw new IllegalArgumentException("La lista de habilidades no puede contener null");
            }
        }

        this.nombre = nombre.strip();

        // Toda entidad nueva empieza con sus recursos completos.
        this.vida = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.mana = manaMaximo;
        this.manaMaximo = manaMaximo;

        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.velocidadBase = velocidadBase;

        this.iniciativaInicial = iniciativa;
        this.iniciativa = iniciativa;

        // Las habilidades son configuración; no cambian durante la partida.
        this.habilidades = List.copyOf(habilidades);

        this.ataqueADistancia = ataqueDistancia;

        this.puntosAccion = 0;
        this.turnoActivo = false;
    }

    public String getNombre() { return nombre; }

    public int getVida() {
        return vida;
    }
    public int getVidaMaxima() { return vidaMaxima; }

    public int getMana() {
        return mana;
    }
    public int getManaMaximo() { return manaMaximo; }
    public int getAtaqueBase() { return ataqueBase; }
    public int getDefensaBase() { return defensaBase; }
    public int getVelocidadBase() { return velocidadBase; }

    public int getIniciativaInicial() {
        return iniciativaInicial;
    }

    public int getIniciativa() {
        return iniciativa;
    }

    public int getPuntosAccion() {
        return puntosAccion;
    }

    public int getPuntosAccionMaximos() {
        return ReglasJuego.PA_POR_TURNO;
    }

    public boolean isAtaqueDistancia() {
        return ataqueADistancia;
    }

    // Por defecto, una entidad no tiene probabilidad de crítico.
    public double getProbabilidadCritico() {
        return 0.0;
    }

    public boolean estaVivo() {
        return vida > 0;
    }
    public boolean estaEnBatalla() { return enBatalla; }

    public List<Habilidad> getHabilidades() { return habilidades; }

    public List<EstadoAlterado> getEstados() {
        List<EstadoAlterado> resultado = new ArrayList<>();
        for (EstadoAlterado estado : estados) {
            // La duración no debe poder cambiarse desde otra clase.
            resultado.add(estado.copia());
        }
        return List.copyOf(resultado);
    }

    public void prepararParaBatalla() {
        iniciativa = iniciativaInicial;
        puntosAccion = 0;
        turnoActivo = false;

        // Los estados temporales no pasan a la siguiente batalla.
        estados.clear();
    }

    public void comenzarBatalla() {
        if (enBatalla) {
            throw new IllegalStateException("La entidad ya participa en una batalla");
        }
        enBatalla = true;
    }

    public void terminarBatalla() {
        finalizarTurno();
        enBatalla = false;
        estados.clear();
    }

    public void recibirDanio(int danio) {
        // Ataque y defensa ya se resolvieron en EfectoDanio.
        if (danio < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo");}

        // Descontamos vida, pero nunca permitimos que quede negativa.
        int danioRecibido = danio;
        for (EstadoAlterado estado : estados) {
            danioRecibido = estado.reducirDanio(danioRecibido);
        }
        vida = Math.max(0, vida - danioRecibido);
    }

    public void curar(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La curación no puede ser negativa");
        }

        // Una curación normal no revive a una entidad derrotada.
        if (!estaVivo()) { return; }

        // Calculamos cuánto falta para llegar a la vida máxima. Recuperamos la cantidad solicitada o lo que falta.
        int vidaFaltante = getVidaMaxima() - vida;
        vida += Math.min(cantidad, vidaFaltante);
    }

    public void recuperarVidaCompleta() {
        vida = getVidaMaxima();
    }

    public void recuperarMana(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La recuperación de maná no puede ser negativa");
        }

        // Calculamos cuánto falta para llegar al mana máximo. Recuperamos la cantidad solicitada o lo que falta.
        int manaFaltante = getManaMaximo() - mana;
        mana += Math.min(cantidad, manaFaltante);
    }

    public void recuperarManaCompleto() {
        mana = getManaMaximo();
    }

    public boolean tienePA(int costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        return puntosAccion >= costo;
    }

    public boolean gastarPA(int costo) {
        if (!tienePA(costo)) { return false; }

        puntosAccion -= costo;
        return true; // El gasto se realizó correctamente.
    }

    public boolean tieneMana(int costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        return mana >= costo;
    }

    public boolean gastarMana(int costo) {
        if (!tieneMana(costo)) { return false; }

        mana -= costo;
        return true; // El gasto se realizó correctamente.
    }

    public boolean puedeActuar() {
        // El aturdimiento quita un PA al abrir el turno, no bloquea la barra.
        return estaVivo() && iniciativa >= ReglasJuego.UMBRAL_INICIATIVA;
    }

    public void actualizarIniciativa() {
        // Una entidad derrotada o con turno activo no carga su barra.
        if (!estaVivo() || turnoActivo) { return; }

        // Cada avance suma velocidad, sin superar el umbral.
        int iniciativaFaltante = ReglasJuego.UMBRAL_INICIATIVA - iniciativa;
        iniciativa += Math.min(velocidadTotal(), iniciativaFaltante);
    }

    public boolean iniciarTurno() {
        if (turnoActivo || !puedeActuar()) {
            return false;
        }

        // Aturdido hace perder una acción en este turno.
        boolean aturdido = tieneEstado(TipoEstado.ATURDIDO);

        // Duración 1: vence al comenzar el próximo turno propio.
        // Primero vencen los modificadores; luego se resuelve el veneno.
        // Así un escudo que vence no depende del orden de inserción.
        for (EstadoAlterado estado : estados) {
            if (estado.getTipo() != TipoEstado.VENENO) {
                estado.alInicioTurno(this);
            }
        }
        for (EstadoAlterado estado : estados) {
            if (estado.getTipo() == TipoEstado.VENENO) {
                estado.alInicioTurno(this);
            }
        }

        // Recorremos hacia atrás para poder quitar elementos de la lista.
        for (int i = estados.size() - 1; i >= 0; i--) {
            if (estados.get(i).finalizo()) estados.remove(i);
        }

        turnoActivo = true;
        puntosAccion = estaVivo()
                ? Math.max(0, ReglasJuego.PA_POR_TURNO - (aturdido ? 1 : 0))
                : 0;
        return true;
    }

    public void finalizarTurno() {
        // Si no hay turno abierto, no cambiamos nada.
        if (!turnoActivo) { return; }

        // Los PA sobrantes no se acumulan.
        puntosAccion = 0;

        // La siguiente carga comienza desde cero, no desde la precarga.
        iniciativa = 0;
        turnoActivo = false;
    }

    public double ataqueTotal() {
        int ataque = ataqueSinEstados();
        EstadoAlterado aumento = buscarEstado(TipoEstado.AUMENTO_ATAQUE);
        EstadoAlterado reduccion = buscarEstado(TipoEstado.REDUCCION_ATAQUE);
        // El aumento siempre se aplica antes que la reducción.
        if (aumento != null) ataque = aumento.modificarAtaque(ataque);
        if (reduccion != null) ataque = reduccion.modificarAtaque(ataque);
        return ataque;
    }

    public double defensaTotal() {
        int defensa = defensaSinEstados();

        EstadoAlterado aumento = buscarEstado(TipoEstado.AUMENTO_DEFENSA);
        EstadoAlterado reduccion = buscarEstado(TipoEstado.REDUCCION_DEFENSA);
        if (aumento != null) defensa = aumento.modificarDefensa(defensa);
        if (reduccion != null) defensa = reduccion.modificarDefensa(defensa);

        return defensa;
    }

    public int velocidadTotal() {
        int velocidad = velocidadSinEstados();
        for (EstadoAlterado estado : estados) {
            velocidad = estado.modificarVelocidad(velocidad);
        }
        // Siempre puede cargar iniciativa, aun con una reducción del 100 %.
        return Math.max(1, velocidad);
    }

    public Habilidad[] habilidadesDisponibles() {
        // Devolvemos un arreglo nuevo con las habilidades configuradas.
        // Las entidades sin progresión, como los enemigos, usan todas ellas.
        return habilidades.toArray(new Habilidad[0]);
    }

    public void agregarEstado(EstadoAlterado estado) {
        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado es obligatorio");
        }

        if (!estaVivo() || estado.finalizo()) {
            return;
        }

        // Reemplazamos el estado del mismo tipo: el bonus no se acumula.
        for (int i = estados.size() - 1; i >= 0; i--) {
            EstadoAlterado actual = estados.get(i);
            if (actual.finalizo() || actual.getTipo() == estado.getTipo()) {
                estados.remove(i);
            }
        }
        estados.add(estado.copia());
    }

    public void eliminarEstadosNegativos() {
        for (int i = estados.size() - 1; i >= 0; i--) {
            if (estados.get(i).getTipo().esNegativo()) estados.remove(i);
        }
    }

    public boolean tieneEstado(TipoEstado tipo) {
        return buscarEstado(tipo) != null;
    }

    protected int ataqueSinEstados() { return ataqueBase; }
    protected int defensaSinEstados() { return defensaBase; }
    protected int velocidadSinEstados() { return velocidadBase; }

    protected void aumentarEstadisticas(int vida, int mana, int ataque, int defensa, int velocidad) {
        if (vida < 0 || mana < 0 || ataque < 0 || defensa < 0 || velocidad < 0) {
            throw new IllegalArgumentException("Las mejoras no pueden ser negativas");
        }
        vidaMaxima = sumarSinDesbordar(vidaMaxima, vida);
        manaMaximo = sumarSinDesbordar(manaMaximo, mana);
        ataqueBase = sumarSinDesbordar(ataqueBase, ataque);
        defensaBase = sumarSinDesbordar(defensaBase, defensa);
        velocidadBase = sumarSinDesbordar(velocidadBase, velocidad);
        // Subir de nivel no cura ni revive; recuperarse es otra operación.
    }

    protected void ajustarRecursosAMaximos() {
        vida = Math.min(vida, getVidaMaxima());
        mana = Math.min(mana, getManaMaximo());
    }

    protected static int sumarSinDesbordar(int base, int bonus) {
        // long evita que la suma se vuelva negativa antes de aplicar el límite de int.
        return (int) Math.min(Integer.MAX_VALUE, (long) base + bonus);
    }

    private EstadoAlterado buscarEstado(TipoEstado tipo) {
        for (EstadoAlterado estado : estados) {
            if (estado.getTipo() == tipo && !estado.finalizo()) return estado;
        }
        return null;
    }
}
