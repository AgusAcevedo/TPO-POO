package zonaliberada.factories;

import java.util.List;
import java.util.ArrayList;
import zonaliberada.models.Partida;
import zonaliberada.models.Party;
import zonaliberada.models.combate.*;
import zonaliberada.models.combate.efectos.*;
import zonaliberada.models.inventario.*;
import zonaliberada.models.mapa.*;
import zonaliberada.models.personajes.*;

/**
 * Arma partidas, mapas y encuentros. Las fábricas consultan el catálogo compartido;
 * la vida, los estados, la XP y las cantidades pertenecen a cada partida.
 */
public class FabricaJuego {
    private final ItemFactory itemFactory = new ItemFactory();
    private final EnemigoFactory enemigoFactory = new EnemigoFactory();

    public Partida crearPartida() {
        // Todo el contenido inicial se crea aquí; los modelos no dependen de la UI.
        List<Aliado> iniciales = List.of(crearAliado("gaucho"), crearAliado("medico"),
                crearAliado("twittero"), crearAliado("electricista"));
        Party party = new Party(iniciales);
        int[] posiciones = {1, 4, 5, 6};
        for (int i = 0; i < iniciales.size(); i++) party.editarFormacion(posiciones[i], iniciales.get(i));

        Inventario inventario = new Inventario(List.of());
        inventario.agregarItem(crearItem("empanada"), 2);
        for (String tipo : List.of("mate", "fernet", "alfajor", "facon", "campera")) {
            inventario.agregarItem(crearItem(tipo));
        }

        Zona zonaA = new Zona("Zona A · Conurbano", List.of(
                ubicacion("a1", "La esquina", TipoUbicacion.BATALLA, 80,
                        List.of(TipoEnemigo.MOTOCHORRO, TipoEnemigo.FISURA), null,
                        List.of("empanada"), List.of()),
                ubicacion("a2", "Punto de encuentro", TipoUbicacion.MEJORA, 60,
                        List.of(), null, List.of("mate"), List.of()),
                ubicacion("a3", "El piquete", TipoUbicacion.BATALLA, 140,
                        List.of(TipoEnemigo.PIQUETERO, TipoEnemigo.TRAPITO), null,
                        List.of("choripan"), List.of()),
                ubicacion("a4", "La parrilla", TipoUbicacion.MEJORA, 100,
                        List.of(), null, List.of("espatula"), List.of("parrillero")),
                ubicacion("a5", "Gordo Mortero", TipoUbicacion.BOSS_ZONA, 250,
                        List.of(), "gordo-mortero", List.of("chaleco"), List.of("colectivero"))
        ), true);

        Zona zonaB = new Zona("Zona B · El barrio", List.of(
                ubicacion("b1", "La tribuna", TipoUbicacion.BATALLA, 200,
                        List.of(TipoEnemigo.BARRA_BRAVA, TipoEnemigo.PATOVICA), null,
                        List.of("fernet"), List.of()),
                ubicacion("b2", "El pasaje", TipoUbicacion.BATALLA, 260,
                        List.of(TipoEnemigo.TRANSA, TipoEnemigo.FISURA, TipoEnemigo.TRAPITO),
                        null, List.of("alfajor"), List.of()),
                ubicacion("b3", "Centro de jubilados", TipoUbicacion.MEJORA, 150,
                        List.of(), null, List.of("termo"), List.of("jubilado")),
                ubicacion("b4", "El Organizador", TipoUbicacion.BOSS_ZONA, 350,
                        List.of(), "organizador", List.of("celular"), List.of())
        ), false);

        Zona finalZona = new Zona("Zona final · Obelisco", List.of(
                ubicacion("final", "Antagonista final", TipoUbicacion.BOSS_FINAL, 500,
                        List.of(TipoEnemigo.BARRA_BRAVA), "antagonista-final",
                        List.of("escarapela"), List.of())
        ), false);

        return new Partida(party, inventario, new Mapa(List.of(zonaA, zonaB, finalZona), 0));
    }

