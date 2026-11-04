package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import pages.FichaPage;
import pages.ListadoPage;
import utils.Contexto;
import utils.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class BusquedaSteps {

    private ListadoPage listado() {
        return new ListadoPage(DriverManager.getDriver());
    }

    @Dado("que abro el listado de mascotas")
    public void abrirListado() {
        DriverManager.abrir("/Publicaciones");
        listado().esperarCarga();
    }

    @Entonces("veo al menos {int} publicación en el listado")
    public void veoAlMenos(int minimo) {
        int cantidad = listado().cantidadTarjetas();
        assertTrue(cantidad >= minimo, "Hay " + cantidad + " publicaciones. Resumen: " + listado().resumen());
    }

    @Cuando("filtro por el tipo {string}")
    public void filtroPorTipo(String tipo) {
        listado().filtrarPorTipo(tipo);
    }

    @Entonces("todas las publicaciones del listado son del tipo {string}")
    public void todasDelTipo(String tipo) {
        var tipos = listado().tiposDeLasTarjetas();
        if (tipos.isEmpty()) {
            assertTrue(listado().avisoSinResultadosVisible(), "Sin tarjetas y sin el aviso de «no hay resultados»");
        }
        tipos.forEach(t -> assertEquals(tipo, t, "Apareció una publicación de otro tipo"));
    }

    @Cuando("busco {string}")
    public void busco(String texto) {
        listado().buscar(texto);
    }

    @Entonces("veo el aviso de que no hay resultados")
    public void avisoSinResultados() {
        assertTrue(listado().avisoSinResultadosVisible());
        assertEquals(0, listado().cantidadTarjetas());
    }

    @Cuando("abro la primera publicación del listado")
    public void abroLaPrimera() {
        Contexto.guardar("tarjeta.nombre", listado().abrirPrimera());
    }

    @Dado("abro la primera publicación de adopción del listado")
    public void abroLaPrimeraDeAdopcion() {
        abrirListado();
        listado().filtrarPorTipo("Adopcion");
        String nombre = listado().abrirPrimeraDeAdopcion();
        Contexto.guardar("tarjeta.nombre", nombre);
        Contexto.guardar("ficha.url", DriverManager.getDriver().getCurrentUrl());
    }

    @Entonces("la ficha muestra el mismo nombre que la tarjeta")
    public void mismoNombre() {
        assertEquals(Contexto.obtener("tarjeta.nombre"), new FichaPage(DriverManager.getDriver()).nombre());
    }

    @Entonces("la ficha muestra la ubicación y los datos de la mascota")
    public void ubicacionYDatos() {
        FichaPage ficha = new FichaPage(DriverManager.getDriver());
        assertFalse(ficha.ubicacion().isBlank(), "La ficha no muestra la ubicación");
        assertTrue(ficha.datosVisibles(), "La ficha no muestra los datos");
    }
}
