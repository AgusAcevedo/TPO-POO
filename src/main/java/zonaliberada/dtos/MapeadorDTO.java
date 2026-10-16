package zonaliberada.dtos;

import java.util.ArrayList;
import java.util.List;
import zonaliberada.models.Partida;
import zonaliberada.models.ReglasJuego;
import zonaliberada.models.combate.*;
import zonaliberada.models.inventario.*;
import zonaliberada.models.mapa.UbicacionMapa;
import zonaliberada.models.mapa.Zona;
import zonaliberada.models.personajes.*;

/** Traduce información; no altera el modelo ni decide resultados. */
public final class MapeadorDTO {
    // Solo se usan sus métodos static; el constructor privado evita crear instancias.
    private MapeadorDTO() {}

    public static ItemDTO item(Item item) {
        return item(item, 1);
    }

    public static ItemDTO item(Item item, int cantidad) {
        // instanceof comprueba el tipo y permite usar los datos propios de esa subclase.
        if (item instanceof Equipable equipo) {
            return new ItemDTO(item.getId(), item.getNombre(), item.getDescripcion(),
                    equipo.getTipo().name(), equipo.getBonusAtaque(), equipo.getBonusDefensa(),
                    equipo.getBonusVida(), equipo.getBonusMana(), equipo.getBonusVelocidad(),
                    equipo.getBonusPrecision(), equipo.getClasesPermitidas(), false, cantidad);
        }
        if (item instanceof Consumible consumible) {
            return new ItemDTO(item.getId(), item.getNombre(), consumible.getDescripcionEfectos(),
                    "CONSUMIBLE", 0, 0, 0, 0, 0, 0, List.of(),
                    consumible.esGrupal(), cantidad);
        }
        return new ItemDTO(item.getId(), item.getNombre(), item.getDescripcion(),
                "ITEM", 0, 0, 0, 0, 0, 0, List.of(), false, cantidad);
    }

    public static PersonajeDTO personaje(Entidad entidad, String id, int posicion) {
        Aliado aliado = entidad instanceof Aliado a ? a : null;
        String rol = "ENEMIGO";
        if (aliado != null) rol = aliado.tipoClase().name();
        else if (entidad instanceof Boss) rol = "BOSS";

        String atributo = "";
        int valor = 0;
        if (entidad instanceof Guerrero guerrero) {
            atributo = "Furia";
            valor = guerrero.getFuria();
        } else if (entidad instanceof Mago mago) {
            atributo = "Concentración";
            valor = mago.getConcentracion();
        } else if (entidad instanceof Arquero arquero) {
            atributo = "Precisión";
            valor = arquero.getPrecision();
        }

        // Incluimos las bloqueadas para que la ficha pueda mostrar su nivel requerido.
        List<HabilidadDTO> habilidades = new ArrayList<>();
        for (Habilidad habilidad : entidad.getHabilidades()) {
            boolean desbloqueada = aliado == null || aliado.getNivel() >= habilidad.getNivelDesbloqueo();
            habilidades.add(new HabilidadDTO(habilidad.getNombre(), habilidad.getCostoPA(), habilidad.getCostoMana(),
                    habilidad.afectaAliados(), habilidad.esGrupal(), habilidad.getNivelDesbloqueo(),
                    desbloqueada, desbloqueada && entidad.estaVivo()
                    && entidad.tienePA(habilidad.getCostoPA()) && entidad.tieneMana(habilidad.getCostoMana()),
                    habilidad.getDescripcion()));
        }
        List<ItemDTO> equipados = new ArrayList<>();
        if (aliado != null) {
            for (Equipable equipado : aliado.getEquipados()) equipados.add(item(equipado));
        }
        List<EstadoDTO> estados = new ArrayList<>();
        for (EstadoAlterado estado : entidad.getEstados()) {
            estados.add(new EstadoDTO(estado.getTipo(), estado.getTurnosRestantes()));
        }
        // La vista recibe valores efectivos: ya incluyen equipo, estados y pasivas.
        return new PersonajeDTO(id, entidad.getNombre(), rol, posicion,
                entidad.getVida(), entidad.getVidaMaxima(), entidad.getMana(), entidad.getManaMaximo(),
                aliado == null ? 0 : aliado.getNivel(), aliado == null ? 0 : aliado.getExperiencia(),
                aliado == null ? 0 : aliado.getExperienciaSiguienteNivel(),
                entidad.getIniciativa(), entidad.getPuntosAccion(), entidad.getPuntosAccionMaximos(),
                entidad.ataqueTotal(), entidad.defensaTotal(), entidad.velocidadTotal(),
                entidad.isAtaqueDistancia(), entidad.estaVivo(), atributo, valor,
                entidad instanceof Boss jefe ? jefe.getFaseActual() : 0, habilidades, equipados, estados);
    }

