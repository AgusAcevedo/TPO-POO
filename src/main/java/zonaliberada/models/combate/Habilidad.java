package zonaliberada.models.combate;

import java.util.List;
import zonaliberada.models.combate.efectos.EfectoCombate;
import zonaliberada.models.personajes.Entidad;

public class Habilidad {
    private String nombre;
    private int costoPA;
    private int costoMana;
    private boolean afectaAliados;
    private boolean esGrupal;
    private int nivelDesbloqueo;
    private boolean aDistancia;
    private List<EfectoCombate> efectos;

    public Habilidad() {
    }

    public Habilidad(
            String nombre,
            int costoPA,
            int costoMana,
            boolean afectaAliados,
            boolean esGrupal,
            int nivelDesbloqueo,
            boolean aDistancia,
            List<EfectoCombate> efectos) {
    }

    public boolean puedeUsarse(Entidad usuario) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void aplicarEfectos(Entidad usuario, Entidad[] objetivos) {
    }
}
