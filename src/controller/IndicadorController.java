package controller;

import model.*;
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
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

/**
 * Created by Colo on 31/5/2017.
 */
public class IndicadorController {

    public static void insertarIndicadorEnBd(String nombre, String ecuacion) {
        Indicador indicador = new Indicador(nombre, ecuacion.replaceAll("\\s",""));
        indicador.insertar();
    }

    public static int calcularIndicador(String nombreEmpresa, int anio, String ecuacion){
        int index = 0;
        String ecuacionNumerica = "";
        String[] parts = ecuacion.split("[-+*]");
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

    static String traerNumeroParaIndicador(String valor, String nombreEmpresa, int anio) {
        int cuenta = 0;
        if(isNumeric(valor)){
            return valor;
        }
        try {
            Statement stmt = DbManager.getConector().createStatement();
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
                Statement stmt = DbManager.getConector().createStatement();
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


