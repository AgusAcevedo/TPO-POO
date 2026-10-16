package zonaliberada.models.combate.efectos;

import zonaliberada.models.personajes.Entidad;

public class EfectoDanio extends EfectoCombate {

    // Reglas compartidas por ataques básicos y habilidades que causan daño.
    private static final double MULTIPLICADOR_CRITICO = 1.5;
    private static final int DANIO_MINIMO = 1;

    // Porcentaje del ataque total:
    // 100 = ataque normal; 150 = 150 % del ataque.
    private final int potencia;

    public EfectoDanio(int potencia) {
        if (potencia <= 0) {
            throw new IllegalArgumentException(
                    "La potencia del daño debe ser positiva");
        }

        this.potencia = potencia;
    }

    @Override
    public String descripcion() {
        return "Daño: " + potencia + " % del ataque; se aplica la defensa del objetivo";
    }

    @Override
    public String aplicar(Entidad usuario, Entidad objetivo) {
        if (usuario == null || objetivo == null) {
            throw new IllegalArgumentException(
                    "El usuario y el objetivo son obligatorios");
        }

        // No volvemos a dañar a un personaje derrotado.
        if (!objetivo.estaVivo()) {
            return "";
        }

        // ataqueTotal ya incorpora las bonificaciones del personaje:
        // furia, concentración o precisión, según corresponda.
        double ataque = usuario.ataqueTotal() * (potencia / 100.0);

        // Esta es la única implementación del sorteo del crítico.
        // Cada entidad informa su probabilidad mediante polimorfismo.
        boolean critico = Math.random() < usuario.getProbabilidadCritico();
        if (critico) {
            ataque *= MULTIPLICADOR_CRITICO;
        }

        // Primero aplicamos potencia y crítico; después, la defensa.
        // defensaTotal también considera los estados temporales.
        double danioCalculado = ataque - objetivo.defensaTotal();
        long danioRedondeado = Math.round(danioCalculado);

        // Mínimo 1 de daño, sin superar lo que puede representar un int.
        int danioFinal = (int) Math.max(
                DANIO_MINIMO,
                Math.min(Integer.MAX_VALUE, danioRedondeado));

        // El receptor administra su vida y sus propias mecánicas.
        // Por ejemplo: ganar furia o perder concentración.
        int vidaAnterior = objetivo.getVida();
        objetivo.recibirDanio(danioFinal);

        // No descontamos PA ni maná y no gestionamos el turno.
        // Esas responsabilidades siguen en ControladorTurno.
        return objetivo.getNombre() + " recibió " + (vidaAnterior - objetivo.getVida())
                + " de daño" + (critico ? " (crítico)" : "")
                + (objetivo.estaVivo() ? "." : " y quedó fuera de combate.");
    }
}
