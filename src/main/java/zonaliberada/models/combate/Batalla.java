package zonaliberada.models.combate;

import zonaliberada.models.Party;
import zonaliberada.models.mapa.UbicacionMapa;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.models.personajes.Enemigo;
import zonaliberada.models.personajes.Entidad;

public class Batalla {
    private Party party;
    private FormacionCombate formacionEnemiga;
    private UbicacionMapa ubicacion;
    private Entidad actorActual;
    private EstadoBatalla estado;
    private Recompensa recompensa;
    private boolean recompensaEntregada;

    public Batalla() {
    }

    public Batalla(
            Party party,
            FormacionCombate formacionEnemiga,
            UbicacionMapa ubicacion,
            Entidad actorActual,
            EstadoBatalla estado,
            Recompensa recompensa,
            boolean recompensaEntregada) {
    }

    public boolean iniciar() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Aliado[] aliados() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Enemigo[] enemigos() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad[] participantes() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad avanzarHastaSiguienteActor() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad obtenerActorActual() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Entidad[] objetivosValidos(Entidad origen, boolean afectaAliados, boolean esGrupal, boolean aDistancia) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void finalizarTurno() {
    }

    public EstadoBatalla verificarResultado() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public Recompensa entregarRecompensa() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    private Recompensa generarRecompensa() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
