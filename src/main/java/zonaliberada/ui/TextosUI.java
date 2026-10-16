package zonaliberada.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import zonaliberada.dtos.ItemDTO;
import zonaliberada.models.combate.TipoEstado;

/** Solo formato de presentación; no calcula bonus, costos ni efectos. */
public final class TextosUI {
    private TextosUI() {}

    public static String numero(double valor) {
        // Evita mostrar decimales innecesarios: 25.0 se presenta como 25.
        return valor == Math.rint(valor) ? Long.toString((long) valor)
                : String.format(Locale.ROOT, "%.1f", valor);
    }

    public static String legible(String valor) {
        if (valor == null || valor.isBlank()) return "";
        String texto = valor.toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    public static String html(String texto) {
        // Escapa los caracteres que JLabel interpretaría como etiquetas HTML.
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\n", "<br>");
    }

    public static String estado(TipoEstado tipo) {
        return switch (tipo) {
            case VENENO -> "Veneno";
            case ATURDIDO -> "Aturdido";
            case ESCUDO -> "Escudo";
            case RALENTIZADO -> "Ralentizado";
            case AUMENTO_ATAQUE -> "Ataque aumentado";
            case REDUCCION_ATAQUE -> "Ataque reducido";
            case AUMENTO_DEFENSA -> "Defensa aumentada";
            case REDUCCION_DEFENSA -> "Defensa reducida";
        };
    }

    public static String bonus(ItemDTO item) {
        List<String> valores = new ArrayList<>();
        agregar(valores, "ATQ", item.getBonusAtaque());
        agregar(valores, "DEF", item.getBonusDefensa());
        agregar(valores, "HP máx.", item.getBonusVida());
        agregar(valores, "MP máx.", item.getBonusMana());
        agregar(valores, "VEL", item.getBonusVelocidad());
        agregar(valores, "Precisión", item.getBonusPrecision());
        return valores.isEmpty() ? "Sin bonus de estadísticas" : String.join(" · ", valores);
    }

    private static void agregar(List<String> lista, String nombre, int valor) {
        if (valor != 0) lista.add(nombre + " +" + valor);
    }
}
