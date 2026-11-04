package utils;

import pages.LoginPage;
import pages.NavegacionPage;

/**
 * Cambia de usuario dentro de un mismo escenario (por ejemplo: la dueña publica, el administrador
 * aprueba y otra persona se postula). Borrar las cookies es más rápido y estable que cerrar sesión
 * desde el menú en cada cambio.
 */
public final class Sesion {

    private Sesion() {
    }

    public static void ingresarComo(String email, String password) {
        var driver = DriverManager.getDriver();
        driver.manage().deleteAllCookies();
        DriverManager.abrir("/Cuenta/Login");
        new LoginPage(driver).ingresar(email, password);
        if (!new NavegacionPage(driver).sesionIniciada()) {
            throw new AssertionError("No se pudo iniciar sesión como " + email + ". URL: " + driver.getCurrentUrl());
        }
    }

    public static void ingresarComoAdministrador() {
        ingresarComo(Config.adminEmail(), Config.adminPassword());
    }

    public static void ingresarComoGuardado(String alias) {
        ingresarComo(Contexto.obtener(alias + ".email"), Contexto.obtener(alias + ".password"));
    }
}
