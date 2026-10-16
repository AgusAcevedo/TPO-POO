package zonaliberada.models.personajes;

import java.util.List;
import zonaliberada.models.combate.*;
import zonaliberada.models.combate.acciones.*;

/** Decide qué hacer; ControladorTurno crea el contexto y paga la acción. */
public class Enemigo extends Entidad {
    private final TipoEnemigo tipo;
    private Habilidad habilidadElegida;

    /** Para Boss, que define su comportamiento sin un TipoEnemigo normal. */
    protected Enemigo(
            String nombre,
            int vidaMaxima,
            int manaMaximo,
            int ataqueBase,
            int defensaBase,
            int velocidadBase,
            int iniciativa,
            List<Habilidad> habilidades,
            boolean ataqueDistancia) {

        super(nombre, vidaMaxima, manaMaximo,
                ataqueBase, defensaBase, velocidadBase, iniciativa,
                habilidades, ataqueDistancia);
        this.tipo = null;
    }

    /** Los enemigos normales se crean indicando su tipo. */
    public Enemigo(
            String nombre,
            int vidaMaxima,
            int manaMaximo,
            int ataqueBase,
            int defensaBase,
            int velocidadBase,
            int iniciativa,
            List<Habilidad> habilidades,
            boolean ataqueDistancia,
            TipoEnemigo tipo) {

        super(nombre, vidaMaxima, manaMaximo,
                ataqueBase, defensaBase, velocidadBase, iniciativa,
                habilidades, ataqueDistancia);
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo del enemigo es obligatorio");
        }
        this.tipo = tipo;
    }

    public TipoEnemigo getTipo() { return tipo; }
    public Habilidad getHabilidadElegida() { return habilidadElegida; }

    public Accion elegirAccion(Batalla batalla) {
        limpiarDecision();
        if (!puedeDecidir(batalla)) return null;
        // Los motochorros usan ataques básicos; Boss decide en su propia clase.
        if (tipo == null || tipo == TipoEnemigo.MOTOCHORRO) return new Atacar();

        for (Habilidad habilidad : habilidadesDisponibles()) {
            Entidad[] candidatos = batalla.objetivosValidos(this,
                    habilidad.afectaAliados(), habilidad.permiteDistancia());
            boolean conviene = false;
            switch (tipo) {
                case BARRA_BRAVA:
                    conviene = !tieneEstado(TipoEstado.AUMENTO_ATAQUE);
                    break;
                case PIQUETERO:
                case TRAPITO:
                    conviene = true;
                    break;
                case FISURA:
                    conviene = Math.random() < 0.5;
                    break;
                case TRANSA:
                    conviene = hayCandidatoSinEstado(candidatos, TipoEstado.VENENO);
                    break;
                case PATOVICA:
                    conviene = hayCandidatoSinEstado(candidatos, TipoEstado.ESCUDO);
                    break;
                default:
                    break;
            }
            if (conviene && candidatos.length > 0 && seleccionarHabilidad(habilidad)) {
                return new UsarHabilidad();
            }
        }
        return new Atacar();
    }

    public Entidad[] elegirObjetivos(Accion accion, Batalla batalla) {
        if (accion == null || !puedeDecidir(batalla)) return new Entidad[0];
        if (accion instanceof Defender || accion instanceof InvocarEnemigo) return new Entidad[0];

        Habilidad habilidad = accion instanceof UsarHabilidad ? habilidadElegida : null;
        Entidad[] candidatos = batalla.objetivosValidos(this,
                habilidad != null && habilidad.afectaAliados(),
                habilidad != null ? habilidad.permiteDistancia() : isAtaqueDistancia());
        if (candidatos.length == 0 || (habilidad != null && habilidad.esGrupal())) return candidatos;
        if (tipo == TipoEnemigo.BARRA_BRAVA && habilidad != null && habilidad.afectaAliados()) {
            return new Entidad[]{this};
        }

        Entidad objetivo = candidatos[0];
        if (tipo == TipoEnemigo.MOTOCHORRO) {
            for (Entidad candidato : candidatos) {
                if (candidato.getVida() < objetivo.getVida()) objetivo = candidato;
            }
        } else if (tipo == TipoEnemigo.TRANSA && habilidad != null) {
            for (Entidad candidato : candidatos) {
                if (!candidato.tieneEstado(TipoEstado.VENENO)) {
                    objetivo = candidato;
                    break;
                }
            }
        } else if (tipo == TipoEnemigo.PATOVICA && habilidad != null) {
            Entidad sinEscudo = null;
            for (Entidad candidato : candidatos) {
                if (!candidato.tieneEstado(TipoEstado.ESCUDO)
                        && (sinEscudo == null || candidato.getVida() < sinEscudo.getVida())) {
                    sinEscudo = candidato;
                }
            }
            if (sinEscudo != null) objetivo = sinEscudo;
        } else if (tipo == TipoEnemigo.TRAPITO && habilidad != null) {
            for (Entidad candidato : candidatos) {
                if (candidato.getMana() > objetivo.getMana()) objetivo = candidato;
            }
        } else {
            objetivo = candidatos[(int) (Math.random() * candidatos.length)];
        }
        return new Entidad[]{objetivo};
    }

    public void alEjecutarAccion() {
        // Extensión polimórfica para la memoria de los bosses.
    }

    protected boolean puedeDecidir(Batalla batalla) {
        return batalla != null && batalla.verificarResultado() == EstadoBatalla.EN_CURSO
                && batalla.obtenerActorActual() == this && estaVivo() && getPuntosAccion() > 0;
    }

    protected void limpiarDecision() { habilidadElegida = null; }

    protected boolean seleccionarHabilidad(Habilidad habilidad) {
        // Guarda la decisión; el controlador validará y pagará la acción después.
        if (habilidad == null || !habilidad.puedeUsarse(this)
                || !tienePA(habilidad.getCostoPA()) || !tieneMana(habilidad.getCostoMana())) {
            return false;
        }
        habilidadElegida = habilidad;
        return true;
    }

    private boolean hayCandidatoSinEstado(Entidad[] candidatos, TipoEstado estado) {
        for (Entidad candidato : candidatos) {
            if (!candidato.tieneEstado(estado)) return true;
        }
        return false;
    }
}