    public static List<PersonajeDTO> roster(Partida partida) {
        FormacionCombate formacion = partida.getParty().formacionActual();
        List<PersonajeDTO> personajes = new ArrayList<>();
        for (Aliado aliado : partida.getParty().getRoster()) {
            personajes.add(personaje(aliado, "aliado:" + aliado.getIdPersonaje(),
                    formacion.posicionDe(aliado)));
        }
        return List.copyOf(personajes);
    }

    public static InventarioDTO inventario(Partida partida) {
        List<ItemDTO> items = new ArrayList<>();
        for (Item item : partida.getInventario().getItems()) {
            items.add(item(item, partida.getInventario().getCantidad(item)));
        }
        return new InventarioDTO(items);
    }

    public static MapaDTO mapa(Partida partida) {
        List<MapaDTO.ZonaDTO> zonas = new ArrayList<>();
        for (Zona zona : partida.getMapa().getZonas()) {
            List<MapaDTO.UbicacionDTO> ubicaciones = new ArrayList<>();
            for (UbicacionMapa ubicacion : zona.getUbicaciones()) {
                ubicaciones.add(new MapaDTO.UbicacionDTO(
                        ubicacion.getId(), ubicacion.getNombre(), ubicacion.getTipo(),
                        ubicacion.getEstado(), ubicacion.esRepetible(), partida.puedeIniciar(ubicacion),
                        ubicacion.getExperiencia(), ubicacion.getItemsRecompensa(),
                        ubicacion.getAliadosDesbloqueados()));
            }
            zonas.add(new MapaDTO.ZonaDTO(zona.getNombre(), zona.estaDesbloqueada(),
                    zona.estaCompletada(), ubicaciones));
        }
        return new MapaDTO(partida.getMapa().getIndiceZonaActual(), partida.estaCompletada(), zonas);
    }

    public static BatallaDTO batalla(Batalla batalla) {
        if (batalla == null) return new BatallaDTO(EstadoBatalla.NO_INICIADA, "", "", null, List.of(), List.of(), "");
        EstadoBatalla estado = batalla.getEstado();
        return new BatallaDTO(estado, batalla.getUbicacion().getId(), batalla.getUbicacion().getNombre(),
                batalla.idDe(batalla.obtenerActorActual()),
                formacion(batalla, batalla.formacionAliadaActual()),
                formacion(batalla, batalla.getFormacionEnemiga()), batalla.getMensaje());
    }

    private static List<PersonajeDTO> formacion(Batalla batalla, FormacionCombate formacion) {
        List<PersonajeDTO> miembros = new ArrayList<>();
        for (int posicion = 1; posicion <= ReglasJuego.CANTIDAD_POSICIONES; posicion++) {
            Entidad entidad = formacion.ocupanteEn(posicion);
            if (entidad != null) miembros.add(personaje(entidad, batalla.idDe(entidad), posicion));
        }
        return List.copyOf(miembros);
    }

}
