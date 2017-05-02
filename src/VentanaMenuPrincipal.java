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



public class VentanaMenuPrincipal extends Application{

    Stage window;
    Scene scene;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        window.setTitle("Sistema de Apoyo a Decisiones");
        scene = new Scene(new Group(), 350, 100);


        final Label labelFile = new Label();

        Button btnCargaArchivo = new Button("Cargar Archivo");
        Button btnConsultasXPeriodoEmpresa = new Button("Consultar Cuentas");
        Button btnCerrar = new Button("Cerrar");


        btnCargaArchivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = new Stage();
                VentanaFileChooser  vfc= new VentanaFileChooser();
                vfc.start(stage);
                stage.show();
                primaryStage.close();
            }
        });

        btnConsultasXPeriodoEmpresa.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = new Stage();
                VentanaConsulta vc = new VentanaConsulta();
                try {
                    vc.start(stage);
                }catch(Exception ex){}
                primaryStage.close();
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
        grid.add(btnCerrar,5,3);


    }

}