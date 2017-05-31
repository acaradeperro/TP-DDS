package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
    private String nombre = null;
    private float valor = 0;

    public Cuenta(String nombre, float valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public float getValor() {
        return valor;
    }

    public int insertar(){
        int id = -1;
        try {
            Statement stmt = DbManager.getConector().createStatement();
            stmt.executeUpdate("Insert into Cuentas (nombre,valor) values ('" + this.getNombre() + "','"+this.getValor()+"')",Statement.RETURN_GENERATED_KEYS);

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

    public static List<Cuenta> fetchAllCuentas(int anio, Empresa empresa){
        List<Cuenta> listaCuentas = new ArrayList<Cuenta>();
        Cuenta cuenta = null;
        try {
            Statement stmt = DbManager.getConector().createStatement();
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
}