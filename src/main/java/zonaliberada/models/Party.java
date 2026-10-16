package zonaliberada.models;

import java.util.ArrayList;
import java.util.List;

import zonaliberada.models.combate.FormacionCombate;
import zonaliberada.models.combate.TipoBando;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.models.personajes.Entidad;

/** Separa todos los aliados desbloqueados (roster) de los seleccionados para combatir. */
public class Party {

    // final impide reemplazar las referencias, no modificar su contenido.
    private final List<Aliado> roster;
    private final FormacionCombate formacion;

    public Party(List<Aliado> roster) {
        if (roster == null) {
            throw new IllegalArgumentException("El roster es obligatorio");
        }

        // Copiamos la lista, pero conservamos los personajes originales.
        List<Aliado> rosterPropio = new ArrayList<>();

        for (Aliado aliado : roster) {
            if (aliado == null) {
                throw new IllegalArgumentException("El roster no puede contener null");
            }

            // Dos objetos con el mismo ID representan al mismo personaje.
            if (buscarPorId(rosterPropio, aliado.getIdPersonaje()) != null) {
                throw new IllegalArgumentException("El roster no puede repetir personajes");
            }

            rosterPropio.add(aliado);
        }

        this.roster = rosterPropio;
        // La selección empieza vacía y se organiza mediante editarFormacion.
        this.formacion = new FormacionCombate(TipoBando.ALIADO);
    }

    public List<Aliado> getRoster() {
        return List.copyOf(roster);
    }

    public FormacionCombate formacionActual() {
        // Copia las posiciones, no los personajes.
        return formacion.copia();
    }

    public Aliado[] seleccionados() {
        Entidad[] participantes = formacion.participantes();
        Aliado[] aliados = new Aliado[participantes.length];
        for (int i = 0; i < participantes.length; i++) {
            aliados[i] = (Aliado) participantes[i];
        }
        return aliados;
    }

    public boolean hayCombateActivo() {
        for (Aliado aliado : roster) {
            if (aliado.estaEnBatalla()) return true;
        }
        return false;
    }

    public Aliado buscarPorId(String idPersonaje) {
        return buscarPorId(roster, idPersonaje);
    }

    public boolean contiene(Aliado aliado) {
        // Consulta si el personaje está desbloqueado por su ID,
        // aunque se reciba otro objeto que lo represente.
        return aliado != null && buscarPorId(roster, aliado.getIdPersonaje()) != null;
    }

    public boolean desbloquearAliado(Aliado aliado) {
        if (hayCombateActivo() || aliado == null || contiene(aliado)) { return false; }

        roster.add(aliado);
        return true;
    }

    public boolean formacionValida() {
        int cantidad = formacion.participantes().length;

        // Valida la cantidad seleccionada, no cuántos están vivos.
        // Durante la edición se permite tener menos integrantes.
        return cantidad >= ReglasJuego.MINIMO_ALIADOS_ACTIVOS
                && cantidad <= ReglasJuego.MAXIMO_ALIADOS_ACTIVOS;
    }

    public boolean editarFormacion(int numero, Aliado aliado) {
        if (hayCombateActivo()) return false;
        // La formación valida que la posición esté entre 1 y 6.
        Entidad ocupante = formacion.ocupanteEn(numero);

        if (aliado == null) { return false; }

        Aliado aliadoDelRoster = buscarPorId(roster, aliado.getIdPersonaje());

        // Todavía no está desbloqueado.
        if (aliadoDelRoster == null) { return false; }

        // Siempre utilizamos el personaje que pertenece a esta partida.
        if (ocupante == aliadoDelRoster) { return true; }

        // No reemplazamos ocupantes automáticamente.
        if (ocupante != null) { return false; }

        boolean yaActivo = formacion.contiene(aliadoDelRoster);

        // Mover un aliado activo no aumenta la cantidad de integrantes.
        if (!yaActivo && formacion.participantes().length >= ReglasJuego.MAXIMO_ALIADOS_ACTIVOS) { return false; }

        // Validamos el destino antes de quitar su ubicación anterior.
        if (yaActivo) { formacion.quitar(aliadoDelRoster); }

        return formacion.colocar(aliadoDelRoster, numero);
    }

    public boolean quitarDeFormacion(Aliado aliado) {
        if (hayCombateActivo()) return false;
        if (aliado == null) { return false; }

        Aliado aliadoDelRoster = buscarPorId(roster, aliado.getIdPersonaje());

        // Quitar de la formación no elimina del roster.
        return aliadoDelRoster != null && formacion.quitar(aliadoDelRoster);
    }

    private static Aliado buscarPorId(
            List<Aliado> aliados, String idPersonaje) {

        for (Aliado aliado : aliados) {
            // equals compara el texto del ID, no la identidad de los objetos String.
            if (aliado.getIdPersonaje().equals(idPersonaje)) {
                return aliado;
            }
        }

        return null; // No está desbloqueado.
    }
}
