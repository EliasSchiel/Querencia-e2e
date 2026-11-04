package utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Crea y cierra el navegador de cada escenario.
 * Usa ThreadLocal para que cada hilo tenga su propio driver (permite correr en paralelo más adelante).
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    /** Tamaño de escritorio y de celular (iPhone 12/13/14). */
    public static final Dimension ESCRITORIO = new Dimension(1366, 900);
    public static final Dimension CELULAR = new Dimension(390, 844);

    private DriverManager() {
    }

    public static void startDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        WebDriver driver = switch (Config.navegador()) {
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (Config.headless()) options.addArguments("--headless=new");
                options.setAcceptInsecureCerts(true);
                yield new EdgeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (Config.headless()) options.addArguments("-headless");
                options.setAcceptInsecureCerts(true);
                yield new FirefoxDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (Config.headless()) options.addArguments("--headless=new");
                // Necesarias en contenedores Linux (GitHub Actions).
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--lang=es-AR");
                // El certificado de desarrollo de ASP.NET (https://localhost) no es de confianza para Chrome.
                options.setAcceptInsecureCerts(true);
                String binario = System.getProperty("chrome.binary");
                if (binario != null && !binario.isBlank()) options.setBinary(binario);
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Navegador no soportado: " + Config.navegador());
        };

        driver.manage().window().setSize(ESCRITORIO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("El WebDriver todavía no fue inicializado.");
        }
        return driver;
    }

    public static boolean iniciado() {
        return DRIVER.get() != null;
    }

    public static void usarTamanio(Dimension tamanio) {
        getDriver().manage().window().setSize(tamanio);
    }

    /** Abre una ruta de Querencia, por ejemplo "/Publicaciones". */
    public static void abrir(String ruta) {
        getDriver().get(Config.baseUrl() + (ruta.startsWith("/") ? ruta : "/" + ruta));
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
