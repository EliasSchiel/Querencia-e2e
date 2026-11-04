package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.junit.jupiter.api.Assumptions;
import pages.BasePage;
import pages.InicioPage;
import pages.NavegacionPage;
import utils.Config;
import utils.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

/** Pasos comunes de navegación, páginas públicas, seguridad y versión celular. */
public class NavegacionSteps {

    private NavegacionPage navegacion() {
        return new NavegacionPage(DriverManager.getDriver());
    }

    private BasePage pagina() {
        return new InicioPage(DriverManager.getDriver());
    }

    @Dado("que abro la página de inicio")
    public void abrirInicio() {
        DriverManager.abrir("/");
    }

    @Cuando("abro la dirección {string}")
    public void abrirDireccion(String ruta) {
        DriverManager.abrir(ruta);
    }

    @Entonces("veo la portada de Querencia")
    public void veoPortada() {
        assertTrue(new InicioPage(DriverManager.getDriver()).portadaVisible(), "No se ve la portada");
    }

    @Entonces("el título de la pestaña contiene {string}")
    public void tituloPestania(String texto) {
        assertTrue(pagina().getTitle().contains(texto), "Título: " + pagina().getTitle());
    }

    @Cuando("entro al enlace {string} del menú/pie")
    public void entrarAlEnlace(String testId) {
        navegacion().irA(testId);
    }

    @Entonces("la dirección contiene {string}")
    public void direccionContiene(String texto) {
        String url = DriverManager.getDriver().getCurrentUrl();
        assertTrue(url.contains(texto), "La dirección es " + url);
    }

    @Entonces("el título principal contiene {string}")
    public void tituloPrincipal(String texto) {
        String titulo = pagina().tituloPrincipal();
        assertTrue(titulo.contains(texto), "El título principal es «" + titulo + "»");
    }

    @Entonces("veo el aviso {string}")
    public void veoAviso(String texto) {
        assertTrue(pagina().tituloPrincipal().contains(texto), "Título: " + pagina().tituloPrincipal());
    }

    // ---- Seguridad

    @Entonces("me lleva a la página para ingresar")
    public void meLlevaAlLogin() {
        String url = DriverManager.getDriver().getCurrentUrl();
        assertTrue(url.contains("/Cuenta/Login"), "No pidió ingresar. Dirección: " + url);
    }

    @Entonces("después de ingresar volvería a {string}")
    public void returnUrl(String ruta) {
        String url = java.net.URLDecoder.decode(DriverManager.getDriver().getCurrentUrl(), java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(url.toLowerCase().contains("returnurl=" + ruta.toLowerCase()), "ReturnUrl incorrecto: " + url);
    }

    @Entonces("el contenido incluye {string}")
    public void contenidoIncluye(String texto) {
        String fuente = DriverManager.getDriver().getPageSource();
        assertTrue(fuente.contains(texto), "No se encontró «" + texto + "» en " + DriverManager.getDriver().getCurrentUrl());
    }

    @Cuando("abro el sitio con http")
    public void abrirConHttp() {
        Assumptions.assumeTrue(Config.baseUrl().startsWith("https://"),
                "Solo aplica al sitio publicado con https (la base actual es " + Config.baseUrl() + ").");
        DriverManager.getDriver().get(Config.baseUrl().replaceFirst("^https://", "http://") + "/");
    }

    @Entonces("la dirección final empieza con {string}")
    public void direccionFinalEmpieza(String prefijo) {
        String url = DriverManager.getDriver().getCurrentUrl();
        assertTrue(url.startsWith(prefijo), "La dirección final es " + url);
    }

    // ---- Celular

    @Entonces("el botón del menú está visible")
    public void botonMenuVisible() {
        assertTrue(navegacion().botonMenuVisible(), "No se ve el botón del menú");
    }

    @Entonces("el enlace {string} está oculto")
    public void enlaceOculto(String testId) {
        assertFalse(navegacion().enlaceVisible(testId), testId + " debería estar oculto");
    }

    @Entonces("el enlace {string} está visible")
    public void enlaceVisible(String testId) {
        assertTrue(navegacion().enlaceVisible(testId), testId + " debería estar visible");
    }

    @Cuando("abro el menú")
    public void abroElMenu() {
        navegacion().abrirMenuMovil();
    }

    @Entonces("la página no tiene desplazamiento horizontal")
    public void sinScrollHorizontal() {
        assertFalse(navegacion().tieneScrollHorizontal(),
                "La página se desborda hacia los costados en " + DriverManager.getDriver().getCurrentUrl());
    }
}
