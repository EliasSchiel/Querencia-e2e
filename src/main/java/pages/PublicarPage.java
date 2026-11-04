package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Formulario para publicar una mascota (/Publicaciones/Crear). */
public class PublicarPage extends BasePage {

    public PublicarPage(WebDriver driver) {
        super(driver);
    }

    /** Solo las asociaciones verificadas eligen el tipo: Adopcion, PrimerHogar, Transito o PedidoAyuda. */
    public void elegirTipo(String tipo) {
        click(By.cssSelector("input[type='radio'][name='Tipo'][value='" + tipo + "']"));
    }

    public void completarUbicacionSiFalta(String provincia, String localidad) {
        if (driver.findElement(By.id("LocalidadId")).getAttribute("value").isBlank()) {
            seleccionar(By.id("ProvinciaId"), provincia);
            seleccionarCuandoAparezca(By.id("LocalidadId"), localidad);
        }
    }

    public void completarColecta(String detalle, int montoObjetivo, String fechaLimiteIso) {
        type(By.id("DetalleAyuda"), detalle);
        type(By.id("MontoObjetivo"), String.valueOf(montoObjetivo));
        escribirFecha(By.id("FechaLimite"), fechaLimiteIso);
    }

    public void enviar() {
        click(testId("btn-enviar-publicacion"));
    }

    public String notaColectas() {
        return textOf(testId("nota-colectas"));
    }

    public boolean sigueEnElFormulario() {
        return existe(testId("btn-enviar-publicacion"));
    }
}
