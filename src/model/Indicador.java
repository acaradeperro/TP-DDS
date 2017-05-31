package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Indicador {
    private String nombre = null;
    private String ecuacion = null;

    public Indicador(String nombre, String ecuacion) {
        this.nombre = nombre;
        this.ecuacion = ecuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEcuacion() {
        return ecuacion;
    }

    public int insertar(){
        int id = -1;
        try {
            Statement stmt = DbManager.getConector().createStatement();
            stmt.executeUpdate("Insert into indicadores(nombre,ecuacion) values ('" + this.getNombre() + "','"+this.getEcuacion()+
                    "')",Statement.RETURN_GENERATED_KEYS);

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

    public static List<Indicador> fetchAllIndicadores(){
        Indicador indicador = null;
        List<Indicador> indicadores = new ArrayList<Indicador>();
        try {
            Statement stmt = DbManager.getConector().createStatement();
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

}

