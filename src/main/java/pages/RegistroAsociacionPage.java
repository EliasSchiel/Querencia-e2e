package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Registro de una asociación protectora (/Cuenta/RegistroAsociacion). */
public class RegistroAsociacionPage extends BasePage {

    public RegistroAsociacionPage(WebDriver driver) {
        super(driver);
    }

    public void completar(String nombre, String cuit, String descripcion, String provincia, String localidad,
                          String documentacion, String responsable, String telefono, String email, String password) {
        type(By.id("Nombre"), nombre);
        type(By.id("Cuit"), cuit);
        type(By.id("Descripcion"), descripcion);
        seleccionar(By.id("ProvinciaId"), provincia);
        seleccionarCuandoAparezca(By.id("LocalidadId"), localidad);
        if (documentacion != null) adjuntar(By.id("Documentacion"), documentacion);
        type(By.id("Responsable"), responsable);
        type(By.id("Telefono"), telefono);
        type(By.id("Email"), email);
        type(By.id("Password"), password);
        type(By.id("ConfirmarPassword"), password);
    }

    public void enviar() {
        click(testId("btn-registrar-asociacion"));
    }
}
