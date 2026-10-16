package zonaliberada.models.combate;

import java.util.List;

import zonaliberada.models.combate.efectos.EfectoCombate;
import zonaliberada.models.personajes.Entidad;

/** Define costo, alcance y efectos; ControladorTurno se encarga del pago de recursos. */
public class Habilidad {

    private final String nombre;
    private final int costoPA;
    private final int costoMana;
    private final boolean afectaAliados;
    private final boolean esGrupal;
    private final int nivelDesbloqueo;
    private final boolean aDistancia;
    private final List<EfectoCombate> efectos;

    public Habilidad(
            String nombre,
            int costoPA,
            int costoMana,
            boolean afectaAliados,
            boolean esGrupal,
            int nivelDesbloqueo,
            boolean aDistancia,
            List<EfectoCombate> efectos) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la habilidad es obligatorio");
        }

        if (costoPA <= 0 || costoMana < 0) {
            throw new IllegalArgumentException(
                    "El costo de PA debe ser positivo y el de maná no negativo");
        }

        if (nivelDesbloqueo < 1) {
            throw new IllegalArgumentException(
                    "El nivel de desbloqueo debe ser al menos 1");
        }

        if (efectos == null) {
            throw new IllegalArgumentException(
                    "La lista de efectos es obligatoria");
        }

        if (efectos.isEmpty()) {
            throw new IllegalArgumentException(
                    "La habilidad necesita al menos un efecto y no admite null");
        }
        for (EfectoCombate efecto : efectos) {
            if (efecto == null) throw new IllegalArgumentException("Los efectos no pueden contener null");
        }

        this.nombre = nombre.strip();
        this.costoPA = costoPA;
        this.costoMana = costoMana;
        this.afectaAliados = afectaAliados;
        this.esGrupal = esGrupal;
        this.nivelDesbloqueo = nivelDesbloqueo;
        this.aDistancia = aDistancia;

        // Copiamos la lista y no permitimos modificar su configuración.
        this.efectos = List.copyOf(efectos);
    }

    public String getNombre() {
        return nombre;
    }

    public int getCostoPA() {
        return costoPA;
    }

    public int getCostoMana() {
        return costoMana;
    }

    public boolean afectaAliados() {
        return afectaAliados;
    }

    public boolean esGrupal() {
        return esGrupal;
    }

    public int getNivelDesbloqueo() {
        return nivelDesbloqueo;
    }

    public boolean permiteDistancia() {
        return aDistancia;
    }

    public String getDescripcion() {
        return EfectoCombate.describir(efectos);
    }

    public boolean puedeUsarse(Entidad usuario) {
        if (usuario == null || !usuario.estaVivo()) {
            return false;
        }

        // Las disponibles ya consideran el nivel si es un aliado.
        for (Habilidad habilidad : usuario.habilidadesDisponibles()) {
            if (habilidad == this) {
                return true;
            }
        }

        // No puede utilizar una habilidad que no tiene disponible.
        return false;
    }

    public String aplicarEfectos(Entidad usuario, Entidad[] objetivos) {
        if (!puedeUsarse(usuario)) {
            throw new IllegalArgumentException(
                    "La habilidad no está disponible para este usuario");
        }

        if (objetivos == null || objetivos.length == 0
                || (!esGrupal && objetivos.length != 1)) {
            throw new IllegalArgumentException(
                    "La cantidad de objetivos no es válida");
        }

        Entidad[] objetivosPropios = objetivos.clone();

        // Validamos todos los objetivos antes de aplicar el primer efecto.
        for (int i = 0; i < objetivosPropios.length; i++) {
            Entidad objetivo = objetivosPropios[i];

            if (objetivo == null || !objetivo.estaVivo()) {
                throw new IllegalArgumentException(
                        "Los objetivos deben ser entidades vivas");
            }

            for (int j = 0; j < i; j++) {
                if (objetivo == objetivosPropios[j]) {
                    throw new IllegalArgumentException(
                            "Los objetivos no pueden estar repetidos");
                }
            }
        }

        // Cada objetivo recibe los efectos en el orden configurado.
        String mensaje = "";
        for (Entidad objetivo : objetivosPropios) {
            mensaje += EfectoCombate.aplicarTodos(efectos, usuario, objetivo) + " ";
        }

        // No comprobamos ni descontamos PA o maná aquí.
        // ControladorTurno gestiona esos recursos.
        // También comprueba el bando, el alcance y los objetivos de grupo.
        return mensaje.strip();
    }
}
