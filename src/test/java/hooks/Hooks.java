package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.junit.jupiter.api.Assumptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.Config;
import utils.Contexto;
import utils.DriverManager;

import java.nio.charset.StandardCharsets;

public class Hooks {

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        Contexto.limpiar();
        DriverManager.startDriver();
        scenario.log("Querencia: " + Config.baseUrl() + " · navegador: " + Config.navegador()
                + (Config.headless() ? " (headless)" : ""));
    }

    /**
     * Protección: los escenarios @regresion crean cuentas, mascotas y denuncias. Nunca se ejecutan
     * contra la página publicada, aunque alguien olvide filtrar las etiquetas.
     */
    @Before(value = "@regresion", order = 1)
    public void soloEnEntornoLocal() {
        Assumptions.assumeFalse(Config.esProduccion(),
                "Los escenarios @regresion crean datos: solo se ejecutan contra Querencia local.");
    }

    /** Los escenarios @movil se ejecutan con el tamaño de pantalla de un celular. */
    @Before(value = "@movil", order = 1)
    public void tamanioCelular() {
        DriverManager.usarTamanio(DriverManager.CELULAR);
    }

    /** Si un paso falla, se adjunta una captura al reporte (Cucumber HTML y Allure). */
    @AfterStep
    public void capturaSiFalla(Scenario scenario) {
        if (scenario.isFailed() && DriverManager.iniciado()) {
            adjuntarCaptura(scenario, "Captura del fallo");
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (DriverManager.iniciado()) {
            if (scenario.isFailed()) {
                String url = DriverManager.getDriver().getCurrentUrl();
                scenario.attach(url.getBytes(StandardCharsets.UTF_8), "text/plain", "URL al fallar");
            }
            DriverManager.quitDriver();
        }
    }

    private static void adjuntarCaptura(Scenario scenario, String nombre) {
        try {
            byte[] png = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(png, "image/png", nombre);
        } catch (Exception ignored) {
            // Si el navegador ya no responde, el reporte queda sin captura.
        }
    }
}
