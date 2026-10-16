package zonaliberada.catalogo;

import java.util.List;
import zonaliberada.models.combate.Habilidad;

/** Estadísticas de partida de un enemigo normal, sin vida ni estados actuales. */
public final class EnemigoDef {
    private final String nombre;
    private final int vidaMaxima;
    private final int ataque;
    private final int defensa;
    private final int velocidad;
    private final List<Habilidad> habilidades;

    public EnemigoDef(String nombre, int vidaMaxima, int ataque, int defensa,
            int velocidad, List<Habilidad> habilidades) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.habilidades = List.copyOf(habilidades);
    }

    public String getNombre() { return nombre; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVelocidad() { return velocidad; }
    public List<Habilidad> getHabilidades() { return habilidades; }
}
