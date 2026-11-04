package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Datos compartidos entre los pasos de un mismo escenario (por ejemplo, el nombre de la
 * mascota que se creó en un paso y se busca en otro). Se limpia antes de cada escenario.
 */
public final class Contexto {

    private static final ThreadLocal<Map<String, String>> DATOS = ThreadLocal.withInitial(HashMap::new);

    private Contexto() {
    }

    public static void guardar(String clave, String valor) {
        DATOS.get().put(clave, valor);
    }

    public static String obtener(String clave) {
        String valor = DATOS.get().get(clave);
        if (valor == null) throw new IllegalStateException("No hay un valor guardado para «" + clave + "».");
        return valor;
    }

    public static void limpiar() {
        DATOS.get().clear();
    }
}
