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


        try {
            conectarBd();
        }
        catch(Exception ex){System.out.println("Error: unable to load driver class!"); System.exit(1);}


    }

    void conectarBd() throws Exception {
            Class.forName(com.mysql.jdbc.Driver.class.getName());
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/bd?serverTimezone=UTC", "root", "1234");
    }


    void cargarDatos(File archivo) {
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
                idEmpresa = insertarEmpresa(empresa);
            }

            if(idEmpresa == -1){
                idEmpresa = traerIdEmpresa(nombreEmpresa);
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
            idCuenta = insertarCuenta(cuenta);
            insertarPeriodo(periodo,idCuenta,idEmpresa);
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

    int insertarEmpresa(Empresa empresa){
        int id = -1;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("Insert into Empresas (nombre) values ('" + empresa.getNombre() + "')",Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                id=rs.getInt(1);
            }
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return id;
    }

    void insertarPeriodo(Periodo periodo, int idCuenta, int idEmpresa){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("Insert into Periodos (anio,idEmpresa,idCuenta) values ('" + periodo.getAnio() + "',"+idEmpresa+","+idCuenta+")");
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    int insertarCuenta(Cuenta cuenta){
        int id = -1;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("Insert into Cuentas (nombre,valor) values ('" + cuenta.getNombre() + "','"+cuenta.getValor()+"')",Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                id=rs.getInt(1);
            }
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return id;
    }

    int traerIdEmpresa(String nombre){
        int idEmpresa = -1;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id from empresas where nombre = ('" + nombre + "')");
            while (rs.next()) {
                idEmpresa = rs.getInt("id");
            }
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

        return idEmpresa;

    }

}
