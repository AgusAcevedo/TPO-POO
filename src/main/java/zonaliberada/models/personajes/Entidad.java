package zonaliberada.models.personajes;

import zonaliberada.models.combate.EstadoAlterado;
import zonaliberada.models.combate.Habilidad;

import java.util.List;
import java.util.Set;

public abstract class Entidad {
    private String nombre;
    private int vida;
    private int vidaMaxima;
    private int mana;
    private int manaMaximo;
    private int ataqueBase;
    private int defensaBase;
    private int velocidadBase;
    private int iniciativa;
    private int puntosAccion;
    private int puntosAccionMaximos;
    private List<Habilidad> habilidades;
    private Set<EstadoAlterado> estados;
    private boolean ataqueADistancia;

    // Permite preparar constructores vacíos en las subclases.
    protected Entidad() {
    }

    public Entidad(String nombre, int vida, int vidaMaxima, int mana, int manaMaximo, int ataqueBase, int defensaBase, int velocidadBase, int iniciativa, List<Habilidad> habilidades, Set<EstadoAlterado> estados, boolean ataqueDistancia) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vidaMaxima;
        this.mana = mana;
        this.manaMaximo = manaMaximo;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.velocidadBase = velocidadBase;
        this.iniciativa = iniciativa;
        this.habilidades = habilidades;
        this.estados = estados;
        this.ataqueADistancia = ataqueDistancia;
    }

    public boolean isAtaqueDistancia() {
        return ataqueADistancia;
    }

    public boolean puedeActuar() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public void actualizarIniciativa() {
    }

    public double ataqueTotal() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void recibirDanio(int danio) {
    }

    public void curar(int cantidad) {
    }

    public void recuperarVidaCompleta() {
        vida = vidaMaxima;
    }

    public void recuperarMana(int cantidad) {
    }

    public boolean tienePA(int costo) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean tieneMana(int costo) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean gastarPA(int costo) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public boolean gastarMana(int costo) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void agregarEstado(EstadoAlterado estado) {
    }

    public Habilidad[] habilidadesDisponibles() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    protected void aumentarEstadisticas(int vida, int mana, int ataque, int defensa, int velocidad) {
    }
}
