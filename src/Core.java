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
            if (validar(cuenta, idEmpresa, periodo)) {
                idCuenta = insertarCuenta(cuenta);
                insertarPeriodo(periodo, idCuenta, idEmpresa);
            }
        }
    }

    boolean validar(Cuenta cuenta, int idEmpresa, Periodo periodo){
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM periodos inner join cuentas on periodos.idCuenta = cuentas.id where anio = " + periodo.getAnio() +
                                        " and idEmpresa = " + idEmpresa + " and cuentas.nombre = '" + cuenta.getNombre() + "'");
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    List<Cuenta> obtenerCuentas(int anio, Empresa empresa) {
        for (Periodo p : periodos) {
            if (p.getAnio() == anio) {
                return p.getCuentasPorEmpresa(empresa);
            }
        }
        return null;
    }

    List<Cuenta> fetchAllCuentas(int anio, Empresa empresa){
        List<Cuenta> listaCuentas = new ArrayList<Cuenta>();
        Cuenta cuenta = null;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM cuentas inner join periodos on cuentas.id = periodos.idCuenta inner join empresas on periodos.idEmpresa = empresas.id " +
                                        "where empresas.nombre = '" + empresa.getNombre() + "' and periodos.anio = " + anio);
            while(rs.next()){
                cuenta = new Cuenta(rs.getString("nombre"),rs.getFloat("valor"));
                listaCuentas.add(cuenta);
            }
            return listaCuentas;
        } catch (SQLException e) {
            e.printStackTrace();
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

    List<Integer> fetchAllAnios(Empresa empresa){
        List<Integer> listaAnios = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM periodos inner join empresas on periodos.idEmpresa = empresas.id where nombre = '" + empresa.getNombre() + "'");
            while(rs.next()){
                listaAnios.add(rs.getInt("anio"));
            }
            return listaAnios;
    } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public List<Empresa> fetchAllEmpresas(){
        Empresa empresa = null;
        List<Empresa> empresas = new ArrayList<Empresa>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM empresas");
            while ( rs.next() ) {
                empresa = new Empresa(rs.getString("nombre"));
                empresas.add(empresa);
            }
            //con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return empresas;
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
            stmt.executeUpdate("Insert into Indicadores (nombre,ecuacion) values ('" + nombre + "','"+ecuacion+"')");
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    public int calcularIndicador(String nombreEmpresa, int anio, String ecuacion){
        int index = 0;
        String ecuacionNumerica = "";
        String[] parts = ecuacion.split("[-+*/]");
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
            return (int) engine.eval(ecuacionNumerica);
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
           return Integer.toString(calcularIndicador(nombreEmpresa, anio, indicador));
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
