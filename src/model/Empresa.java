package model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.DbManager;

public class Empresa {
    private String nombre = null;

    public Empresa(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Empresa && nombre.equals(((Empresa) o).getNombre());
    }

    public String getNombre() {
        return nombre;
    }

    public int insertar(){
        int id = -1;
        try {
            Statement stmt = DbManager.getConector().createStatement();
            stmt.executeUpdate("Insert into Empresas (nombre) values ('" + this.getNombre() + "')",Statement.RETURN_GENERATED_KEYS);
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

    public int getId(){
        int idEmpresa = -1;
        try {
            Statement stmt = DbManager.getConector().createStatement();
            ResultSet rs = stmt.executeQuery("select id from empresas where nombre = ('" + this.nombre + "')");
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

    public static List<Empresa> fetchAllEmpresas(){
        Empresa empresa = null;
        List<Empresa> empresas = new ArrayList<Empresa>();
        try {
            Statement stmt = DbManager.getConector().createStatement();
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
}
