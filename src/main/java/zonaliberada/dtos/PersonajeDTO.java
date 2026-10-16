package zonaliberada.dtos;
import java.util.List;
/** Instantánea inmutable: no expone Entidad ni colecciones del modelo. */
public final class PersonajeDTO {
    private final String id;
    private final String nombre;
    private final String rol;
    private final int posicion;
    private final int vida;
    private final int vidaMaxima;
    private final int mana;
    private final int manaMaximo;
    private final int nivel;
    private final int experiencia;
    private final int experienciaSiguiente;
    private final int iniciativa;
    private final int puntosAccion;
    private final int puntosAccionMaximos;
    private final double ataque;
    private final double defensa;
    private final int velocidad;
    private final boolean aDistancia;
    private final boolean vivo;
    private final String atributoEspecial;
    private final int valorEspecial;
    private final int faseBoss;
    private final List<HabilidadDTO> habilidades;
    private final List<ItemDTO> equipados;
    private final List<EstadoDTO> estados;

    public PersonajeDTO(
            String id,
            String nombre,
            String rol,
            int posicion,
            int vida,
            int vidaMaxima,
            int mana,
            int manaMaximo,
            int nivel,
            int experiencia,
            int experienciaSiguiente,
            int iniciativa,
            int puntosAccion,
            int puntosAccionMaximos,
            double ataque,
            double defensa,
            int velocidad,
            boolean aDistancia,
            boolean vivo,
            String atributoEspecial,
            int valorEspecial,
            int faseBoss,
            List<HabilidadDTO> habilidades,
            List<ItemDTO> equipados,
            List<EstadoDTO> estados) {
        habilidades = List.copyOf(habilidades);
        equipados = List.copyOf(equipados);
        estados = List.copyOf(estados);
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.posicion = posicion;
        this.vida = vida;
        this.vidaMaxima = vidaMaxima;
        this.mana = mana;
        this.manaMaximo = manaMaximo;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.experienciaSiguiente = experienciaSiguiente;
        this.iniciativa = iniciativa;
        this.puntosAccion = puntosAccion;
        this.puntosAccionMaximos = puntosAccionMaximos;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.aDistancia = aDistancia;
        this.vivo = vivo;
        this.atributoEspecial = atributoEspecial;
        this.valorEspecial = valorEspecial;
        this.faseBoss = faseBoss;
        this.habilidades = habilidades;
        this.equipados = equipados;
        this.estados = estados;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public int getPosicion() { return posicion; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getMana() { return mana; }
    public int getManaMaximo() { return manaMaximo; }
    public int getNivel() { return nivel; }
    public int getExperiencia() { return experiencia; }
    public int getExperienciaSiguiente() { return experienciaSiguiente; }
    public int getIniciativa() { return iniciativa; }
    public int getPuntosAccion() { return puntosAccion; }
    public int getPuntosAccionMaximos() { return puntosAccionMaximos; }
    public double getAtaque() { return ataque; }
    public double getDefensa() { return defensa; }
    public int getVelocidad() { return velocidad; }
    public boolean isADistancia() { return aDistancia; }
    public boolean isVivo() { return vivo; }
    public String getAtributoEspecial() { return atributoEspecial; }
    public int getValorEspecial() { return valorEspecial; }
    public int getFaseBoss() { return faseBoss; }
    public List<HabilidadDTO> getHabilidades() { return habilidades; }
    public List<ItemDTO> getEquipados() { return equipados; }
    public List<EstadoDTO> getEstados() { return estados; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof PersonajeDTO)) return false;
        PersonajeDTO otro = (PersonajeDTO) objeto;
        return java.util.Objects.equals(id, otro.id)
                && java.util.Objects.equals(nombre, otro.nombre)
                && java.util.Objects.equals(rol, otro.rol)
                && posicion == otro.posicion
                && vida == otro.vida
                && vidaMaxima == otro.vidaMaxima
                && mana == otro.mana
                && manaMaximo == otro.manaMaximo
                && nivel == otro.nivel
                && experiencia == otro.experiencia
                && experienciaSiguiente == otro.experienciaSiguiente
                && iniciativa == otro.iniciativa
                && puntosAccion == otro.puntosAccion
                && puntosAccionMaximos == otro.puntosAccionMaximos
                && Double.compare(ataque, otro.ataque) == 0
                && Double.compare(defensa, otro.defensa) == 0
                && velocidad == otro.velocidad
                && aDistancia == otro.aDistancia
                && vivo == otro.vivo
                && java.util.Objects.equals(atributoEspecial, otro.atributoEspecial)
                && valorEspecial == otro.valorEspecial
                && faseBoss == otro.faseBoss
                && java.util.Objects.equals(habilidades, otro.habilidades)
                && java.util.Objects.equals(equipados, otro.equipados)
                && java.util.Objects.equals(estados, otro.estados);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                id, nombre, rol, posicion, vida, vidaMaxima, mana, manaMaximo, nivel, experiencia,
                experienciaSiguiente, iniciativa, puntosAccion,
                puntosAccionMaximos, ataque, defensa, velocidad, aDistancia,
                vivo, atributoEspecial, valorEspecial, faseBoss, habilidades, equipados,
                estados);
    }

    @Override
    public String toString() {
        return "PersonajeDTO["
                + "id=" + id
                + ", nombre=" + nombre
                + ", rol=" + rol
                + ", posicion=" + posicion
                + ", vida=" + vida
                + ", vidaMaxima=" + vidaMaxima
                + ", mana=" + mana
                + ", manaMaximo=" + manaMaximo
                + ", nivel=" + nivel
                + ", experiencia=" + experiencia
                + ", experienciaSiguiente=" + experienciaSiguiente
                + ", iniciativa=" + iniciativa
                + ", puntosAccion=" + puntosAccion
                + ", puntosAccionMaximos=" + puntosAccionMaximos
                + ", ataque=" + ataque
                + ", defensa=" + defensa
                + ", velocidad=" + velocidad
                + ", aDistancia=" + aDistancia
                + ", vivo=" + vivo
                + ", atributoEspecial=" + atributoEspecial
                + ", valorEspecial=" + valorEspecial
                + ", faseBoss=" + faseBoss
                + ", habilidades=" + habilidades
                + ", equipados=" + equipados
                + ", estados=" + estados + "]";
    }

    public static PersonajeDTO buscarPorId(List<PersonajeDTO> personajes, String id) {
        for (PersonajeDTO personaje : personajes) {
            if (personaje.getId().equals(id)) return personaje;
        }
        return null;
    }

    public static PersonajeDTO buscarEnPosicion(List<PersonajeDTO> personajes, int posicion) {
        for (PersonajeDTO personaje : personajes) {
            if (personaje.getPosicion() == posicion) return personaje;
        }
        return null;
    }
}
