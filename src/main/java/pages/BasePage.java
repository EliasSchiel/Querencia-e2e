package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base de todas las páginas (Page Object Model). Centraliza las esperas explícitas para
 * que los pasos no dependan de tiempos fijos, y los selectores por data-testid, que son
 * estables aunque cambien los textos o los estilos de Querencia.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ---------------------------------------------------------------- selectores

    /** Selector por atributo data-testid. */
    public static By testId(String id) {
        return By.cssSelector("[data-testid='" + id + "']");
    }

    /** Elemento con data-testid y data-nombre (filas de listados: una mascota, una publicación). */
    public static By testIdConNombre(String id, String nombre) {
        return By.cssSelector("[data-testid='" + id + "'][data-nombre='" + nombre + "']");
    }

    // ---------------------------------------------------------------- acciones

    protected WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void click(WebElement element) {
        scrollIntoView(element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void click(By by) {
        click(wait.until(ExpectedConditions.presenceOfElementLocated(by)));
    }

    protected void type(WebElement element, String text) {
        WebElement visible = wait.until(ExpectedConditions.visibilityOf(element));
        visible.clear();
        visible.sendKeys(text);
    }

    protected void type(By by, String text) {
        type(visible(by), text);
    }

    protected String textOf(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    protected String textOf(By by) {
        return visible(by).getText().trim();
    }

    protected boolean isDisplayed(By by) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean existe(By by) {
        return !driver.findElements(by).isEmpty();
    }

    protected List<WebElement> todos(By by) {
        return driver.findElements(by);
    }

    /** Elige una opción de un select por su texto visible. */
    protected void seleccionar(By by, String textoVisible) {
        new Select(visible(by)).selectByVisibleText(textoVisible);
    }

    /**
     * Elige una opción de un select que se carga por AJAX (por ejemplo, la localidad después de
     * elegir la provincia): espera a que la opción aparezca antes de elegirla.
     */
    protected void seleccionarCuandoAparezca(By select, String textoVisible) {
        wait.until(d -> {
            WebElement el = d.findElement(select);
            return el.findElements(By.xpath("./option[normalize-space()='" + textoVisible + "']")).size() > 0;
        });
        seleccionar(select, textoVisible);
    }

    /**
     * Completa un input type="date" con una fecha ISO (aaaa-MM-dd). Se hace por JavaScript porque
     * el formato para tipear cambia según el idioma del navegador (dd/mm o mm/dd).
     */
    protected void escribirFecha(By by, String fechaIso) {
        WebElement input = visible(by);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", input, fechaIso);
    }

    /** Adjunta un archivo a un input type="file". */
    protected void adjuntar(By by, String rutaAbsoluta) {
        driver.findElement(by).sendKeys(rutaAbsoluta);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', behavior:'instant'});", element);
    }

    // ---------------------------------------------------------------- SweetAlert2

    /** Confirma el cuadro de SweetAlert ("¿Seguro?") que abren los botones de acción. */
    public void confirmarDialogo() {
        click(By.cssSelector(".swal2-popup .swal2-confirm"));
    }

    /** Escribe el motivo en un cuadro de SweetAlert con textarea y lo confirma. */
    public void escribirMotivoYConfirmar(String motivo) {
        type(By.cssSelector(".swal2-popup textarea.swal2-textarea"), motivo);
        confirmarDialogo();
    }

    /** Texto del aviso (toast) de SweetAlert que aparece después de una acción. */
    public String textoAviso() {
        return textOf(By.cssSelector(".swal2-toast .swal2-title, .swal2-popup:not(.swal2-toast) .swal2-html-container"));
    }

    /** Espera a que la página se recargue después de una acción por AJAX. */
    protected void esperarRecarga(WebElement elementoAnterior) {
        wait.until(ExpectedConditions.stalenessOf(elementoAnterior));
    }

    // ---------------------------------------------------------------- datos de la página

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String tituloPrincipal() {
        return textOf(By.cssSelector("main h1"));
    }

    /** Mensajes de error de un formulario (resumen + errores por campo). */
    public String erroresFormulario() {
        wait.until(d -> !d.findElements(By.cssSelector(
                "[data-testid='errores-formulario'] li, .field-validation-error:not(:empty)")).isEmpty());
        StringBuilder sb = new StringBuilder();
        for (WebElement e : driver.findElements(By.cssSelector(
                "[data-testid='errores-formulario'] li, .field-validation-error"))) {
            if (!e.getText().isBlank()) sb.append(e.getText().trim()).append(" | ");
        }
        return sb.toString();
    }
}
