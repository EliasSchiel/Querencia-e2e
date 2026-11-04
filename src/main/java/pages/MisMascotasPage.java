package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** «Mis mascotas» (/Mascotas) y el formulario para cargar una. */
public class MisMascotasPage extends BasePage {

    public MisMascotasPage(WebDriver driver) {
        super(driver);
    }

    /** Carga una mascota con los datos mínimos y vuelve al listado de «Mis mascotas». */
    public void cargarMascota(String nombre, String especie, String raza, String sexo, String tamanio,
                              String fechaNacimientoIso, String descripcion) {
        click(testId("btn-cargar-mascota"));
        type(By.id("Nombre"), nombre);
        seleccionar(By.id("selEspecie"), especie);
        seleccionarCuandoAparezca(By.id("selRaza"), raza);
        seleccionar(By.id("Sexo"), sexo);
        seleccionar(By.id("Tamanio"), tamanio);
        escribirFecha(By.id("FechaNacAprox"), fechaNacimientoIso);
        type(By.id("Descripcion"), descripcion);
        click(testId("btn-guardar-mascota"));
        visible(testIdConNombre("fila-mascota", nombre));
    }

    public boolean tieneMascota(String nombre) {
        return isDisplayed(testIdConNombre("fila-mascota", nombre));
    }

    /** Abre el formulario para publicar la mascota. */
    public void publicar(String nombre) {
        click(By.cssSelector("[data-testid='fila-mascota'][data-nombre='" + nombre + "'] [data-testid='btn-publicar']"));
        visible(testId("btn-enviar-publicacion"));
    }
}
