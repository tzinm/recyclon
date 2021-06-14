package net.zabalburu.recyclon.app;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXMLPPrincipalController implements Initializable {

    private static Stage stPago = new Stage();
    private static Stage stCobro = new Stage();
    private static Stage stClien = new Stage();
    private static Stage stBanco = new Stage();
    private static Stage stProv = new Stage();

    @FXML
    private BorderPane borderPanePrincipal;
    @FXML
    private FlowPane Footer;
    @FXML
    private Label lblFechaActual;
    @FXML
    private FlowPane MenuIzquierda;
    @FXML
    private Button btnCliente;
    @FXML
    private Button btnProv;
    @FXML
    private Button btnBanco;
    @FXML
    private Button btnPago;
    @FXML
    private Button btnCobro;
    @FXML
    private MenuButton MenuH;
    @FXML
    private MenuItem menuItemCliente1;
    @FXML
    private MenuItem menuItemProveedor1;
    @FXML
    private MenuItem menuItemBanco1;
    @FXML
    private MenuItem menuItemVerPagos1;
    @FXML
    private MenuItem menuItemVerCobro1;
    @FXML
    private MenuItem menuItemAyuda1;
    @FXML
    private MenuItem menuItemCerrarApli1;
    @FXML
    private ImageView recyclon;
    @FXML
    private Button btnGraficos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fechaHora();
        try {
            /**
             * *************************************Pago************************************************************
             */
            Parent root1 = FXMLLoader.load(this.getClass().getResource("/net/zabalburu/recyclon/fxml/FXMLPago.fxml"));
            Scene sc1 = new Scene(root1);
            sc1.setFill(Color.TRANSPARENT);
            stPago.setScene(sc1);
            stPago.centerOnScreen();
            stPago.setTitle("Gestión de Pagos");
            stPago.getIcons().add(new Image(this.getClass().getResourceAsStream("/net/zabalburu/recyclon/img/recy.png")));
            stPago.setResizable(false);
            stPago.setOnCloseRequest(evt -> {
                stPago.hide();
            });
            stPago.hide();
            /**
             * ****************************************Cobro**********************************************************
             */
            Parent root2 = FXMLLoader.load(this.getClass().getResource("/net/zabalburu/recyclon/fxml/FXMLCobro.fxml"));
            Scene sc2 = new Scene(root2);
            sc2.setFill(Color.TRANSPARENT);
            stCobro.setScene(sc2);
            stCobro.centerOnScreen();
            stCobro.setTitle("Gestión de Cobros");
            stCobro.getIcons().add(new Image(this.getClass().getResourceAsStream("/net/zabalburu/recyclon/img/recy.png")));
            stCobro.setResizable(false);
            stCobro.setOnCloseRequest(evt -> {
                stCobro.hide();
            });
            stCobro.hide();
            /**
             * ****************************************Cliente**********************************************************
             */
            Parent root3 = FXMLLoader.load(this.getClass().getResource("/net/zabalburu/recyclon/fxml/FXMLCliente.fxml"));
            Scene sc3 = new Scene(root3);
            sc3.setFill(Color.TRANSPARENT);
            stClien.setScene(sc3);
            stClien.centerOnScreen();
            stClien.setTitle("Gestión de Clientes");
            stClien.getIcons().add(new Image(this.getClass().getResourceAsStream("/net/zabalburu/recyclon/img/recy.png")));
            stClien.setResizable(false);
            stClien.setOnCloseRequest(evt -> {
                stClien.hide();
            });
            stClien.hide();
            /**
             * ****************************************Banco**********************************************************
             */
            Parent root4 = FXMLLoader.load(this.getClass().getResource("/net/zabalburu/recyclon/fxml/FXMLBanco.fxml"));
            Scene sc4 = new Scene(root4);
            sc4.setFill(Color.TRANSPARENT);
            stBanco.setScene(sc4);
            stBanco.centerOnScreen();
            stBanco.setTitle("Gestión de Bancos");
            stBanco.getIcons().add(new Image(this.getClass().getResourceAsStream("/net/zabalburu/recyclon/img/recy.png")));
            stBanco.setResizable(false);
            stBanco.setOnCloseRequest(evt -> {
                stBanco.hide();
            });
            stBanco.hide();
            /**
             * ****************************************Proveedor**********************************************************
             */
            Parent root5 = FXMLLoader.load(this.getClass().getResource("/net/zabalburu/recyclon/fxml/FXMLProveedor.fxml"));
            Scene sc5 = new Scene(root5);
            sc5.setFill(Color.TRANSPARENT);
            stProv.setScene(sc5);
            stProv.centerOnScreen();
            stProv.setTitle("Gestión de Cobros");
            stProv.getIcons().add(new Image(this.getClass().getResourceAsStream("/net/zabalburu/recyclon/img/recy.png")));
            stProv.setResizable(false);
            stProv.setOnCloseRequest(evt -> {
                stProv.hide();
            });
            stProv.hide();
        } catch (IOException ex) {
            Logger.getLogger(FXMLPPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void cerrarAplicacion(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void irAyuda(ActionEvent event) {
        String url = "https://zabalrecyclon.duckdns.org";
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (URISyntaxException ex) {
                Logger.getLogger(FXMLPPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLPPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    public void VerPagos(ActionEvent event) {
        this.Pago(event);
    }

    @FXML
    public void VerCobros(ActionEvent event) {
        this.Cobro(event);
    }

    private void fechaHora() {
        Timeline reloj = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
                    if (lblFechaActual != null) {
                        lblFechaActual.setText(LocalDateTime.now().format(formatter));
                    }
                }), new KeyFrame(Duration.seconds(1))
        );
        reloj.setCycleCount(Animation.INDEFINITE);
        reloj.play();
    }

    @FXML
    public void Banco(ActionEvent event) {
        if (!stBanco.isShowing()) {
            stBanco.showAndWait();
        }
    }

    @FXML
    public void Cliente(ActionEvent event) {
        if (!stClien.isShowing()) {
            stClien.showAndWait();
        }
    }

    @FXML
    public void Cobro(ActionEvent event) {
        if (!stCobro.isShowing()) {
            stCobro.showAndWait();
        }
    }

    @FXML
    public void Pago(ActionEvent event) {
        if (!stPago.isShowing()) {
            stPago.showAndWait();
        }
    }

    @FXML
    public void Proveedor(ActionEvent event) {
        if (!stProv.isShowing()) {
            stProv.showAndWait();
        }
    }

    @FXML
    private void graficos(ActionEvent event) {
        if (!Recyclon.stGraf.isShowing()) {
            Recyclon.stGraf.showAndWait();
        }
    }

}
