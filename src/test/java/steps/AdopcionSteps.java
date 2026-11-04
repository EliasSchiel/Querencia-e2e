package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import pages.AdminPage;
import pages.FichaPage;
import pages.ListadoPage;
import pages.MisMascotasPage;
import pages.MisPublicacionesPage;
import pages.PublicarPage;
import pages.SolicitudPage;
import utils.Contexto;
import utils.DatosPrueba;
import utils.DriverManager;
import utils.Sesion;

import static org.junit.jupiter.api.Assertions.*;

/** Flujo completo: publicar, aprobar, postularse, elegir y registrar la entrega. */
public class AdopcionSteps {

    private org.openqa.selenium.WebDriver driver() {
        return DriverManager.getDriver();
    }

    private String mascota() {
        return Contexto.obtener("mascota");
    }

    @Dado("Ana cargó una mascota llamada {string}")
    public void anaCargoMascota(String nombre) {
        String unico = DatosPrueba.nombreMascota(nombre);
        DriverManager.abrir("/Mascotas");
        new MisMascotasPage(driver()).cargarMascota(unico, "Perro", "Mestizo", "Macho", "Mediano",
                DatosPrueba.fechaHaceAnios(2), "Mascota creada por la suite de pruebas automatizadas.");
        Contexto.guardar("mascota", unico);
    }

    @Cuando("Ana publica la mascota para adopción")
    public void anaPublica() {
        DriverManager.abrir("/Mascotas");
        new MisMascotasPage(driver()).publicar(mascota());
        PublicarPage publicar = new PublicarPage(driver());
        publicar.completarUbicacionSiFalta(RegistroSteps.PROVINCIA, RegistroSteps.LOCALIDAD);
        publicar.enviar();
    }

    @Entonces("la publicación queda {string}( de revisión)")
    public void publicacionQueda(String estado) {
        Sesion.ingresarComoGuardado("Ana");
        DriverManager.abrir("/Publicaciones/Mias");
        assertEquals(estado, new MisPublicacionesPage(driver()).estado(mascota()));
    }

    @Cuando("el administrador aprueba la publicación")
    public void adminAprueba() {
        Sesion.ingresarComoAdministrador();
        DriverManager.abrir("/Admin/Moderacion");
        new AdminPage(driver()).aprobarPublicacion(mascota());
    }

    @Entonces("la mascota aparece en el listado público")
    public void apareceEnListado() {
        driver().manage().deleteAllCookies();
        DriverManager.abrir("/Publicaciones");
        ListadoPage listado = new ListadoPage(driver()).esperarCarga();
        listado.buscar(mascota());
        assertEquals(1, listado.cantidadTarjetas(), "La mascota no aparece en el listado");
        listado.abrirPublicacion(mascota());
        Contexto.guardar("ficha.url", driver().getCurrentUrl());
    }

    @Cuando("{string} se registra y se postula para adoptarla")
    public void seRegistraYPostula(String nombre) {
        RegistroSteps.registrarPersona(nombre);
        driver().get(Contexto.obtener("ficha.url"));
        new FichaPage(driver()).postularse();
        SolicitudPage solicitud = new SolicitudPage(driver());
        solicitud.completar("Casa", true,
                "Hola, vivo en una casa con patio cerrado y trabajo desde casa. Me encantaría conocerlo.");
        solicitud.enviar();
    }

    @Cuando("Ana elige a Bruno entre las solicitudes")
    public void anaEligeABruno() {
        Sesion.ingresarComoGuardado("Ana");
        DriverManager.abrir("/Publicaciones/Mias");
        new MisPublicacionesPage(driver()).verSolicitudes(mascota());
        SolicitudPage solicitudes = new SolicitudPage(driver());
        assertEquals(1, solicitudes.solicitudesRecibidas());
        solicitudes.elegirA("Bruno");
    }

    @Cuando("Ana registra la entrega")
    public void anaRegistraEntrega() {
        DriverManager.abrir("/Publicaciones/Mias");
        new MisPublicacionesPage(driver()).registrarEntrega(mascota());
        new SolicitudPage(driver()).registrarAdopcion();
    }

    @Entonces("la adopción queda registrada")
    public void adopcionRegistrada() {
        String titulo = new SolicitudPage(driver()).tituloAdopcion();
        assertTrue(titulo.contains(mascota()) && titulo.contains("nueva casa"), "Título: " + titulo);
    }
}
