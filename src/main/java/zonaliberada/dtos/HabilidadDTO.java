package zonaliberada.dtos;
/** La UI muestra desbloqueada/pagable sin ejecutar ninguna regla de combate. */
// Desbloqueada depende del nivel; pagable también considera vida, PA y maná actuales.
public final class HabilidadDTO {
    private final String nombre;
    private final int costoPA;
    private final int costoMana;
    private final boolean afectaAliados;
    private final boolean grupal;
    private final int nivelDesbloqueo;
    private final boolean desbloqueada;
    private final boolean pagable;
    private final String descripcion;

    public HabilidadDTO(
            String nombre,
            int costoPA,
            int costoMana,
            boolean afectaAliados,
            boolean grupal,
            int nivelDesbloqueo,
            boolean desbloqueada,
            boolean pagable,
            String descripcion) {
        this.nombre = nombre;
        this.costoPA = costoPA;
        this.costoMana = costoMana;
        this.afectaAliados = afectaAliados;
        this.grupal = grupal;
        this.nivelDesbloqueo = nivelDesbloqueo;
        this.desbloqueada = desbloqueada;
        this.pagable = pagable;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public int getCostoPA() { return costoPA; }
    public int getCostoMana() { return costoMana; }
    public boolean isAfectaAliados() { return afectaAliados; }
    public boolean isGrupal() { return grupal; }
    public int getNivelDesbloqueo() { return nivelDesbloqueo; }
    public boolean isDesbloqueada() { return desbloqueada; }
    public boolean isPagable() { return pagable; }
    public String getDescripcion() { return descripcion; }

    // Dos instantáneas con los mismos datos son iguales, aunque sean objetos distintos.
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof HabilidadDTO)) return false;
        HabilidadDTO otro = (HabilidadDTO) objeto;
        return java.util.Objects.equals(nombre, otro.nombre)
                && costoPA == otro.costoPA
                && costoMana == otro.costoMana
                && afectaAliados == otro.afectaAliados
                && grupal == otro.grupal
                && nivelDesbloqueo == otro.nivelDesbloqueo
                && desbloqueada == otro.desbloqueada
                && pagable == otro.pagable
                && java.util.Objects.equals(descripcion, otro.descripcion);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(
                nombre, costoPA, costoMana, afectaAliados, grupal, nivelDesbloqueo, desbloqueada,
                pagable, descripcion);
    }

    @Override
    public String toString() {
        return "HabilidadDTO["
                + "nombre=" + nombre
                + ", costoPA=" + costoPA
                + ", costoMana=" + costoMana
                + ", afectaAliados=" + afectaAliados
                + ", grupal=" + grupal
                + ", nivelDesbloqueo=" + nivelDesbloqueo
                + ", desbloqueada=" + desbloqueada
                + ", pagable=" + pagable
                + ", descripcion=" + descripcion + "]";
    }

}
