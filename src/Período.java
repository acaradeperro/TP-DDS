import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Período {
    private Year año = null;
    private HashMap<Empresa, ArrayList<Cuenta>> cuentasPorEmpresa = new HashMap<>();

    Período(Year año) {
        this.año = año;
    }

    @Override
    public int hashCode() {
        return año.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Período && año.equals(((Período) o).getAño());
    }

    Year getAño() {
        return año;
    }

    List<Cuenta> getCuentasPorEmpresa(Empresa e) {
        if (!cuentasPorEmpresa.containsKey(e)) cuentasPorEmpresa.put(e, new ArrayList<>());
        return cuentasPorEmpresa.get(e);
    }

    void agregarCuenta(Empresa e, Cuenta c) {
        getCuentasPorEmpresa(e).add(c);
    }
}
