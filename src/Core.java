import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class Core {
    private HashSet<Empresa> empresas = new HashSet<>();
    private HashSet<Período> períodos = new HashSet<>();

    public static void main(String[] args) {

    }

    void cargarDatos(File archivo) {
        XSSFWorkbook wb;
        Sheet s = null;
        try {
            InputStream inp = new FileInputStream(archivo);
            wb = new XSSFWorkbook(inp);
            s = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert s != null;
        for (Row r : s) {
            String nombreEmpresa = r.getCell(0).getStringCellValue();
            if (nombreEmpresa.isEmpty()) break;
            Year año = Year.of((int) r.getCell(1).getNumericCellValue());
            String nombreCuenta = r.getCell(2).getStringCellValue();
            float valorCuenta = (float) r.getCell(3).getNumericCellValue();

            Empresa ne = null;
            for (Empresa e : empresas) {
                if (e.getNombre().equals(nombreEmpresa)) {
                    ne = e;
                    break;
                }
            }
            if (ne == null) {
                ne = new Empresa(nombreEmpresa);
                empresas.add(ne);
            }

            Período np = null;
            for (Período p : períodos) {
                if (p.getAño().equals(año)) {
                    np = p;
                    break;
                }
            }
            if (np == null) {
                np = new Período(año);
                períodos.add(np);
            }

            Cuenta c = new Cuenta(nombreCuenta, valorCuenta);
            np.agregarCuenta(ne, c);
        }
    }

    List<Cuenta> obtenerCuentas(Year anio, Empresa empresa) {
        for (Período p : períodos) {
            if (p.getAño().equals(anio)) {
                return p.getCuentasPorEmpresa(empresa);
            }
        }
        return null;
    }

    List<Year> obtenerAnios(Empresa empresa) {
        List<Year> listaAnios = new ArrayList<>();
        for (Período p : períodos) {
            if (p.periodoPerteneceALaEmpresa(empresa)) {
                listaAnios.add(p.getAño());
            }
        }
        return listaAnios;
    }

    public List<Empresa> obtenerEmpresas() {
        List<Empresa> listaEmpresas = new ArrayList<>();
        listaEmpresas.addAll(empresas);
        return listaEmpresas;
    }
}
