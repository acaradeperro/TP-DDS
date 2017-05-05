import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Periodo {
    private int anio = 0;
    private HashMap<Empresa, ArrayList<Cuenta>> cuentasPorEmpresa = new HashMap<>();

    Periodo(int año) {
        this.anio = año;
    }

   /* @Override
    /*public int hashCode() {
        return anio.hashCode();
    }*/

    @Override
    public boolean equals(Object o) {
        return o instanceof Periodo && anio == ((Periodo) o).getAnio();
    }

    int getAnio() {
        return anio;
    }

    boolean periodoPerteneceALaEmpresa (Empresa e){
        return cuentasPorEmpresa.containsKey(e);
    }

    List<Cuenta> getCuentasPorEmpresa(Empresa e) {
        if (!cuentasPorEmpresa.containsKey(e)) cuentasPorEmpresa.put(e, new ArrayList<>());
        return cuentasPorEmpresa.get(e);
    }

    void agregarCuenta(Empresa e, Cuenta c) {
        getCuentasPorEmpresa(e).add(c);
    }
}
