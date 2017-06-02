package controller;

import exception.IndicadorException;
import model.*;
import com.fathzer.soft.javaluator.DoubleEvaluator;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import java.lang.System;
/**
 * Created by Colo on 31/5/2017.
 */
public class IndicadorController {

    public static void insertarIndicadorEnBd(String nombre, String ecuacion) {
        Indicador indicador = new Indicador(nombre, ecuacion.replaceAll("\\s",""));
        indicador.insertar();
    }

    public static Double calcularIndicador(String nombreEmpresa, int anio, String ecuacion){
        int index = 0;
        String ecuacionNumerica = "";
        String[] parts = ecuacion.split("[-+/*]");
        for(int i = 0; i < parts.length; i++){
            try {
                ecuacionNumerica += traerNumeroParaIndicador(parts[i], nombreEmpresa, anio);
            }
            catch (IndicadorException e){
                System.out.println(e.getMessage());
            }
            index += parts[i].length();
            if( index < ecuacion.length()) {
                ecuacionNumerica += ecuacion.charAt(index);
                index++;
            }
        }
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {

            return (Double) new DoubleEvaluator().evaluate(ecuacionNumerica);
        }
        catch (Exception e ){

        }
        return 0.0;
    }

    static String traerNumeroParaIndicador(String valor, String nombreEmpresa, int anio) throws IndicadorException{
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
                    return Double.toString(calcularIndicador(nombreEmpresa, anio, indicador));
                }

            }
            catch (Exception e)
            {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
            throw new IndicadorException("La cuenta o indicador " + valor + " no existe");


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


