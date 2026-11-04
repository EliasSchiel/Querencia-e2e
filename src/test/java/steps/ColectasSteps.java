package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import org.openqa.selenium.WebDriver;
import pages.AdminPage;
import pages.FichaPage;
import pages.MisMascotasPage;
import pages.MisPublicacionesPage;
import pages.NavegacionPage;
import pages.PublicarPage;
import pages.RegistroAsociacionPage;
import pages.RendirPage;
import utils.Contexto;
import utils.DatosPrueba;
import utils.DriverManager;
import utils.Sesion;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regla de negocio: una asociación puede tener como máximo 3 colectas (pedidos de ayuda) sin
 * comprobante aprobado. Cada escenario crea su propia asociación para poder repetirse.
 */
public class ColectasSteps {

    private static final String DETALLE = "Necesita una cirugía de cadera y una semana de internación con controles.";

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @Dado("una asociación nueva verificada por el administrador")
    public void asociacionNuevaVerificada() {
        String nombre = "E2E Refugio " + DatosPrueba.sufijo();
        String email = DatosPrueba.email("asociacion");

        driver().manage().deleteAllCookies();
        DriverManager.abrir("/Cuenta/RegistroAsociacion");
        RegistroAsociacionPage registro = new RegistroAsociacionPage(driver());
        registro.completar(nombre, DatosPrueba.cuitValido(),
                "Asociación creada por la suite de pruebas automatizadas de Querencia.",
                RegistroSteps.PROVINCIA, RegistroSteps.LOCALIDAD, DatosPrueba.archivo("documentacion.pdf"),
                "Responsable E2E", "2954123456", email, RegistroSteps.CLAVE);
        registro.enviar();
        assertTrue(new NavegacionPage(driver()).sesionIniciada(), () -> "No se registró la asociación: " + registro.erroresFormulario());

        Sesion.ingresarComoAdministrador();
        DriverManager.abrir("/Admin/Asociaciones");
        new AdminPage(driver()).verificarAsociacion(nombre);

        Contexto.guardar("asociacion.email", email);
        Contexto.guardar("asociacion.password", RegistroSteps.CLAVE);
        Sesion.ingresarComoGuardado("asociacion");
    }

    /** Carga una mascota y la publica como pedido de ayuda. Devuelve true si se publicó. */
    private boolean publicarColecta(String nombreMascota, boolean cargarMascota) {
        MisMascotasPage mascotas = new MisMascotasPage(driver());
        DriverManager.abrir("/Mascotas");
        if (cargarMascota) {
            mascotas.cargarMascota(nombreMascota, "Gato", "Mestizo", "Hembra", "Pequeño",
                    DatosPrueba.fechaHaceAnios(1), "Mascota de una colecta de prueba.");
        }
        mascotas.publicar(nombreMascota);
        PublicarPage publicar = new PublicarPage(driver());
        publicar.elegirTipo("PedidoAyuda");
        Contexto.guardar("nota.colectas", publicar.notaColectas());
        publicar.completarUbicacionSiFalta(RegistroSteps.PROVINCIA, RegistroSteps.LOCALIDAD);
        publicar.completarColecta(DETALLE, 50000, DatosPrueba.fechaEnDias(20));
        publicar.enviar();
        return !publicar.sigueEnElFormulario();
    }

    @Dado("que la asociación abrió {int} colectas")
    public void abrioColectas(int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            String nombre = DatosPrueba.nombreMascota("Colecta" + i);
            assertTrue(publicarColecta(nombre, true), "No se pudo publicar la colecta " + i);
            Contexto.guardar("colecta" + i, nombre);
        }
    }

    @Cuando("intenta abrir una cuarta colecta")
    public void intentaCuarta() {
        String nombre = DatosPrueba.nombreMascota("Colecta4");
        Contexto.guardar("colecta4", nombre);
        assertFalse(publicarColecta(nombre, true), "La cuarta colecta no debería publicarse");
    }

    @Entonces("ve el aviso {string}")
    public void veAviso(String texto) {
        String errores = new PublicarPage(driver()).erroresFormulario();
        assertTrue(errores.contains(texto), "Errores: " + errores);
    }

    @Cuando("la asociación informa que recaudó {int} en la primera colecta y la cierra")
    public void recaudoYCierra(int monto) {
        DriverManager.abrir("/Publicaciones/Mias");
        new MisPublicacionesPage(driver()).abrirFicha(Contexto.obtener("colecta1"));
        Contexto.guardar("colecta1.url", driver().getCurrentUrl());
        FichaPage ficha = new FichaPage(driver());
        ficha.actualizarRecaudado(monto);
        ficha.cerrarColecta();
    }

    @Cuando("sube el comprobante de la primera colecta")
    public void subeComprobante() {
        DriverManager.abrir("/Publicaciones/Mias");
        new MisPublicacionesPage(driver()).subirComprobante(Contexto.obtener("colecta1"));
        RendirPage rendir = new RendirPage(driver());
        rendir.completar("Cirugía de cadera en la Veterinaria San Roque, con internación y controles.",
                39500, DatosPrueba.archivo("factura.pdf"));
        rendir.enviar();
    }

    @Entonces("la primera colecta queda con el comprobante en revisión")
    public void comprobanteEnRevision() {
        DriverManager.abrir("/Publicaciones/Mias");
        String fila = new MisPublicacionesPage(driver()).textoFila(Contexto.obtener("colecta1"));
        assertTrue(fila.contains("Comprobante en revisión"), "Fila: " + fila);
    }

    @Entonces("todavía no puede abrir una cuarta colecta")
    public void todaviaNoPuede() {
        assertFalse(publicarColecta(Contexto.obtener("colecta4"), false), "Con el comprobante en revisión no debería poder");
    }

    @Cuando("el administrador aprueba el comprobante de la primera colecta")
    public void adminApruebaComprobante() {
        Sesion.ingresarComoAdministrador();
        DriverManager.abrir("/Admin/Colectas");
        new AdminPage(driver()).aprobarComprobante(Contexto.obtener("colecta1"));
    }

    @Entonces("la ficha de la primera colecta muestra la rendición verificada")
    public void fichaVerificada() {
        driver().manage().deleteAllCookies();
        driver().get(Contexto.obtener("colecta1.url"));
        assertTrue(new FichaPage(driver()).rendicionVerificadaVisible(), "No se ve «Rendición verificada»");
    }

    @Entonces("la asociación ya puede abrir otra colecta")
    public void yaPuede() {
        Sesion.ingresarComoGuardado("asociacion");
        assertTrue(publicarColecta(Contexto.obtener("colecta4"), false), "Con el comprobante aprobado debería poder publicar");
        assertEquals("Publicada", new MisPublicacionesPage(driver()).estado(Contexto.obtener("colecta4")));
    }
}
