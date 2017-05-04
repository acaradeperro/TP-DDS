import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static javafx.application.Application.launch;


public class VentanaFileChooser extends Application {
    private Stage ventanaMenu;
    private Core elCore;
    File file;
    Stage window;
    Scene scene;

    VentanaFileChooser(Stage ventanaRecibida, Core miCore){this.ventanaMenu = ventanaRecibida;this.elCore = miCore;}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        window.setTitle("Cargar Archivo");
        scene = new Scene(new Group(), 400, 100);

        final Label labelFile = new Label();
        Label lEspacio = new Label();
        Button btn = new Button();
        Button btnVolver = new Button();
        Button btnConfirmar = new Button();

        btnVolver.setText("Volver");
        btnConfirmar.setText("Confirmar");
        btn.setText("Buscar Archivo");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos XLSX (*.xlsx)", "*.xlsx");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show open file dialog
                file = fileChooser.showOpenDialog(null);

                labelFile.setText(file.getPath());
            }
        });

        btnConfirmar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                elCore.cargarDatos(file);
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
        grid.add(btn, 3, 3);
        grid.add(btnConfirmar, 3, 5);
        grid.add(btnVolver, 4, 5);


    }
}