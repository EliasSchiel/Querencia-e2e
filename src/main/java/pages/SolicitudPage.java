package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Formulario para postularse a adoptar (/Solicitudes/Crear) y la lista de solicitudes recibidas. */
public class SolicitudPage extends BasePage {

    public SolicitudPage(WebDriver driver) {
        super(driver);
    }

    public void completar(String vivienda, boolean patio, String mensaje) {
        seleccionar(By.id("TipoVivienda"), vivienda);
        if (patio) click(By.id("TienePatio"));
        type(By.id("Mensaje"), mensaje);
    }

    public void enviar() {
        click(testId("btn-enviar-solicitud"));
    }

    // ---- Lista de solicitudes recibidas (/Publicaciones/Solicitudes/{id})

    public int solicitudesRecibidas() {
        visible(testId("tarjeta-solicitud"));
        return todos(testId("tarjeta-solicitud")).size();
    }

    /** Elige a la persona que se postuló (botón «Elegir a …» + confirmación). */
    public void elegirA(String nombreCorto) {
        click(By.xpath("//button[@data-testid='btn-elegir-solicitud'][contains(normalize-space(), '" + nombreCorto + "')]"));
        confirmarDialogo();
        visible(testId("btn-registrar-entrega"));
    }

    public void irARegistrarEntrega() {
        click(testId("btn-registrar-entrega"));
    }

    // ---- Registrar la entrega (/Adopciones/Registrar)

    public void registrarAdopcion() {
        click(testId("btn-registrar-adopcion"));
        visible(testId("adopcion-titulo"));
    }

    public String tituloAdopcion() {
        return textOf(testId("adopcion-titulo"));
    }
}
