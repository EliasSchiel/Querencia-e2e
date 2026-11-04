package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import pages.AdminPage;
import pages.FichaPage;
import pages.ListadoPage;
import utils.Contexto;
import utils.DriverManager;
import utils.Sesion;

import static org.junit.jupiter.api.Assertions.*;

public class DenunciasSteps {

    private FichaPage ficha() {
        return new FichaPage(DriverManager.getDriver());
    }

    @Cuando("denuncio la publicación por {string} con el detalle {string}")
    public void denuncio(String motivo, String detalle) {
        ficha().abrirDenuncia();
        ficha().denunciar(motivo, detalle);
    }

    @Entonces("veo el aviso de confirmación {string}")
    public void avisoConfirmacion(String texto) {
        String aviso = ficha().textoAviso();
        assertTrue(aviso.contains(texto), "Aviso: " + aviso);
    }

    @Entonces("la ficha indica que ya la denuncié")
    public void yaDenuncie() {
        assertTrue(ficha().avisoYaDenuncioVisible(), "La ficha debería indicar que ya la denunciaste");
    }

    @Cuando("el administrador abre las denuncias")
    public void adminAbreDenuncias() {
        Sesion.ingresarComoAdministrador();
        DriverManager.abrir("/Admin/Denuncias");
    }

    @Entonces("ve la denuncia de esa publicación con el motivo {string}")
    public void veDenuncia(String motivo) {
        String texto = new AdminPage(DriverManager.getDriver()).textoDenuncia(Contexto.obtener("tarjeta.nombre"));
        assertTrue(texto.contains(motivo), "Tarjeta de la denuncia: " + texto);
    }

    @Cuando("el administrador descarta las denuncias de esa publicación")
    public void descarta() {
        new AdminPage(DriverManager.getDriver()).descartarDenuncias(Contexto.obtener("tarjeta.nombre"));
    }

    @Entonces("la publicación sigue visible en el listado")
    public void sigueVisible() {
        DriverManager.getDriver().manage().deleteAllCookies();
        DriverManager.abrir("/Publicaciones");
        ListadoPage listado = new ListadoPage(DriverManager.getDriver()).esperarCarga();
        listado.buscar(Contexto.obtener("tarjeta.nombre"));
        assertTrue(listado.cantidadTarjetas() >= 1, "La publicación ya no aparece en el listado");
    }
}
