package controller;

import model.DbManager;
import model.Periodo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;

import model.Empresa;
import model.Cuenta;

/**
 * Created by Colo on 31/5/2017.
 */
public class CuentaController {

    private static HashSet<Empresa> empresas = new HashSet<>();
    private static HashSet<Periodo> periodos = new HashSet<>();

    public static void cargarDatos(File archivo) {
        XSSFWorkbook wb;
        int idEmpresa = -1;
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
            int idCuenta;

            Empresa empresa = null;
            for (Empresa e : empresas) {
                if (e.getNombre().equals(nombreEmpresa)) {
                    empresa = e;
                    break;
                }
            }
            if (empresa == null) {
                empresa = new Empresa(nombreEmpresa);
                empresas.add(empresa);
                idEmpresa = empresa.insertar();
            }

            if (idEmpresa == -1) {
                idEmpresa = empresa.getId();
            }

            Periodo periodo = null;
            for (Periodo p : periodos) {
                if (p.getAnio() == anio) {
                    periodo = p;
                    break;
                }
            }
            if (periodo == null) {
                periodo = new Periodo(anio);
                periodos.add(periodo);
            }

            Cuenta cuenta = new Cuenta(nombreCuenta, valorCuenta);
            periodo.agregarCuenta(empresa, cuenta);
            if (validar(cuenta, idEmpresa, periodo)) {
                idCuenta = cuenta.insertar();
                periodo.insertar(idCuenta, idEmpresa);
            }
        }
    }
    static boolean validar(Cuenta cuenta, int idEmpresa, Periodo periodo){
        try {
            Statement stmt = DbManager.getConector().createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM periodos inner join cuentas on periodos.idCuenta = cuentas.id where anio = " + periodo.getAnio() +
                    " and idEmpresa = " + idEmpresa + " and cuentas.nombre = '" + cuenta.getNombre() + "'");
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Cuenta obtenerCuenta(List<Cuenta> cuentas, String nombre) {
        Cuenta cuentaRetorno = new Cuenta(null, 0);
        for (int i = 0; i < cuentas.size(); i++) {
            if (cuentas.get(i).getNombre().equals(nombre)) {

                cuentaRetorno = cuentas.get(i);
            }
        }
        return cuentaRetorno;
    }
}
