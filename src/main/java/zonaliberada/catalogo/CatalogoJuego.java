package zonaliberada.catalogo;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import zonaliberada.models.combate.*;
import zonaliberada.models.combate.efectos.*;
import zonaliberada.models.inventario.*;
import zonaliberada.models.personajes.*;

/** Singleton de configuración. No guarda inventarios, partidas ni entidades en combate. */
public final class CatalogoJuego {
    private static final CatalogoJuego INSTANCIA = new CatalogoJuego();
    private final Map<String, Consumible> consumibles = new LinkedHashMap<>();
    private final Map<String, EquipableDef> equipables = new LinkedHashMap<>();
    private final Map<TipoEnemigo, EnemigoDef> enemigos = new EnumMap<>(TipoEnemigo.class);

    private CatalogoJuego() {
        cargarConsumibles();
        cargarEquipables();
        cargarEnemigos();
    }

    public static CatalogoJuego getInstancia() { return INSTANCIA; }
    public Consumible buscarConsumible(String tipo) { return consumibles.get(tipo); }
    public EquipableDef buscarEquipable(String tipo) { return equipables.get(tipo); }
    public EnemigoDef buscarEnemigo(TipoEnemigo tipo) { return enemigos.get(tipo); }

    // Valores provisionales de balance: se cambian aquí, no en la UI ni en las fábricas.
    private void cargarConsumibles() {
        consumibles.put("mate", new Consumible(new ConsumibleDef("mate", "Mate",
                "Consumible: Mate", true, false, List.of(new EfectoMana(25)))));
        consumibles.put("fernet", new Consumible(new ConsumibleDef("fernet", "Fernet",
                "Consumible: Fernet", true, false, List.of(
                        new EfectoEstado(TipoEstado.AUMENTO_ATAQUE, 2, 30, 1),
                        new EfectoEstado(TipoEstado.REDUCCION_DEFENSA, 2, 15, 1)))));
        consumibles.put("empanada", new Consumible(new ConsumibleDef("empanada", "Empanada",
                "Consumible: Empanada", true, false, List.of(new EfectoCuracion(35)))));
        consumibles.put("choripan", new Consumible(new ConsumibleDef("choripan", "Choripán",
                "Consumible: Choripán", true, false, List.of(new EfectoCuracion(60)))));
        consumibles.put("alfajor", new Consumible(new ConsumibleDef("alfajor", "Alfajor",
                "Consumible: Alfajor", true, false, List.of(
                        new EfectoCuracion(20), new EfectoMana(15)))));
    }

    private void cargarEquipables() {
        equipables.put("facon", new EquipableDef("Facón", TipoEquipable.ARMA,
                8, 0, 0, 0, 0, 0, List.of(TipoClase.GUERRERO)));
        equipables.put("celular", new EquipableDef("Celular", TipoEquipable.ARMA,
                6, 0, 0, 0, 0, 5, List.of(TipoClase.ARQUERO)));
        equipables.put("espatula", new EquipableDef("Espátula", TipoEquipable.ARMA,
                5, 0, 0, 5, 0, 0, List.of(TipoClase.CURADOR)));
        equipables.put("baston", new EquipableDef("Bastón", TipoEquipable.ARMA,
                6, 0, 0, 10, 0, 0, List.of(TipoClase.MAGO)));
        equipables.put("campera", new EquipableDef("Campera", TipoEquipable.ROPA, 0, 4, 20, 0, 0, 0, List.of()));
        equipables.put("chaleco", new EquipableDef("Chaleco reflectivo", TipoEquipable.ROPA,
                0, 6, 30, 0, 0, 0, List.of()));
        equipables.put("termo", new EquipableDef("Termo", TipoEquipable.ACCESORIO, 0, 0, 0, 20, 0, 0, List.of()));
        equipables.put("sube", new EquipableDef("SUBE", TipoEquipable.ACCESORIO, 0, 0, 0, 0, 3, 0, List.of()));
        equipables.put("escarapela", new EquipableDef("Escarapela", TipoEquipable.ACCESORIO,
                2, 2, 15, 15, 1, 0, List.of()));
    }

    private void cargarEnemigos() {
        enemigos.put(TipoEnemigo.MOTOCHORRO, new EnemigoDef("Motochorro", 55, 16, 4, 28, List.of()));
        enemigos.put(TipoEnemigo.BARRA_BRAVA, new EnemigoDef("Barra brava", 110, 20, 9, 14,
                List.of(new Habilidad("Aliento de la barra", 1, 8, true, false, 1, true,
                        List.of(new EfectoEstado(TipoEstado.AUMENTO_ATAQUE, 2, 30, 1))))));
        enemigos.put(TipoEnemigo.PIQUETERO, new EnemigoDef("Piquetero", 75, 16, 5, 17,
                List.of(new Habilidad("Corte de calle", 1, 8, false, false, 1, true,
                        List.of(new EfectoDanio(70),
                                new EfectoEstado(TipoEstado.RALENTIZADO, 2, 35, 1),
                                new EfectoEstado(TipoEstado.ATURDIDO, 1, 0, 0.3))))));
        enemigos.put(TipoEnemigo.FISURA, new EnemigoDef("Fisura", 65, 18, 3, 23,
                List.of(new Habilidad("Arrebato", 1, 8, false, false, 1, true,
                        List.of(new EfectoDanio(130))))));
        enemigos.put(TipoEnemigo.TRAPITO, new EnemigoDef("Trapito", 70, 15, 4, 20,
                List.of(new Habilidad("Peaje", 1, 7, false, false, 1, true,
                        List.of(new EfectoDanio(70), new EfectoMana(-8),
                                new EfectoEstado(TipoEstado.REDUCCION_DEFENSA, 2, 25, 1))))));
        enemigos.put(TipoEnemigo.TRANSA, new EnemigoDef("Transa", 80, 18, 5, 18,
                List.of(new Habilidad("Veneno", 1, 8, false, false, 1, true,
                        List.of(new EfectoDanio(60),
                                new EfectoEstado(TipoEstado.VENENO, 3, 8, 1))))));
        enemigos.put(TipoEnemigo.PATOVICA, new EnemigoDef("Patovica", 140, 18, 12, 12,
                List.of(new Habilidad("Custodia", 1, 8, true, false, 1, true,
                        List.of(new EfectoEstado(TipoEstado.ESCUDO, 2, 35, 1))))));
    }
}
