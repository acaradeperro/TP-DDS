import java.sql.ResultSet;
import java.sql.Statement;

class Indicador {
    private String nombre = null;
    private String ecuacion = null;

    Indicador(String nombre, String ecuacion) {
        this.nombre = nombre;
        this.ecuacion = ecuacion;
    }

    String getNombre() {
        return nombre;
    }

    String getEcuacion() {
        return ecuacion;
    }

}
