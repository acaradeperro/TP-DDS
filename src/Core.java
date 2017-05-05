import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


class Core {
    private HashSet<Empresa> empresas = new HashSet<>();
    private HashSet<Periodo> periodos = new HashSet<>();
    Connection con;

    public void main(String[] args) {

    }

    void conectarBd() throws Exception {
       //     Class.forName("com.mysql.jdbc.Driver");
        //    new com.mysql.jdbc.Driver();
            Class.forName(com.mysql.jdbc.Driver.class.getName());
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/bd?serverTimezone=UTC", "root", "1234");
    }

    void insertPrueba() throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("Insert into Empresas (nombre) values ('colo srl')");
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
            int anio = (int) r.getCell(1).getNumericCellValue();
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

            Periodo np = null;
            for (Periodo p : periodos) {
                if (p.getAnio() == anio) {
                    np = p;
                    break;
                }
            }
            if (np == null) {
                np = new Periodo(anio);
                periodos.add(np);
            }

            Cuenta c = new Cuenta(nombreCuenta, valorCuenta);
            np.agregarCuenta(ne, c);
        }
    }

    List<Cuenta> obtenerCuentas(int anio, Empresa empresa) {
        for (Periodo p : periodos) {
            if (p.getAnio() == anio) {
                return p.getCuentasPorEmpresa(empresa);
            }
        }
        return null;
    }

    List<Integer> obtenerAnios(Empresa empresa) {
        List<Integer> listaAnios = new ArrayList<>();
        for (Periodo p : periodos) {
            if (p.periodoPerteneceALaEmpresa(empresa)) {
                listaAnios.add(p.getAnio());
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
