package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Formulario para rendir una colecta con su comprobante (/Publicaciones/Rendir/{id}). */
public class RendirPage extends BasePage {

    public RendirPage(WebDriver driver) {
        super(driver);
    }

    public void completar(String descripcion, int monto, String archivo) {
        type(By.id("Descripcion"), descripcion);
        type(By.id("MontoRendido"), String.valueOf(monto));
        adjuntar(By.id("Archivo"), archivo);
    }

    public void enviar() {
        click(testId("btn-enviar-comprobante"));
    }
}
