import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import java.time.Year;
import java.util.stream.Collectors;


public class VentanaConsulta extends Application {
    Stage window;
    Scene scene;

    ComboBox<String> cbEmpresas= new ComboBox<>();;
    ComboBox<String> cbCuentas= new ComboBox<>();
    ComboBox<String> cbPeriodo= new ComboBox<>();


    Label lEmpresa = new Label();
    Label lCuentaEmpresa = new Label();
    Label lNombreEmpresa = new Label();
    Label lValor = new Label();

    List<Empresa> listaEmpresas = new ArrayList<>();
    List<String> listaAnios = new ArrayList<>();
    List<Cuenta> listaCuentas = new ArrayList<>();

    Core core;

    public static void main(String[] args){
        launch(args);
    }

    final Button boton = new Button ("Consultar");
    final Button volver = new Button("Volver");
    @Override
    public void start(Stage primaryStage) throws Exception{

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));

        core = new Core();

        listaEmpresas = core.obtenerEmpresas2();

        window = primaryStage;
        window.setTitle("Consulta");
        scene = new Scene(new Group(), 200, 225);


        cbEmpresas.setPromptText("Empresas");

        cbPeriodo = new ComboBox<>();
        cbPeriodo.setPromptText("Periodos");

        cbCuentas.setPromptText("Cuentas");


        cbEmpresas.getItems().addAll(
                listaEmpresas.stream().map( n ->n.getNombre()).collect(Collectors.toList())
        );

        cbEmpresas.setOnAction(e->{
            listaAnios = core.obtenerAnios(obtenerEmpresa(listaEmpresas, cbEmpresas.getValue())).stream().map( n ->n.toString()).collect(Collectors.toList());
            cbPeriodo.getItems().remove(0,cbPeriodo.getItems().size());
            cbCuentas.getItems().remove(0,cbCuentas.getItems().size());

            cbPeriodo.getItems().addAll(
                    listaAnios);

        });


        cbPeriodo.setOnAction(e-> {
                    listaCuentas = core.obtenerDatos(stringToYear(cbPeriodo.getValue()), obtenerEmpresa(listaEmpresas, cbEmpresas.getValue()));
                    cbCuentas.getItems().addAll(
                            listaCuentas.stream().map(n -> n.getNombre()).collect(Collectors.toList()));
                }
        );


        grid.add(new Label("Empresa: "), 0, 0);
        grid.add(cbEmpresas, 1, 0);

        grid.add(new Label("Periodo: "), 0, 1);
        grid.add(cbPeriodo, 1, 1);

        grid.add(new Label("Cuenta: "), 0, 2);
        grid.add(cbCuentas, 1, 2);


        grid.add(boton, 0, 4);
        grid.add(volver,1,4);

        grid.add(new Label(""), 0,5);
        grid.add(new Label(""), 1,5);


        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);
        primaryStage.setScene(scene);
        primaryStage.show();

        grid.add(lEmpresa, 0, 6);
        grid.add(lNombreEmpresa, 1, 6);
        grid.add(lCuentaEmpresa, 0, 7);
        grid.add(lValor, 1, 7);

        boton.setOnAction(e->{

            lEmpresa.setText("Empresa: ");
            lNombreEmpresa.setText(cbEmpresas.getValue());
            lCuentaEmpresa.setText(cbCuentas.getValue()+":");
            lValor.setText(String.valueOf(obtenerCuenta(listaCuentas, cbCuentas.getValue()).getValor()));

        });

        volver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                VentanaMenuPrincipal  vmp= new VentanaMenuPrincipal();
                vmp.start(stage);
                stage.show();
                primaryStage.close();
            }

        });

    }

    Cuenta obtenerCuenta(List<Cuenta> cuentas, String nombre){
        Cuenta cuentaRetorno = new Cuenta(null, 0);
        for(int i = 0;i<cuentas.size();i++){
            if(cuentas.get(i).getNombre().equals(nombre)){

                cuentaRetorno = cuentas.get(i);
            }
        }
        return cuentaRetorno;
    }

    Year stringToYear(String anio){
        Year i;
        i = Year.of(Integer.parseInt(anio));
        return i;
    }

    Empresa obtenerEmpresa(List<Empresa> empresas, String nombre){
        Empresa empresaRetorno = new Empresa(null);
        for(int i = 0;i<empresas.size();i++){
            if(empresas.get(i).getNombre().equals(nombre)){

                empresaRetorno = empresas.get(i);
            }
        }
        return empresaRetorno;
    }
}