    public Batalla crearBatalla(UbicacionMapa ubicacion, Party party) {
        if (ubicacion == null || party == null || !ubicacion.esCombate()
                || !ubicacion.puedeIniciarse()) {
            throw new IllegalArgumentException("La ubicación debe permitir un combate");
        }
        FormacionCombate enemigos = new FormacionCombate(TipoBando.ENEMIGO);
        int posicion = 1;
        if (ubicacion.getBossId() != null) {
            enemigos.colocar(crearBoss(ubicacion.getBossId()), posicion++);
        }
        for (TipoEnemigo tipo : ubicacion.getEnemigos()) enemigos.colocar(crearEnemigo(tipo), posicion++);
        return new Batalla(party, enemigos, ubicacion, crearRecompensa(ubicacion));
    }

    public Recompensa crearRecompensa(UbicacionMapa ubicacion) {
        if (ubicacion == null) throw new IllegalArgumentException("La ubicación es obligatoria");
        boolean entrenamiento = ubicacion.getEstado() == EstadoUbicacion.COMPLETADA;
        if (entrenamiento && !ubicacion.esRepetible()) {
            throw new IllegalArgumentException("Esta ubicación no se puede repetir");
        }
        // Entrenamiento conserva XP, pero no vuelve a entregar objetos únicos.
        List<Item> items = new ArrayList<>();
        if (!entrenamiento) {
            for (String tipo : ubicacion.getItemsRecompensa()) items.add(crearItem(tipo));
        }
        return new Recompensa(ubicacion.getExperiencia(), items, ubicacion.getCuracion());
    }

    public Aliado crearAliado(String id) {
        if (id == null) throw new IllegalArgumentException("El ID es obligatorio");
        // Cada case devuelve un aliado nuevo con su rol y sus habilidades configuradas.
        return switch (id) {
            case "gaucho" -> new Guerrero(id, "Gaucho", 150, 40,
                    28, 12, 18, 0, List.of(
                        habilidad("Boleadoras", 1, 10, false, false, 1, true,
                                new EfectoDanio(120), estado(TipoEstado.ATURDIDO, 1, 0, 0.5)),
                        habilidad("Coraje criollo", 1, 12, true, false, ReglasProgresion.NIVEL_SEGUNDA_HABILIDAD, true,
                                estado(TipoEstado.AUMENTO_DEFENSA, 2, 50, 1))),
                    false, 1, 0);
            case "medico" -> new Curador(id, "Médico", 110, 70,
                    18, 8, 20, 0, List.of(
                        habilidad("Primeros auxilios", 1, 12, true, false, 1, true,
                                new EfectoCuracion(55)),
                        habilidad("Alta médica", 1, 15, true, false, ReglasProgresion.NIVEL_SEGUNDA_HABILIDAD, true,
                                new EfectoLimpiarEstados(), new EfectoCuracion(20))),
                    false, 1, 0);
            case "twittero" -> new Arquero(id, "Twittero", 100, 55,
                    24, 6, 28, 0, List.of(
                        habilidad("Hilo viral", 2, 18, false, true, 1, true, new EfectoDanio(90)),
                        habilidad("Cancelado", 1, 12, false, false, ReglasProgresion.NIVEL_SEGUNDA_HABILIDAD, true,
                                estado(TipoEstado.REDUCCION_ATAQUE, 2, 25, 1),
                                estado(TipoEstado.REDUCCION_DEFENSA, 2, 25, 1))),
                    true, 1, 0, 15);
            case "electricista" -> new Mago(id, "Electricista", 105, 80,
                    26, 7, 22, 0, List.of(
                        habilidad("Sobrecarga", 2, 25, false, false, 1, true, new EfectoDanio(200)),
                        habilidad("Corte de luz", 1, 18, false, false, ReglasProgresion.NIVEL_SEGUNDA_HABILIDAD, true,
                                estado(TipoEstado.ATURDIDO, 1, 0, 1))),
                    true, 1, 0);
            case "parrillero" -> new Curador(id, "Parrillero", 130, 65,
                    20, 10, 17, 0, List.of(
                        habilidad("Choripán", 1, 12, true, false, 1, true, new EfectoCuracion(60)),
                        habilidad("Asado completo", 2, 20, true, true, ReglasProgresion.NIVEL_SEGUNDA_HABILIDAD, true,
                                estado(TipoEstado.AUMENTO_ATAQUE, 2, 30, 1))),
                    false, 1, 0);
            case "colectivero" -> new Guerrero(id, "Colectivero", 180, 45,
                    24, 16, 14, 0, List.of(
                        habilidad("Frenada brusca", 2, 18, false, true, 1, true, new EfectoDanio(85)),
                        habilidad("Línea 60", 1, 15, true, false, ReglasProgresion.NIVEL_SEGUNDA_HABILIDAD, true,
                                estado(TipoEstado.AUMENTO_DEFENSA, 2, 40, 1),
                                estado(TipoEstado.ESCUDO, 2, 25, 1))),
                    false, 1, 0);
            case "jubilado" -> new Mago(id, "Jubilado", 120, 75,
                    23, 10, 15, 0, List.of(
                        habilidad("Experiencia de vida", 2, 18, true, true, 1, true,
                                estado(TipoEstado.AUMENTO_DEFENSA, 2, 30, 1)),
                        habilidad("Fila eterna", 2, 22, false, true, ReglasProgresion.NIVEL_SEGUNDA_HABILIDAD, true,
                                estado(TipoEstado.RALENTIZADO, 2, 40, 1))),
                    true, 1, 0);
            default -> throw new IllegalArgumentException("Aliado desconocido: " + id);
        };
    }

