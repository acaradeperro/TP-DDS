class Empresa {
    private String nombre = null;

    Empresa(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Empresa && nombre.equals(((Empresa) o).getNombre());
    }

    String getNombre() {
        return nombre;
    }
}
