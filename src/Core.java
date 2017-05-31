/*
import model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
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
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/bd?serverTimezone=UTC", "root", "root");
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
            if (validar(cuenta, idEmpresa, periodo)) {
                idCuenta = insertarCuenta(cuenta);
                insertarPeriodo(periodo, idCuenta, idEmpresa);
            }
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


    public List<Indicador> fetchAllIndicadores(){
        Indicador indicador = null;
        List<Indicador> indicadores = new ArrayList<Indicador>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM indicadores");
            while ( rs.next() ) {
                indicador = new Indicador(rs.getString("nombre"),rs.getString("ecuacion"));
                indicadores.add(indicador);
            }
            //con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return indicadores;
    }
    public void insertarIndicador(String nombre, String ecuacion){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("Insert into Indicadores (nombre,ecuacion) values ('" + nombre + "','"+
                    ecuacion.replaceAll("\\s","")+"')");
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    public float calcularIndicador(String nombreEmpresa, int anio, String ecuacion){
        int index = 0;
        String ecuacionNumerica = "";
        String[] parts = ecuacion.split("[-+*//*
]");
        for(int i = 0; i < parts.length; i++){
            ecuacionNumerica += traerNumeroParaIndicador(parts[i],nombreEmpresa, anio);
            index += parts[i].length();
            if( index < ecuacion.length()) {
                ecuacionNumerica += ecuacion.charAt(index);
                index++;
            }
        }
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            return (float) engine.eval(ecuacionNumerica);
        }
        catch (Exception e ){

        }
        return 0;
    }

    String traerNumeroParaIndicador(String valor, String nombreEmpresa, int anio) {
        int cuenta = 0;
        if(isNumeric(valor)){
            return valor;
        }
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM periodos inner join empresas on periodos.idEmpresa = empresas.id inner join cuentas on periodos.idCuenta = cuentas.id" +
                    " where periodos.anio = "+ anio + " and empresas.nombre = '" + nombreEmpresa + "' and cuentas.nombre = '" +valor +"'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                cuenta = rs.getInt("cuentas.valor");
            }
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

        if(cuenta != 0){
            return Integer.toString(cuenta);
        }

        else {
            String indicador = "";
            try {
                Statement stmt = con.createStatement();
                String query = "select * from indicadores where nombre = '" + valor +"'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    indicador = rs.getString("indicadores.ecuacion");
                }
            }
            catch (Exception e)
            {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
           return Float.toString(calcularIndicador(nombreEmpresa, anio, indicador));
        }

    }
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
*/