    public Enemigo crearEnemigo(TipoEnemigo tipo) {
        return enemigoFactory.crear(tipo);
    }

    public Boss crearBoss(String id) {
        if (id == null) throw new IllegalArgumentException("El ID de boss es obligatorio");
        return switch (id) {
            case "gordo-mortero" -> boss("Gordo Mortero", 280, 28, 10, 16, false, List.of(
                    habilidad("Morterazo", 1, 10, false, false, 1, true, new EfectoDanio(150)),
                    habilidad("Lluvia de piedras", 2, 20, false, true, 1, true, new EfectoDanio(90)),
                    habilidad("Atrincherarse", 1, 12, true, false, 1, true,
                            estado(TipoEstado.ESCUDO, 2, 30, 1))));
            case "organizador" -> boss("El Organizador", 400, 32, 12, 18, false, List.of(
                    habilidad("Orden del día", 2, 18, false, true, 1, true,
                            new EfectoDanio(90), estado(TipoEstado.RALENTIZADO, 2, 25, 1)),
                    habilidad("Golpe de autoridad", 1, 12, false, false, 1, true, new EfectoDanio(160)),
                    habilidad("Cerrar filas", 1, 15, true, false, 1, true,
                            estado(TipoEstado.ESCUDO, 2, 30, 1))));
            case "antagonista-final" -> boss("Antagonista final", 550, 36, 14, 20, true, List.of(
                    habilidad("Cambiar las reglas", 1, 14, true, false, 1, true,
                            estado(TipoEstado.AUMENTO_ATAQUE, 2, 25, 1),
                            estado(TipoEstado.ESCUDO, 2, 20, 0.5)),
                    habilidad("AFAna", 1, 14, false, false, 1, true,
                            new EfectoDanio(110), estado(TipoEstado.REDUCCION_ATAQUE, 2, 25, 1)),
                    habilidad("Todo pelota", 2, 25, false, true, 1, true, new EfectoDanio(100))));
            default -> throw new IllegalArgumentException("Boss desconocido: " + id);
        };
    }

    public Item crearItem(String tipo) {
        return itemFactory.crear(tipo);
    }

    private UbicacionMapa ubicacion(String id, String nombre, TipoUbicacion tipo, int xp,
            List<TipoEnemigo> enemigos, String boss, List<String> items, List<String> aliados) {
        return new UbicacionMapa(id, nombre, tipo, EstadoUbicacion.BLOQUEADA,
                tipo == TipoUbicacion.BATALLA, enemigos, boss, xp, items, 0, aliados);
    }

    private Boss boss(String nombre, int vida, int ataque, int defensa, int velocidad,
            boolean esFinal, List<Habilidad> habilidades) {
        return new Boss(nombre, vida, 120, ataque, defensa, velocidad,
                0, habilidades, true, esFinal);
    }

    private Habilidad habilidad(String nombre, int pa, int mana, boolean propios,
            boolean grupal, int nivel, boolean distancia, EfectoCombate... efectos) {
        return new Habilidad(nombre, pa, mana, propios, grupal, nivel, distancia, List.of(efectos));
    }

    private EfectoEstado estado(TipoEstado tipo, int duracion, int magnitud, double probabilidad) {
        return new EfectoEstado(tipo, duracion, magnitud, probabilidad);
    }
}
