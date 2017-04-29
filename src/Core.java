import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.*;

class Core {
    HashSet<Empresa> empresas = new HashSet<>();
    HashSet<Período> períodos = new HashSet<>();

    public void main(String[] args) {
        cargarDatos("workbook.xlsx"); //indicar ruta de archivo
    }

    public void cargarDatos(String path) {
        XSSFWorkbook wb;
        Sheet s = null;
        try {
            InputStream inp = new FileInputStream(path);
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

    public List<Cuenta> obtenerDatos(Year anio, Empresa empresa) {
        for (Período dato : períodos) {
            if (dato.getAño() == anio) {
                return dato.getCuentasPorEmpresa(empresa);
            }
        }
        return null;
    }

    public List<Year> obtenerAnios(Empresa empresa) {
        List<Year> listaAnios = new ArrayList<Year>();
        for (Período dato : períodos) {
            if (dato.periodoPerteneceALaEmpresa(empresa)) {
                listaAnios.add(dato.getAño());
            }
        }
        return listaAnios;
    }

    public List<String> obtenerEmpresas() {
        List<String> listaEmpresas = new ArrayList<String>();
        for (Empresa dato : empresas) {
            listaEmpresas.add(dato.getNombre());
        }
        return listaEmpresas;
    }
}
