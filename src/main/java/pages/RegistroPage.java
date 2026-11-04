package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Registro de una persona (/Cuenta/Registro). */
public class RegistroPage extends BasePage {

    public RegistroPage(WebDriver driver) {
        super(driver);
    }

    public void completar(String nombre, String email, String telefono, String provincia, String localidad,
                          String password, String confirmacion) {
        type(By.id("NombreCompleto"), nombre);
        type(By.id("Email"), email);
        type(By.id("Telefono"), telefono);
        seleccionar(By.id("ProvinciaId"), provincia);
        seleccionarCuandoAparezca(By.id("LocalidadId"), localidad);
        type(By.id("Password"), password);
        type(By.id("ConfirmarPassword"), confirmacion);
    }

    public void enviar() {
        click(testId("btn-crear-cuenta"));
    }
}
