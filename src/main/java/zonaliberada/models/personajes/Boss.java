package zonaliberada.models.personajes;

import java.util.List;
import zonaliberada.models.ReglasJuego;
import zonaliberada.models.combate.*;
import zonaliberada.models.combate.acciones.*;

/** Enemigo con fases y un patrón de acciones; el jefe final también puede invocar. */
public class Boss extends Enemigo {
    private final boolean esFinal;
    private int faseActual;
    // Permite alternar habilidades según las acciones que realmente se ejecutaron.
    private int accionesEjecutadas;
    private boolean invocacionRealizada;

    public Boss(
            String nombre,
            int vidaMaxima,
            int manaMaximo,
            int ataqueBase,
            int defensaBase,
            int velocidadBase,
            int iniciativa,
            List<Habilidad> habilidades,
            boolean ataqueDistancia,
            boolean esFinal) {

        super(nombre, vidaMaxima, manaMaximo,
                ataqueBase, defensaBase, velocidadBase, iniciativa,
                habilidades, ataqueDistancia);
        this.esFinal = esFinal;
        // Un jefe nuevo siempre comienza en la primera fase.
        this.faseActual = 1;
    }

    public boolean esFinal() { return esFinal; }
    public int getFaseActual() { return faseActual; }
    public boolean haInvocado() { return invocacionRealizada; }

    @Override
    public void prepararParaBatalla() {
        super.prepararParaBatalla();
        faseActual = 1;
        accionesEjecutadas = 0;
        invocacionRealizada = false;
    }

    @Override
    public double ataqueTotal() {
        return super.ataqueTotal() * (faseActual == 2 ? 1.2 : 1.0);
    }

    @Override
    public void recibirDanio(int danio) {
        super.recibirDanio(danio);
        if (estaVivo() && getVida() <= getVidaMaxima() / 2) faseActual = 2;
    }

    @Override
    public Accion elegirAccion(Batalla batalla) {
        limpiarDecision();
        if (!puedeDecidir(batalla)) return null;

        // Invoca una sola vez y consume una acción propia de 2 PA.
        if (esFinal && faseActual == 2 && !invocacionRealizada && tienePA(2)) {
            for (int posicion = 1; posicion <= ReglasJuego.CANTIDAD_POSICIONES; posicion++) {
                if (batalla.enemigoEn(posicion) == null) {
                    Enemigo refuerzo = new Enemigo("Refuerzo de la barra", 75,
                            20, 16, 5, 18, 0, List.of(), false,
                            TipoEnemigo.BARRA_BRAVA);
                    return new InvocarEnemigo(batalla, refuerzo, posicion);
                }
            }
        }

        Habilidad[] habilidades = habilidadesDisponibles();
        for (int paso = 0; paso < habilidades.length; paso++) {
            Habilidad habilidad = habilidades[(accionesEjecutadas % habilidades.length + paso) % habilidades.length];
            if (habilidad.afectaAliados() && tieneEstado(TipoEstado.ESCUDO)) continue;
            if (seleccionarHabilidad(habilidad)) return new UsarHabilidad();
        }
        return new Atacar();
    }

    @Override
    public Entidad[] elegirObjetivos(Accion accion, Batalla batalla) {
        if (puedeDecidir(batalla) && accion instanceof UsarHabilidad
                && getHabilidadElegida() != null
                && getHabilidadElegida().afectaAliados()
                && !getHabilidadElegida().esGrupal()) {
            return new Entidad[]{this};
        }
        return super.elegirObjetivos(accion, batalla);
    }

    @Override
    public void alEjecutarAccion() {
        accionesEjecutadas = accionesEjecutadas == Integer.MAX_VALUE ? 0 : accionesEjecutadas + 1;
    }
    public void registrarInvocacion() { invocacionRealizada = true; }
}
