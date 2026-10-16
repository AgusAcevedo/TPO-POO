package zonaliberada.models.combate;

import zonaliberada.models.personajes.Entidad;

/** Estado temporal: duración en comienzos de turno del portador. */
public class EstadoAlterado {
    private final TipoEstado tipo;
    private int turnosRestantes;
    // Representa daño fijo para VENENO y porcentaje para los modificadores y el escudo.
    private final int magnitud;

    public EstadoAlterado(TipoEstado tipo, int turnosRestantes, int magnitud) {
        if (tipo == null || turnosRestantes <= 0 || magnitud < 0) {
            throw new IllegalArgumentException("Tipo, duración y magnitud inválidos");
        }
        if ((tipo == TipoEstado.ESCUDO || tipo == TipoEstado.RALENTIZADO
                || tipo == TipoEstado.REDUCCION_ATAQUE
                || tipo == TipoEstado.REDUCCION_DEFENSA) && magnitud > 100) {
            throw new IllegalArgumentException("Una reducción porcentual no puede superar el 100 %");
        }
        this.tipo = tipo;
        this.turnosRestantes = turnosRestantes;
        this.magnitud = magnitud;
    }

    // Conserva el contador actual, incluso si ya venció; cada copia tiene duración propia.
    private EstadoAlterado(EstadoAlterado original) {
        tipo = original.tipo;
        turnosRestantes = original.turnosRestantes;
        magnitud = original.magnitud;
    }

    public TipoEstado getTipo() { return tipo; }
    public int getTurnosRestantes() { return turnosRestantes; }
    public int getMagnitud() { return magnitud; }

    public boolean finalizo() { return turnosRestantes == 0; }
    public EstadoAlterado copia() { return new EstadoAlterado(this); }

    public void alInicioTurno(Entidad entidad) {
        if (entidad == null) throw new IllegalArgumentException("La entidad es obligatoria");
        if (finalizo()) return;
        // Veneno: daño fijo antes de consumir este comienzo de turno.
        if (tipo == TipoEstado.VENENO && entidad.estaVivo()) {
            entidad.recibirDanio(magnitud);
        }
        turnosRestantes--;
    }

    public int modificarDefensa(int valor) {
        validarValor(valor);
        if (finalizo()) return valor;
        // El switch devuelve el valor modificado solo para los estados de defensa.
        return switch (tipo) {
            case AUMENTO_DEFENSA -> modificarPorcentaje(valor, magnitud);
            case REDUCCION_DEFENSA -> modificarPorcentaje(valor, -magnitud);
            default -> valor;
        };
    }

    public int modificarAtaque(int valor) {
        validarValor(valor);
        if (finalizo()) return valor;
        return switch (tipo) {
            case AUMENTO_ATAQUE -> modificarPorcentaje(valor, magnitud);
            case REDUCCION_ATAQUE -> modificarPorcentaje(valor, -magnitud);
            default -> valor;
        };
    }

    public int modificarVelocidad(int valor) {
        validarValor(valor);
        return !finalizo() && tipo == TipoEstado.RALENTIZADO
                ? Math.max(1, modificarPorcentaje(valor, -magnitud)) : valor;
    }

    public int reducirDanio(int danio) {
        validarValor(danio);
        return !finalizo() && tipo == TipoEstado.ESCUDO
                ? modificarPorcentaje(danio, -magnitud) : danio;
    }

    private static int modificarPorcentaje(int valor, int porcentaje) {
        return (int) Math.max(0, Math.min(Integer.MAX_VALUE,
                Math.round(valor * (1.0 + porcentaje / 100.0))));
    }

    private static void validarValor(int valor) {
        if (valor < 0) throw new IllegalArgumentException("El valor no puede ser negativo");
    }
}
