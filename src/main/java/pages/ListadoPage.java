package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Listado de publicaciones (/Publicaciones) con sus filtros. */
public class ListadoPage extends BasePage {

    private static final By TARJETAS = testId("tarjeta-publicacion");

    public ListadoPage(WebDriver driver) {
        super(driver);
    }

    public ListadoPage esperarCarga() {
        visible(testId("resumen-resultados"));
        return this;
    }

    public void filtrarPorTipo(String tipo) {
        WebElement resumen = visible(testId("resumen-resultados"));
        click(testId("filtro-tipo-" + tipo));
        wait.until(ExpectedConditions.stalenessOf(resumen));
        esperarCarga();
    }

    public void buscar(String texto) {
        WebElement resumen = visible(testId("resumen-resultados"));
        type(By.id("Texto"), texto);
        click(testId("btn-filtrar"));
        wait.until(ExpectedConditions.stalenessOf(resumen));
        esperarCarga();
    }

    public int cantidadTarjetas() {
        return todos(TARJETAS).size();
    }

    /** Tipo de cada tarjeta (Adopcion, PrimerHogar, Transito, PedidoAyuda). */
    public List<String> tiposDeLasTarjetas() {
        return todos(TARJETAS).stream().map(t -> t.getAttribute("data-tipo")).toList();
    }

    public boolean avisoSinResultadosVisible() {
        return isDisplayed(testId("sin-resultados"));
    }

    public String resumen() {
        return textOf(testId("resumen-resultados"));
    }

    /** Abre la primera publicación y devuelve el nombre que mostraba la tarjeta. */
    public String abrirPrimera() {
        WebElement enlace = visible(By.cssSelector("[data-testid='tarjeta-publicacion'] [data-testid='tarjeta-nombre']"));
        String nombre = enlace.getText().trim();
        click(enlace);
        visible(testId("ficha-nombre"));
        return nombre;
    }

    /** Abre la primera publicación que no sea de pedido de ayuda (se puede postular o denunciar). */
    public String abrirPrimeraDeAdopcion() {
        WebElement enlace = visible(By.cssSelector(
                "[data-testid='tarjeta-publicacion']:not([data-tipo='PedidoAyuda']) [data-testid='tarjeta-nombre']"));
        String nombre = enlace.getText().trim();
        click(enlace);
        visible(testId("ficha-nombre"));
        return nombre;
    }

    public void abrirPublicacion(String nombre) {
        click(By.xpath("//a[@data-testid='tarjeta-nombre'][normalize-space()='" + nombre + "']"));
        visible(testId("ficha-nombre"));
    }
}
