package utils;

/**
 * Configuración de la suite. Cada valor se toma, en este orden, de:
 * 1. una propiedad del sistema (-Dbaseurl=...),
 * 2. una variable de entorno (BASE_URL=...), útil en GitHub Actions,
 * 3. un valor por defecto pensado para correr contra Querencia en la PC.
 */
public final class Config {

    private Config() {
    }

    /** Dirección de Querencia. Por defecto, la que levanta Visual Studio con F5. */
    public static String baseUrl() {
        String url = leer("baseurl", "BASE_URL", "http://localhost:5180");
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public static String navegador() {
        return leer("browser", "BROWSER", "chrome").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(leer("headless", "HEADLESS", "false"));
    }

    /** true cuando la suite corre contra la página publicada (no contra localhost). */
    public static boolean esProduccion() {
        String url = baseUrl();
        return !url.contains("localhost") && !url.contains("127.0.0.1");
    }

    // ---- Credenciales

    /** Administrador de los datos de prueba (solo existe en desarrollo). */
    public static String adminEmail() {
        return leer("admin.email", "QUERENCIA_ADMIN_EMAIL", "admin@querencia.com");
    }

    public static String adminPassword() {
        return leer("admin.password", "QUERENCIA_ADMIN_PASSWORD", "Admin123");
    }

    /** Cuenta de prueba en producción. Se guarda en los Secrets de GitHub, nunca en el código. */
    public static String cuentaPruebaEmail() {
        return leer("cuenta.email", "QUERENCIA_TEST_EMAIL", "");
    }

    public static String cuentaPruebaPassword() {
        return leer("cuenta.password", "QUERENCIA_TEST_PASSWORD", "");
    }

    private static String leer(String propiedad, String variableEntorno, String porDefecto) {
        String valor = System.getProperty(propiedad);
        if (valor == null || valor.isBlank() || valor.startsWith("${")) {
            valor = System.getenv(variableEntorno);
        }
        return valor == null || valor.isBlank() ? porDefecto : valor.trim();
    }
}
