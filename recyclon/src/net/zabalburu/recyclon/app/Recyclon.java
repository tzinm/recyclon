package net.zabalburu.recyclon.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Recyclon extends Application {
    
    public static Stage stage;
    public static Stage stGraf = new Stage();
    
    @Override
    public void start(Stage stage) throws Exception {
        Recyclon.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/net/zabalburu/recyclon/fxml/FXMLPPrincipal.fxml"));
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/net/zabalburu/recyclon/img/recy.png")));
        stage.setScene(scene);
        stage.setTitle("RECYCLON SLS");
        stage.centerOnScreen();
        stage.setX(0);
        stage.setY(0);
        stage.show();
        Parent root6 = FXMLLoader.load(this.getClass().getResource("/net/zabalburu/recyclon/fxml/FXMLestadisticas.fxml"));
            Scene sc6 = new Scene(root6);
            sc6.setFill(Color.TRANSPARENT);
            stGraf.setScene(sc6);
            stGraf.centerOnScreen();
            stGraf.setTitle("Graficos");
            stGraf.getIcons().add(new Image(this.getClass().getResourceAsStream("/net/zabalburu/recyclon/img/recy.png")));
            stGraf.setResizable(false);
            stGraf.setOnCloseRequest(evt-> {
                stGraf.hide();
            });
            stGraf.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
