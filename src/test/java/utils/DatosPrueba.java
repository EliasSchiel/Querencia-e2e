package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Genera datos únicos para cada ejecución, así los escenarios de regresión se pueden
 * repetir sin chocar con lo que crearon antes (correos, CUIT y nombres).
 */
public final class DatosPrueba {

    private DatosPrueba() {
    }

    /** Sufijo corto y único: segundos del día + 3 dígitos al azar. */
    public static String sufijo() {
        long segundos = System.currentTimeMillis() / 1000 % 100_000;
        return segundos + "" + ThreadLocalRandom.current().nextInt(100, 1000);
    }

    public static String email(String prefijo) {
        return prefijo + "." + sufijo() + "@querencia.test";
    }

    /** Nombre de mascota fácil de reconocer en la base: "E2E Tito 12345678". */
    public static String nombreMascota(String nombre) {
        return "E2E " + nombre + " " + sufijo();
    }

    /** Fecha ISO (aaaa-MM-dd), el valor interno de un input type="date" en cualquier idioma. */
    public static String fechaHaceAnios(int anios) {
        return LocalDate.now().minusYears(anios).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String fechaEnDias(int dias) {
        return LocalDate.now().plusDays(dias).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * CUIT válido de persona jurídica (prefijo 30) con dígito verificador correcto,
     * con el mismo algoritmo que valida Querencia (Helpers/Validaciones.cs).
     */
    public static String cuitValido() {
        int[] pesos = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        while (true) {
            String base = "30" + String.format("%08d", ThreadLocalRandom.current().nextInt(10_000_000, 99_999_999));
            int suma = 0;
            for (int i = 0; i < 10; i++) suma += (base.charAt(i) - '0') * pesos[i];
            int verificador = 11 - suma % 11;
            if (verificador == 11) verificador = 0;
            if (verificador == 10) continue; // ese número no tiene CUIT válido: se prueba otro
            return base + verificador;
        }
    }

    /** Ruta absoluta de un archivo de src/test/resources/archivos. */
    public static String archivo(String nombre) {
        try {
            var url = DatosPrueba.class.getClassLoader().getResource("archivos/" + nombre);
            if (url == null) throw new IllegalArgumentException("No existe el archivo de prueba " + nombre);
            return java.nio.file.Paths.get(url.toURI()).toAbsolutePath().toString();
        } catch (java.net.URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
