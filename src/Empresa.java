import java.util.Objects;

public class Empresa {
    private String nombre = null;

    Empresa(String nombre) {
        this.nombre = nombre;
    }

    public boolean equals(Empresa e) {
        return Objects.equals(e.getNombre(), this.nombre);
    }

    String getNombre() {
        return nombre;
    }
}
