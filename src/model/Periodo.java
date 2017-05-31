package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Periodo {
    private int anio = 0;
    private HashMap<Empresa, ArrayList<Cuenta>> cuentasPorEmpresa = new HashMap<>();

    public Periodo(int año) {
        this.anio = año;
    }

   /* @Override
    /*public int hashCode() {
        return anio.hashCode();
    }*/

    @Override
    public boolean equals(Object o) {
        return o instanceof Periodo && anio == ((Periodo) o).getAnio();
    }

    public int getAnio() {
        return anio;
    }

    public boolean periodoPerteneceALaEmpresa (Empresa e){
        return cuentasPorEmpresa.containsKey(e);
    }

    List<Cuenta> getCuentasPorEmpresa(Empresa e) {
        if (!cuentasPorEmpresa.containsKey(e)) cuentasPorEmpresa.put(e, new ArrayList<>());
        return cuentasPorEmpresa.get(e);
    }

    public void agregarCuenta(Empresa e, Cuenta c) {
        getCuentasPorEmpresa(e).add(c);
    }

    public void insertar(int idCuenta, int idEmpresa){
        try {
            Statement stmt = DbManager.getConector().createStatement();
            stmt.executeUpdate("Insert into Periodos (anio,idEmpresa,idCuenta) values ('" + this.getAnio() + "',"+idEmpresa+","+idCuenta+")");
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    public static List<Integer> fetchAllAnios(Empresa empresa){
        List<Integer> listaAnios = new ArrayList<>();
        try {
            Statement stmt = DbManager.getConector().createStatement();
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
}
