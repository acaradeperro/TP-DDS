package controller;

import model.DbManager;
import model.Empresa;

import java.util.List;

public class EmpresaController {

    public static Empresa obtenerEmpresa(String nombre) {
        List<Empresa> empresas = Empresa.fetchAllEmpresas();
        Empresa empresaRetorno = new Empresa(null);
        for (int i = 0; i < empresas.size(); i++) {
            if (empresas.get(i).getNombre().equals(nombre)) {

                empresaRetorno = empresas.get(i);
            }
        }
        return empresaRetorno;
    }

}
