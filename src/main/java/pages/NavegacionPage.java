package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Barra de navegación y pie, presentes en todas las páginas. */
public class NavegacionPage extends BasePage {

    @FindBy(css = "[data-testid='btn-menu']")
    private WebElement botonMenu;

    public NavegacionPage(WebDriver driver) {
        super(driver);
    }

    /** Hace clic en un enlace del menú o del pie por su data-testid (nav-adoptar, pie-terminos…). */
    public void irA(String testIdEnlace) {
        By enlace = testId(testIdEnlace);
        if (testIdEnlace.startsWith("nav-") && botonMenu.isDisplayed() && !driver.findElement(enlace).isDisplayed()) {
            abrirMenuMovil();
        }
        click(enlace);
    }

    public boolean botonMenuVisible() {
        return isDisplayed(testId("btn-menu"));
    }

    public boolean enlaceVisible(String testIdEnlace) {
        return driver.findElement(testId(testIdEnlace)).isDisplayed();
    }

    public void abrirMenuMovil() {
        click(botonMenu);
        wait.until(ExpectedConditions.visibilityOfElementLocated(testId("nav-adoptar")));
    }

    public boolean sesionIniciada() {
        return isDisplayed(testId("menu-cuenta"));
    }

    public boolean menuAdministracionVisible() {
        return existe(testId("menu-admin"));
    }

    public void cerrarSesion() {
        if (botonMenu.isDisplayed()) abrirMenuMovil();
        click(testId("menu-cuenta"));
        click(testId("btn-cerrar-sesion"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(testId("nav-ingresar")));
    }

    /** Ancho del documento contra el ancho de la ventana: si es mayor, hay scroll horizontal. */
    public boolean tieneScrollHorizontal() {
        Object r = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "return document.documentElement.scrollWidth > document.documentElement.clientWidth + 1;");
        return Boolean.TRUE.equals(r);
    }
}
