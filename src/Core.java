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

    public void cargarDatos(File archivo) {
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
            Year período = Year.of((int) r.getCell(1).getNumericCellValue());
            String nombreCuenta = r.getCell(2).getStringCellValue();
            float valorCuenta = (float) r.getCell(3).getNumericCellValue();

            Empresa e = new Empresa(nombreEmpresa);
            Período p = new Período(período);
            Cuenta c = new Cuenta(nombreCuenta, valorCuenta);

            empresas.add(e);
            períodos.add(p);
            p.agregarCuenta(e, c);
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
