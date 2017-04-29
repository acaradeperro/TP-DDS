class Cuenta {
    private String nombre = null;
    private float valor = 0;

    Cuenta(String nombre, float valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    String getNombre() {
        return nombre;
    }

    float getValor() {
        return valor;
    }
}