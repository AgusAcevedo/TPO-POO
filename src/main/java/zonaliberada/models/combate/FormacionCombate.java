package zonaliberada.models.combate;

import java.util.ArrayList;
import java.util.List;

import zonaliberada.models.ReglasJuego;
import zonaliberada.models.personajes.Aliado;
import zonaliberada.models.personajes.Enemigo;
import zonaliberada.models.personajes.Entidad;

/** Organiza las seis posiciones de un bando y determina qué integrantes son alcanzables. */
public class FormacionCombate {

    private final TipoBando bando;
    private final PosicionFormacion[] posiciones;

    public FormacionCombate(TipoBando bando) {
        if (bando == null) {throw new IllegalArgumentException("El bando es obligatorio");}

        this.bando = bando;
        this.posiciones = new PosicionFormacion[ReglasJuego.CANTIDAD_POSICIONES];

        // Posiciones 1 a 3: frontales. Posiciones 4 a 6: traseras.
        for (int i = 0; i < posiciones.length; i++) {
            posiciones[i] = new PosicionFormacion(i + 1);
        }
    }

    public TipoBando getBando() {
        return bando;
    }

    public boolean colocar(Entidad entidad, int numero) {
        PosicionFormacion destino = posicionEn(numero);

        // No admitimos entidades del otro bando ni duplicados.
        if (entidad == null || !perteneceAlBando(entidad) || contiene(entidad)) {
            return false;
        }

        // Para reemplazar un ocupante, primero hay que quitarlo.
        if (!destino.estaLibre()) { return false; }

        destino.asignar(entidad);
        return true;
    }

    public boolean quitar(Entidad entidad) {
        int numero = posicionDe(entidad);
        if (numero == 0) return false;
        posiciones[numero - 1].vaciar();
        return true;
    }

    public Entidad ocupanteEn(int numero) {
        // Si la posición está vacía, devuelve null.
        return posicionEn(numero).getOcupante();
    }

    public int posicionDe(Entidad entidad) {
        if (entidad == null) {
            return 0;
        }

        for (PosicionFormacion posicion : posiciones) {
            if (posicion.getOcupante() == entidad) {
                return posicion.getNumero();
            }
        }

        return 0;
    }

    public boolean contiene(Entidad entidad) {
        return posicionDe(entidad) != 0;
    }

    public Entidad[] participantes() {
        List<Entidad> participantes = new ArrayList<>();

        for (PosicionFormacion posicion : posiciones) {
            if (!posicion.estaLibre()) {
                participantes.add(posicion.getOcupante());
            }
        }

        // Incluye derrotados. Devolvemos un arreglo nuevo,
        // sin exponer el arreglo interno de posiciones.
        // Entidad[0] indica a toArray el tipo del arreglo que debe crear.
        return participantes.toArray(new Entidad[0]);
    }

    public Entidad[] miembrosVivos() {
        List<Entidad> vivos = new ArrayList<>();

        for (PosicionFormacion posicion : posiciones) {
            Entidad ocupante = posicion.getOcupante();
            if (ocupante != null && ocupante.estaVivo()) {
                vivos.add(ocupante);
            }
        }

        return vivos.toArray(new Entidad[0]);
    }

    public boolean hayMiembrosVivos() {
        // Para consultar el resultado no hace falta crear una lista de vivos.
        for (PosicionFormacion posicion : posiciones) {
            Entidad ocupante = posicion.getOcupante();
            if (ocupante != null && ocupante.estaVivo()) return true;
        }
        return false;
    }

    public Entidad[] objetivosAlcanzables(boolean aDistancia) {
        if (aDistancia) {
            // A distancia se puede alcanzar cualquier integrante vivo.
            return miembrosVivos();
        }

        List<Entidad> frontalesVivos = new ArrayList<>();

        for (int i = 0; i < ReglasJuego.CANTIDAD_FRONTALES; i++) {
            Entidad ocupante = posiciones[i].getOcupante();

            if (ocupante != null && ocupante.estaVivo()) {
                frontalesVivos.add(ocupante);
            }
        }

        if (!frontalesVivos.isEmpty()) {
            // Mientras haya alguien vivo adelante, protege la retaguardia.
            return frontalesVivos.toArray(new Entidad[0]);
        }

        // Sin frontales vivos, los traseros quedan expuestos.
        return miembrosVivos();
    }

    public FormacionCombate copia() {
        FormacionCombate copia = new FormacionCombate(bando);

        for (PosicionFormacion posicion : posiciones) {
            if (!posicion.estaLibre()) {
                copia.colocar(
                        posicion.getOcupante(),
                        posicion.getNumero());
            }
        }

        // Copiamos las posiciones; cada ocupante sigue siendo el mismo personaje.
        return copia;
    }

    private PosicionFormacion posicionEn(int numero) {
        if (numero < 1 || numero > ReglasJuego.CANTIDAD_POSICIONES) {
            throw new IllegalArgumentException("El número de posición debe estar entre 1 y " + ReglasJuego.CANTIDAD_POSICIONES);
        }

        // Los números comienzan en 1; los índices del arreglo, en 0.
        return posiciones[numero - 1];
    }

    private boolean perteneceAlBando(Entidad entidad) {
        if (bando == TipoBando.ALIADO) {
            return entidad instanceof Aliado;
        }

        // También acepta Boss, porque hereda de Enemigo.
        return entidad instanceof Enemigo;
    }
}
