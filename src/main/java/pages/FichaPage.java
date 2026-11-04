package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Ficha de una publicación (/Publicaciones/Detalle/{id}). */
public class FichaPage extends BasePage {

    public FichaPage(WebDriver driver) {
        super(driver);
    }

    public String nombre() {
        return textOf(testId("ficha-nombre"));
    }

    public String ubicacion() {
        return textOf(testId("ficha-ubicacion"));
    }

    public boolean datosVisibles() {
        return isDisplayed(testId("ficha-datos"));
    }

    public void postularse() {
        click(testId("btn-postularse"));
    }

    public boolean botonPostularseVisible() {
        return isDisplayed(testId("btn-postularse"));
    }

    // ---- Denuncias (modal)

    public void abrirDenuncia() {
        click(testId("btn-denunciar"));
        visible(By.cssSelector("#modalGeneral form"));
    }

    public void denunciar(String motivo, String detalle) {
        click(By.xpath("//div[@id='modalGeneral']//label[normalize-space()='" + motivo + "']"));
        type(By.cssSelector("#modalGeneral textarea[name='Detalle']"), detalle);
        click(testId("btn-enviar-denuncia"));
    }

    public boolean avisoYaDenuncioVisible() {
        return isDisplayed(testId("aviso-ya-denuncio"));
    }

    // ---- Colectas (pedido de ayuda)

    public boolean rendicionVerificadaVisible() {
        return isDisplayed(testId("rendicion-verificada"));
    }

    /** La asociación informa lo recaudado (input y botón de la ficha propia). */
    public void actualizarRecaudado(int monto) {
        var input = visible(By.id("montoRecaudado"));
        type(input, String.valueOf(monto));
        click(By.id("btnRecaudado"));
        // La página se recarga después de guardar: se espera a que el campo viejo desaparezca.
        esperarRecarga(input);
        wait.until(d -> d.findElement(By.id("montoRecaudado")).getAttribute("value").equals(String.valueOf(monto)));
    }

    /** Cierra la colecta (botón «Cerrar pedido» + confirmación). */
    public void cerrarColecta() {
        var boton = visible(By.xpath("//button[normalize-space()='Cerrar pedido']"));
        click(boton);
        confirmarDialogo();
        esperarRecarga(boton);
    }
}
