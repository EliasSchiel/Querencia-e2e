package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** «Mis publicaciones» (/Publicaciones/Mias). */
public class MisPublicacionesPage extends BasePage {

    public MisPublicacionesPage(WebDriver driver) {
        super(driver);
    }

    private By fila(String nombre) {
        return testIdConNombre("fila-publicacion", nombre);
    }

    /** Estado interno de la publicación: Pendiente, Publicada, EnProceso, Adoptada, Finalizada… */
    public String estado(String nombre) {
        return visible(fila(nombre)).getAttribute("data-estado");
    }

    public String textoFila(String nombre) {
        return visible(fila(nombre)).getText();
    }

    public void verSolicitudes(String nombre) {
        click(By.cssSelector("[data-testid='fila-publicacion'][data-nombre='" + nombre + "'] [data-testid='btn-ver-solicitudes']"));
    }

    public void registrarEntrega(String nombre) {
        click(By.cssSelector("[data-testid='fila-publicacion'][data-nombre='" + nombre + "'] [data-testid='btn-registrar-entrega']"));
    }

    public void subirComprobante(String nombre) {
        click(By.cssSelector("[data-testid='fila-publicacion'][data-nombre='" + nombre + "'] [data-testid='btn-subir-comprobante']"));
    }

    public void abrirFicha(String nombre) {
        click(By.xpath("//*[@data-testid='fila-publicacion'][@data-nombre='" + nombre + "']//a[normalize-space()='Ver' or normalize-space()='Actualizar recaudado']"));
        visible(testId("ficha-nombre"));
    }
}
