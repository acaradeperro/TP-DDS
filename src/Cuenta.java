public class Cuenta {
    private String nombre = null;
    private float valor = 0;

    Cuenta(String nombre, float valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public float getValor() {
        return valor;
    }
}