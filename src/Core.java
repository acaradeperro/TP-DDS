import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.HashSet;

public class Core {
    public static void main(String[] args) {
        HashSet<Empresa> empresas = new HashSet<>();
        HashSet<Período> períodos = new HashSet<>();

        HSSFWorkbook wb;
        Sheet s = null;
        try {
            InputStream inp = new FileInputStream("workbook.xls");
            wb = new HSSFWorkbook(new POIFSFileSystem(inp));
            s = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
