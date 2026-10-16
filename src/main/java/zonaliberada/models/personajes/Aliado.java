package zonaliberada.models.personajes;

import java.util.List;
import java.util.ArrayList;

import zonaliberada.models.combate.Habilidad;
import zonaliberada.models.inventario.Equipable;
import zonaliberada.models.inventario.TipoEquipable;

/** Base de los personajes jugables, con experiencia, nivel y equipamiento. */
public abstract class Aliado extends Entidad {

    private final String idPersonaje;

    private int nivel;
    private int experiencia;
    private final List<Equipable> equipados = new ArrayList<>();

    /** Agrega identidad, progreso y equipo a los datos comunes de Entidad. */
    protected Aliado(
            String idPersonaje,
            String nombre,
            int vidaMaxima,
            int manaMaximo,
            int ataqueBase,
            int defensaBase,
            int velocidadBase,
            int iniciativa,
            List<Habilidad> habilidades,
            boolean ataqueDistancia,
            int nivel,
            int experiencia) {

        super(nombre, vidaMaxima, manaMaximo,
                ataqueBase, defensaBase, velocidadBase, iniciativa,
                habilidades, ataqueDistancia);

        if (idPersonaje == null || idPersonaje.isBlank()) {
            throw new IllegalArgumentException("El identificador del personaje es obligatorio");
        }

        if (nivel < 1 || nivel > ReglasProgresion.NIVEL_MAXIMO) {
            throw new IllegalArgumentException("El nivel debe estar entre 1 y "
                    + ReglasProgresion.NIVEL_MAXIMO);
        }

        if (experiencia < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa");
        }
        if ((nivel == ReglasProgresion.NIVEL_MAXIMO && experiencia != 0)
                || (nivel < ReglasProgresion.NIVEL_MAXIMO
                    && experiencia >= ReglasProgresion.experienciaParaSubir(nivel))) {
            throw new IllegalArgumentException("La experiencia debe corresponder al progreso del nivel actual");
        }

        this.idPersonaje = idPersonaje.strip();

        this.nivel = nivel;
        this.experiencia = experiencia;
        // El equipo se incorpora mediante reemplazarEquipable,
        // una vez que el personaje está construido.
    }

    public String getIdPersonaje() {
        return idPersonaje;
    }

    public abstract TipoClase tipoClase();

    public int getNivel() {
        return nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getExperienciaSiguienteNivel() {
        return ReglasProgresion.experienciaParaSubir(nivel);
    }

    public List<Equipable> getEquipados() { return List.copyOf(equipados); }

    @Override
    public int getVidaMaxima() {
        int total = super.getVidaMaxima();
        for (Equipable item : equipados) total = sumarSinDesbordar(total, item.getBonusVida());
        return total;
    }

    @Override
    public int getManaMaximo() {
        int total = super.getManaMaximo();
        for (Equipable item : equipados) total = sumarSinDesbordar(total, item.getBonusMana());
        return total;
    }

    @Override
    public void prepararParaBatalla() {
        super.prepararParaBatalla();

        // Los aliados empiezan cada encuentro con recursos completos.
        recuperarVidaCompleta();
        recuperarManaCompleto();
    }

    public void ganarExperiencia(int cantidad) {
        if (cantidad < 0) throw new IllegalArgumentException("La experiencia no puede ser negativa");
        if (estaEnBatalla()) {
            throw new IllegalStateException("La experiencia se entrega fuera de combate");
        }
        if (nivel == ReglasProgresion.NIVEL_MAXIMO) {
            experiencia = 0;
            return;
        }
        long restante = (long) experiencia + cantidad;
        // Una recompensa puede alcanzar para subir varios niveles consecutivos.
        while (nivel < ReglasProgresion.NIVEL_MAXIMO
                && restante >= ReglasProgresion.experienciaParaSubir(nivel)) {
            restante -= ReglasProgresion.experienciaParaSubir(nivel);
            nivel++;
            ReglasProgresion.aplicarMejora(this);
            alSubirNivel();
        }
        experiencia = nivel == ReglasProgresion.NIVEL_MAXIMO ? 0 : (int) restante;
    }

    // Las subclases pueden agregar una mejora propia sin repetir la subida de nivel.
    protected void alSubirNivel() {}

    public boolean reemplazarEquipable(Equipable item) {
        if (estaEnBatalla() || item == null || !item.esCompatible(this)
                || buscarEquipado(item.getId()) != null) {
            return false;
        }
        Equipable anterior = equipadoEn(item.getTipo());
        if (anterior != null) equipados.remove(anterior);
        equipados.add(item);
        // Ajustamos una sola vez, con los máximos del equipamiento final.
        ajustarRecursosAMaximos();
        return true;
    }

    public boolean desequipar(Equipable item) {
        if (estaEnBatalla() || item == null) return false;
        for (int i = 0; i < equipados.size(); i++) {
            if (equipados.get(i) == item) {
                equipados.remove(i);
                ajustarRecursosAMaximos();
                return true;
            }
        }
        return false;
    }

    public boolean tieneEquipado(Equipable item) {
        for (Equipable equipado : equipados) {
            if (equipado == item) return true;
        }
        return false;
    }

    public Equipable equipadoEn(TipoEquipable tipo) {
        for (Equipable equipado : equipados) {
            if (equipado.getTipo() == tipo) return equipado;
        }
        return null;
    }

    public Equipable buscarEquipado(String id) {
        for (Equipable equipado : equipados) {
            if (equipado.getId().equals(id)) return equipado;
        }
        return null;
    }

    @Override
    public Habilidad[] habilidadesDisponibles() {
        List<Habilidad> disponibles = new ArrayList<>();

        for (Habilidad habilidad : getHabilidades()) {
            if (nivel >= habilidad.getNivelDesbloqueo()) {
                disponibles.add(habilidad);
            }
        }

        return disponibles.toArray(new Habilidad[0]);
    }

    @Override
    protected int ataqueSinEstados() {
        // Se calcula desde el equipo actual: equipar no modifica permanentemente la base.
        int total = super.ataqueSinEstados();
        for (Equipable item : equipados) total = sumarSinDesbordar(total, item.getBonusAtaque());
        return total;
    }

    @Override
    protected int defensaSinEstados() {
        int total = super.defensaSinEstados();
        for (Equipable item : equipados) total = sumarSinDesbordar(total, item.getBonusDefensa());
        return total;
    }

    @Override
    protected int velocidadSinEstados() {
        int total = super.velocidadSinEstados();
        for (Equipable item : equipados) total = sumarSinDesbordar(total, item.getBonusVelocidad());
        return total;
    }

    protected int bonusPrecision() {
        int total = 0;
        for (Equipable item : equipados) total = sumarSinDesbordar(total, item.getBonusPrecision());
        return total;
    }
}
