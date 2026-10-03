package zonaliberada.ui;

import java.util.List;

/**
 * Datos temporales para visualizar las pantallas antes de crear el modelo.
 * Ficha NO reemplaza a Entidad ni a Aliado: solo reúne información para mostrar.
 */
public final class DatosDemo {
    // List.of crea una lista que no permite agregar o quitar elementos.
    // Los números son ejemplos, no representan el balance definitivo del juego.
    public static final int PUNTOS_ACCION_MAXIMOS = 2;
    public static final int CANTIDAD_MATES = 5;
    public static final int CANTIDAD_FERNETS = 3;
    public static final List<String> EQUIPABLES_DISPONIBLES = List.of("Poncho", "Amuleto");
    public static final List<Ficha> ALIADOS = List.of(
            new Ficha("Gaucho", "Guerrero", 2, 80, 80, 20, 20, 3, 10, 100, 16, 14, 7, true,
                    "Facón", "Boleadoras", "Coraje criollo",
                    "/imagenes/personajes/aliados/gaucho.png"),
            new Ficha("Médico", "Curador", 2, 70, 70, 40, 40, 5, 10, 92, 7, 9, 10, true,
                    "Maletín", "Primeros auxilios", "Alta médica",
                    "/imagenes/personajes/aliados/medico.png"),
            new Ficha("Twittero", "Arquero", 1, 65, 65, 30, 30, 2, 5, 35, 11, 7, 15, true,
                    "Celular", "Hilo viral", "Cancelado",
                    "/imagenes/personajes/aliados/twittero.png"),
            new Ficha("Electricista", "Mago", 1, 60, 60, 45, 45, 1, 5, 60, 14, 8, 9, true,
                    "Pinza", "Sobrecarga", "Corte de luz",
                    "/imagenes/personajes/aliados/electricista.png"),
            new Ficha("Parrillero", "Curador", 1, 75, 75, 30, 30, 0, 5, 0, 8, 10, 8, false,
                    "Espátula", "Choripán", "Asado completo",
                    "/imagenes/personajes/aliados/parrillero.png"),
            new Ficha("Colectivero", "Guerrero", 1, 90, 90, 15, 15, 0, 5, 0, 12, 16, 5, false,
                    "Bocina", "Frenada brusca", "Línea 60",
                    "/imagenes/personajes/aliados/colectivero.png"),
            new Ficha("Jubilado", "Mago", 1, 55, 55, 50, 50, 0, 5, 0, 10, 8, 6, false,
                    "Bastón", "Experiencia de vida", "Fila eterna",
                    "/imagenes/personajes/aliados/jubilado.png"));

    public static final List<Ficha> ENEMIGOS = List.of(
            new Ficha("Barra brava", "Enemigo", 1, 80, 80, 15, 15, 0, 1, 48, 12, 14, 7, true,
                    "/imagenes/personajes/enemigos/barra_brava.png"),
            new Ficha("Motochorro", "Enemigo", 1, 60, 60, 20, 20, 0, 1, 30, 10, 5, 14, true,
                    "/imagenes/personajes/enemigos/motochorro.png"),
            new Ficha("Trapito", "Enemigo", 1, 55, 55, 20, 20, 0, 1, 40, 8, 6, 11, true,
                    "/imagenes/personajes/enemigos/trapito.png"),
            new Ficha("Fisura", "Enemigo", 1, 50, 50, 10, 10, 0, 1, 22, 9, 5, 12, true,
                    "/imagenes/personajes/enemigos/fisura.png"));

    private DatosDemo() {
    }

    public static List<Ficha> partyActiva() {
        // subList incluye el índice inicial (0) y excluye el final (4).
        // Elegimos siempre los primeros cuatro: la selección real está pendiente.
        return ALIADOS.subList(0, 4);
    }

    /** Una ficha inmutable: sus campos final no cambian después del constructor. */
    public static final class Ficha {
        public final String nombre;
        public final String rol;
        public final int nivel;
        public final int vida;
        public final int vidaMaxima;
        public final int mana;
        public final int manaMaximo;
        public final int experiencia;
        public final int experienciaSiguiente;
        public final int iniciativa;
        public final int ataque;
        public final int defensa;
        public final int velocidad;
        public final boolean desbloqueado;
        public final String arma;
        public final String habilidadPrincipal;
        public final String habilidadSecundaria;
        public final String imagen;

        // Este constructor corto se usa para enemigos, que no necesitan mostrar
        // equipamiento ni habilidades de aliado en el inventario.
        private Ficha(String nombre, String rol, int nivel, int vida, int vidaMaxima,
                int mana, int manaMaximo, int experiencia, int experienciaSiguiente,
                int iniciativa, int ataque, int defensa, int velocidad,
                boolean desbloqueado, String imagen) {
            this(nombre, rol, nivel, vida, vidaMaxima, mana, manaMaximo, experiencia,
                    experienciaSiguiente, iniciativa, ataque, defensa, velocidad,
                    desbloqueado, "", "", "", imagen);
        }

        private Ficha(String nombre, String rol, int nivel, int vida, int vidaMaxima,
                int mana, int manaMaximo, int experiencia, int experienciaSiguiente,
                int iniciativa, int ataque, int defensa, int velocidad,
                boolean desbloqueado, String arma,
                String habilidadPrincipal, String habilidadSecundaria, String imagen) {
            this.nombre = nombre;
            this.rol = rol;
            this.nivel = nivel;
            this.vida = vida;
            this.vidaMaxima = vidaMaxima;
            this.mana = mana;
            this.manaMaximo = manaMaximo;
            this.experiencia = experiencia;
            this.experienciaSiguiente = experienciaSiguiente;
            this.iniciativa = iniciativa;
            this.ataque = ataque;
            this.defensa = defensa;
            this.velocidad = velocidad;
            this.desbloqueado = desbloqueado;
            this.arma = arma;
            this.habilidadPrincipal = habilidadPrincipal;
            this.habilidadSecundaria = habilidadSecundaria;
            this.imagen = imagen;
        }
    }
}
