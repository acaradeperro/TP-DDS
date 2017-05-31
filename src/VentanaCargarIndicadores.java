import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.sl.usermodel.TextBox;

import model.*;
import controller.*;

import java.io.File;


public class VentanaCargarIndicadores extends Application {
    private Stage ventanaMenu;
    File file;
    Stage window;
    Scene scene;

    VentanaCargarIndicadores(Stage ventanaRecibida){this.ventanaMenu = ventanaRecibida;}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        window.setTitle("Cargar indicador");
        scene = new Scene(new Group(), 400, 100);

        final Label labelFile = new Label();
        Label lEspacio = new Label();
        Button btnVolver = new Button();
        Button btnConfirmar = new Button();
        TextField txtNombre = new TextField();
        TextField txtEcuacion = new TextField();

        btnVolver.setText("Volver");
        btnConfirmar.setText("Confirmar");

        btnConfirmar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndicadorController.insertarIndicadorEnBd(txtNombre.getText(),txtEcuacion.getText());
            }
        });

        btnVolver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ventanaMenu.show();
                primaryStage.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        primaryStage.setScene(scene);
        primaryStage.show();

        grid.add(lEspacio, 3, 4);
        grid.add(labelFile, 4, 3);
        grid.add(btnConfirmar, 3, 5);
        grid.add(btnVolver, 5, 5);
        grid.add(txtNombre, 4,4);
        grid.add(txtEcuacion, 4,5);


    }
}