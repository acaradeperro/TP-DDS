import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Período {
    private Year año = null;
    private HashMap<Empresa, ArrayList<Cuenta>> cuentasPorEmpresa = new HashMap<>();

    Período(Year año) {
        this.año = año;
    }

    public boolean equals(Período p) {
        return Objects.equals(p.getAño(), this.año);
    }

    Year getAño() {
        return año;
    }

    List<Cuenta> getCuentasPorEmpresa(Empresa e) {
        return cuentasPorEmpresa.containsKey(e) ? cuentasPorEmpresa.get(e) : cuentasPorEmpresa.put(e, new ArrayList<>());
    }

    void agregarCuenta(Empresa e, Cuenta c) {
        getCuentasPorEmpresa(e).add(c);
    }
}
