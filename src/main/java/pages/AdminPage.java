package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Pantallas del administrador. Cada lista tiene tarjetas con data-testid y data-nombre,
 * así se encuentra la que corresponde al dato creado en el escenario.
 */
public class AdminPage extends BasePage {

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    private void accionEnTarjeta(String tarjeta, String nombre, String boton) {
        By selector = By.cssSelector("[data-testid='" + tarjeta + "'][data-nombre='" + nombre + "'] [data-testid='" + boton + "']");
        var tarjetaElemento = visible(testIdConNombre(tarjeta, nombre));
        click(selector);
        confirmarDialogo();
        // La tarjeta se quita de la lista cuando la acción se completa.
        wait.until(ExpectedConditions.stalenessOf(tarjetaElemento));
    }

    public boolean hayTarjeta(String tarjeta, String nombre) {
        return isDisplayed(testIdConNombre(tarjeta, nombre));
    }

    // ---- Publicaciones por revisar (/Admin/Moderacion)
    public void aprobarPublicacion(String nombreMascota) {
        accionEnTarjeta("moderacion-item", nombreMascota, "btn-aprobar");
    }

    // ---- Asociaciones (/Admin/Asociaciones)
    public void verificarAsociacion(String nombre) {
        var tarjeta = visible(testIdConNombre("asociacion-item", nombre));
        click(By.cssSelector("[data-testid='asociacion-item'][data-nombre='" + nombre + "'] [data-testid='btn-verificar']"));
        confirmarDialogo();
        wait.until(ExpectedConditions.stalenessOf(tarjeta));
    }

    // ---- Colectas (/Admin/Colectas)
    public void aprobarComprobante(String nombreMascota) {
        accionEnTarjeta("comprobante-item", nombreMascota, "btn-aprobar-comprobante");
    }

    // ---- Denuncias (/Admin/Denuncias)
    public void descartarDenuncias(String nombreMascota) {
        accionEnTarjeta("denuncia-item", nombreMascota, "btn-descartar-denuncias");
    }

    public String textoDenuncia(String nombreMascota) {
        return textOf(testIdConNombre("denuncia-item", nombreMascota));
    }
}
