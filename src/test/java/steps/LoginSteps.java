package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.junit.jupiter.api.Assumptions;
import pages.LoginPage;
import pages.NavegacionPage;
import utils.Config;
import utils.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    private LoginPage login() {
        return new LoginPage(DriverManager.getDriver());
    }

    private NavegacionPage navegacion() {
        return new NavegacionPage(DriverManager.getDriver());
    }

    @Dado("que abro la página para ingresar")
    public void abrirLogin() {
        DriverManager.abrir("/Cuenta/Login");
    }

    @Entonces("el formulario de ingreso se muestra correctamente")
    public void formularioVisible() {
        assertTrue(login().isLoaded());
    }

    @Cuando("ingreso con el correo {string} y la contraseña {string}")
    public void ingresar(String email, String password) {
        login().ingresar(email, password);
    }

    @Cuando("ingreso con la cuenta de prueba")
    public void ingresarCuentaPrueba() {
        Assumptions.assumeFalse(Config.cuentaPruebaEmail().isBlank(),
                "Sin cuenta de prueba configurada (QUERENCIA_TEST_EMAIL / QUERENCIA_TEST_PASSWORD).");
        login().ingresar(Config.cuentaPruebaEmail(), Config.cuentaPruebaPassword());
    }

    @Entonces("veo el error {string}")
    public void veoError(String error) {
        String errores = login().erroresFormulario();
        assertTrue(errores.contains(error), "Errores mostrados: " + errores);
    }

    @Entonces("tengo la sesión iniciada")
    public void sesionIniciada() {
        assertTrue(navegacion().sesionIniciada(), "No se ve el menú «Mi cuenta». URL: " + DriverManager.getDriver().getCurrentUrl());
    }

    @Entonces("no tengo la sesión iniciada")
    public void sinSesion() {
        assertFalse(navegacion().sesionIniciada(), "Se ve el menú «Mi cuenta»");
    }

    @Cuando("cierro la sesión")
    public void cerrarSesion() {
        navegacion().cerrarSesion();
    }

    @Entonces("el menú de administración está visible")
    public void menuAdminVisible() {
        assertTrue(navegacion().menuAdministracionVisible());
    }

    @Entonces("el menú de administración no está visible")
    public void menuAdminOculto() {
        assertFalse(navegacion().menuAdministracionVisible());
    }
}
