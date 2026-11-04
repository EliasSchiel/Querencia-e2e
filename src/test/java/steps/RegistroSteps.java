package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import pages.RegistroPage;
import utils.Contexto;
import utils.DatosPrueba;
import utils.DriverManager;

public class RegistroSteps {

    static final String PROVINCIA = "La Pampa";
    static final String LOCALIDAD = "Santa Rosa";
    static final String CLAVE = "Clave123";

    private RegistroPage registro() {
        return new RegistroPage(DriverManager.getDriver());
    }

    @Dado("que abro la página para crear una cuenta")
    public void abrirRegistro() {
        DriverManager.abrir("/Cuenta/Registro");
    }

    @Cuando("completo el registro con el correo {string}, la contraseña {string} y la confirmación {string}")
    public void completarRegistro(String email, String clave, String confirmacion) {
        registro().completar("Prueba E2E", email, "2954123456", PROVINCIA, LOCALIDAD, clave, confirmacion);
    }

    @Cuando("completo el registro con un correo nuevo y la contraseña {string}")
    public void completarRegistroNuevo(String clave) {
        registro().completar("Prueba E2E", DatosPrueba.email("registro"), "2954123456", PROVINCIA, LOCALIDAD, clave, clave);
    }

    @Cuando("envío el registro")
    public void enviar() {
        registro().enviar();
    }

    /**
     * Registra una persona nueva con un correo único y guarda sus datos en el contexto con su
     * nombre como alias ("Ana" → Contexto "Ana.email"), para volver a ingresar con ella más adelante.
     */
    @Dado("que {string} se registró como usuaria/usuario nueva/nuevo")
    public static void registrarPersona(String nombre) {
        var driver = DriverManager.getDriver();
        driver.manage().deleteAllCookies();
        DriverManager.abrir("/Cuenta/Registro");
        String email = DatosPrueba.email(nombre.toLowerCase());
        RegistroPage pagina = new RegistroPage(driver);
        pagina.completar(nombre + " Prueba E2E", email, "2954123456", PROVINCIA, LOCALIDAD, CLAVE, CLAVE);
        pagina.enviar();
        if (!new pages.NavegacionPage(driver).sesionIniciada()) {
            throw new AssertionError("No se pudo registrar a " + nombre + ": " + pagina.erroresFormulario());
        }
        Contexto.guardar(nombre + ".email", email);
        Contexto.guardar(nombre + ".password", CLAVE);
    }
}
