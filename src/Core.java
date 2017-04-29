import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.HashSet;

class Core {
    public static void main(String[] args) {
        HashSet<Empresa> empresas = new HashSet<>();
        HashSet<Período> períodos = new HashSet<>();

        XSSFWorkbook wb;
        Sheet s = null;
        try {
            InputStream inp = new FileInputStream("workbook.xlsx");
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
}
