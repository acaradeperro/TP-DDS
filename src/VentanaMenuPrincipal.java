import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;

import java.sql.SQLException;


public class VentanaMenuPrincipal extends Application{

    Stage window;
    Scene scene;
    Core elCore;


    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        elCore = new Core();
        elCore.main(null); //otra forma mas linda de hacer esto?

        window = primaryStage;
        window.setTitle("Sistema de Apoyo a Decisiones");
        scene = new Scene(new Group(), 350, 100);


        final Label labelFile = new Label();

        Button btnCargaArchivo = new Button("Cargar Archivo");
        Button btnConsultasXPeriodoEmpresa = new Button("Consultar Cuentas");
        Button btnCargarIndicadores = new Button("Cargar indicadores");
        Button btnConsultarIndicadores = new Button("Consultar indicadores");
        Button btnCerrar = new Button("Cerrar");


        btnCargaArchivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = new Stage();
                VentanaFileChooser  vfc= new VentanaFileChooser(primaryStage,elCore);
                vfc.start(stage);
                stage.show();
                primaryStage.hide();
            }
        });


        btnConsultasXPeriodoEmpresa.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = new Stage();
                VentanaConsulta vc = new VentanaConsulta(primaryStage,elCore);
                try {
                    vc.start(stage);
                }catch(Exception ex){}
                primaryStage.hide();
            }
        });

        btnCargarIndicadores.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = new Stage();
                VentanaCargarIndicadores vci = new VentanaCargarIndicadores(primaryStage,elCore);
                try {
                    vci.start(stage);
                }catch(Exception ex){}
                primaryStage.hide();
            }
        });

        btnConsultarIndicadores.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = new Stage();
                VentanaConsultarIndicadores vci = new VentanaConsultarIndicadores(primaryStage,elCore);
                try {
                    vci.start(stage);
                }catch(Exception ex){}
                primaryStage.hide();
            }
        });

        btnCerrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                primaryStage.close();
            }
        });
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));

        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);
        primaryStage.setScene(scene);
        primaryStage.show();

        grid.add(btnCargaArchivo,3,3);
        grid.add(btnConsultasXPeriodoEmpresa,4,3);
        grid.add(btnCargarIndicadores,3,4);
        grid.add(btnConsultarIndicadores,4,4);
        grid.add(btnCerrar,5,5);


    }

}