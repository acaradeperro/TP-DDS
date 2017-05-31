import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class VentanaConsultarIndicadores extends Application {

    private Stage ventanaMenu;
    private Core elCore;
    Stage window;
    Scene scene;

    ComboBox<String> cbEmpresas = new ComboBox<>();

    ComboBox<String> cbIndicadores = new ComboBox<>();
    ComboBox<String> cbPeriodo = new ComboBox<>();


    Label lEmpresa = new Label();
    Label lCuentaEmpresa = new Label();
    Label lNombreEmpresa = new Label();
    Label lValor = new Label();

    List<Empresa> listaEmpresas = new ArrayList<>();
    List<String> listaAnios = new ArrayList<>();
    List<Indicador> listaIndicadores = new ArrayList<>();

    VentanaConsultarIndicadores(Stage ventanaRecibida, Core miCore) {
        this.ventanaMenu = ventanaRecibida;
        this.elCore = miCore;
    }

    public static void main(String[] args) {
        launch(args);
    }

    final Button boton = new Button("Consultar");
    final Button volver = new Button("Volver");

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        cbPeriodo.setDisable(true);
        boton.setDisable(true);
        listaIndicadores = elCore.fetchAllIndicadores();
        listaEmpresas = elCore.fetchAllEmpresas();

        window = primaryStage;
        window.setTitle("Consulta");
        scene = new Scene(new Group(), 200, 225);


        cbEmpresas.setPromptText("Empresas");

        cbPeriodo = new ComboBox<>();
        cbPeriodo.setPromptText("Periodos");

        cbIndicadores.setPromptText("Indicadores");


        cbEmpresas.getItems().addAll(
                listaEmpresas.stream().map(n -> n.getNombre()).collect(Collectors.toList())
        );

        cbIndicadores.getItems().addAll(
                listaIndicadores.stream().map(n -> n.getNombre()).collect(Collectors.toList())
        );

        cbEmpresas.setOnAction(e -> {
            boton.setDisable(true);
            listaAnios = elCore.fetchAllAnios(obtenerEmpresa(cbEmpresas.getValue())).stream().map(n -> n.toString()).collect(Collectors.toList());
            cbPeriodo.getItems().remove(0, cbPeriodo.getItems().size());
            cbPeriodo.setDisable(false);

            cbPeriodo.getItems().addAll(
                    listaAnios);

        });

        cbIndicadores.setOnAction(e -> {
            boton.setDisable(false);
        });



        grid.add(new Label("Empresa: "), 0, 0);
        grid.add(cbEmpresas, 1, 0);

        grid.add(new Label("Periodo: "), 0, 1);
        grid.add(cbPeriodo, 1, 1);

        grid.add(new Label("Cuenta: "), 0, 2);
        grid.add(cbIndicadores, 1, 2);


        grid.add(boton, 0, 4);
        grid.add(volver, 1, 4);

        grid.add(new Label(""), 0, 5);
        grid.add(new Label(""), 1, 5);


        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        primaryStage.setScene(scene);
        primaryStage.show();

        grid.add(lEmpresa, 0, 6);
        grid.add(lNombreEmpresa, 1, 6);
        grid.add(lCuentaEmpresa, 0, 7);
        grid.add(lValor, 1, 7);

        boton.setOnAction(e -> {

            lEmpresa.setText("Empresa: ");
            lNombreEmpresa.setText(cbEmpresas.getValue());
            lCuentaEmpresa.setText(cbIndicadores.getValue() + ":");
            lValor.setText(String.valueOf(elCore.calcularIndicador(cbEmpresas.getValue(),stringToInt(cbPeriodo.getValue()),obtenerIndicador(cbIndicadores.getValue()).getEcuacion())));
        });

        volver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ventanaMenu.show();
                primaryStage.close();
            }

        });

    }

    Indicador obtenerIndicador(String nombre) {
        List<Indicador> indicadores = elCore.fetchAllIndicadores();
        Indicador indicadorRetorno = new Indicador(null, null);
        for (int i = 0; i < indicadores.size(); i++) {
            if (indicadores.get(i).getNombre().equals(nombre)) {

                indicadorRetorno = indicadores.get(i);
            }
        }
        return indicadorRetorno;
    }

    int stringToInt(String anio) {

        return Integer.valueOf(anio);

    }

    Empresa obtenerEmpresa(String nombre) {
        List<Empresa> empresas = elCore.fetchAllEmpresas();
        Empresa empresaRetorno = new Empresa(null);
        for (int i = 0; i < empresas.size(); i++) {
            if (empresas.get(i).getNombre().equals(nombre)) {

                empresaRetorno = empresas.get(i);
            }
        }
        return empresaRetorno;
    }
}
