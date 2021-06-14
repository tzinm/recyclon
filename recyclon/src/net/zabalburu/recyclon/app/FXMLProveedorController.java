package net.zabalburu.recyclon.app;

import java.net.URL;
import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import net.zabalburu.recyclon.modelo.Pago;
import net.zabalburu.recyclon.modelo.Proveedor;
import net.zabalburu.recyclon.servicio.RecyclonServicio;

public class FXMLProveedorController implements Initializable {

    private RecyclonServicio servicio = RecyclonServicio.getServicio();
    private final int digitosNombre = servicio.getCaracMaxProv("NOMBRE");
    private NumberFormat nfMoneda = NumberFormat.getCurrencyInstance();
    @FXML
    private void clickPantallaProveedores(MouseEvent event) {
    }
    
    private enum Estado {
        INICIO, CONSULTA, MODIFICACION, ALTA
    };
    private Estado estado = Estado.INICIO;
    private EnumMap<Estado, String> tituloEstado = new EnumMap<Estado, String>(Estado.class);

    @FXML
    private AnchorPane pantallaProveedores;
    @FXML
    private Button btnGuardar;
    @FXML
    private TableView<Proveedor> tvPro;
    @FXML
    private TableColumn<Proveedor, String> tcNombre;
    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private AnchorPane panelInfo;
    @FXML
    private Text textopantallaprincipal;
    @FXML
    private Button btnNuevo;
    @FXML
    private Label lblSaldoxPagar;
    @FXML
    private Label lblSaldoCobrado;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label lblpor1;
    @FXML
    private Label lblpor2;

    @FXML
    private void nuevoProveedor(ActionEvent event) {
        estado = Estado.ALTA;
        txtNombre.clear();
        mostrar();
        txtNombre.requestFocus();
    }

    @FXML
    private void modificarProveedor(ActionEvent event) {
        estado = Estado.MODIFICACION;
        txtNombre.requestFocus();
        mostrar();
    }

    @FXML
    private void eliminarProveedor(ActionEvent event) {
        if (!datosAsociados()) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setContentText("¿Está seguro que desea eliminar el cliente?");
            Optional<ButtonType> seleccion = alerta.showAndWait();
            if (seleccion.isPresent() && seleccion.get() == ButtonType.OK) {
                servicio.eliminarProveedor(tvPro.getSelectionModel().getSelectedItem().getIdProveedor());
            }
            txtNombre.clear();
            estado = Estado.INICIO;
            mostrar();
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("No se puede eliminar un proveedor que contenga pagos asociados");
            alerta.showAndWait();
        }
    }

    @FXML
    private void pulsarGuardar(ActionEvent event) {
        guardar();
        mostrar();
    }

    @FXML
    private void pulsarTecla(KeyEvent event) {
        if (estado == Estado.CONSULTA && event.getCode().equals(KeyCode.ESCAPE)) {
            estado = Estado.INICIO;
            mostrar();
            txtNombre.clear();
            graficoTarta(null);
            btnNuevo.requestFocus();
        } else if (!btnGuardar.isDisable() && event.getCode().equals(KeyCode.ENTER)) {
            guardar();
        }
    }

    @FXML
    private void validarNombre(KeyEvent event) {
        mostrar();
    }

    @FXML
    private void seleccionarProveedor(MouseEvent event) {
        estado = Estado.CONSULTA;
        Proveedor p = tvPro.getSelectionModel().getSelectedItem();
        txtNombre.setText(p.getNombreProveedor());
        mostrar();
        graficoTarta(p);
    }

    @FXML
    private void mostrarTartaGeneral(MouseEvent e) {
        graficoTarta(null);
    }

    @FXML
    private void mostrarPorcentaje(MouseEvent event) {
        this.pieChart.setLabelsVisible(true);
        if (this.pieChart.getLabelsVisible()) {
            this.pieChart.getData().forEach(d -> {
                Optional<Node> opTextNode = pieChart.lookupAll(".chart-pie-label")
                        .stream()
                        .filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName()))
                        .findAny();
                if (opTextNode.isPresent()) {
                    Text t = (Text) opTextNode.get();
                    t.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
                    t.setFill(((Region) d.getNode()).getBackground().getFills().get(0).getFill());
                    t.setText(NumberFormat.getNumberInstance().format(d.getPieValue()) + "%");
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tituloEstado.put(Estado.INICIO, "Vista general de proveedores");
        tituloEstado.put(Estado.CONSULTA, "Vista general de proveedores");
        tituloEstado.put(Estado.ALTA, "Nuevo proveedor");
        tituloEstado.put(Estado.MODIFICACION, "Modificar proveedor");
        tcNombre.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombreProveedor"));
        txtNombre.setTextFormatter(new TextFormatter<String>(cambio -> {
            if (cambio.getControlNewText().length() <= digitosNombre) {
                return cambio;
            } else {
                cambio.setText("");
                cambio.setRange(cambio.getRangeStart(), cambio.getRangeStart());
                return cambio;
            }
        }));
        mostrar();
        graficoTarta(null);
    }

    private void mostrar() {
        if (!tvPro.isFocused()) {
            https://stackoverflow.com/a/36240791/12868523
            tvPro.setItems(FXCollections.observableList(
                    servicio.getProveedores()));
            tvPro.getSortOrder().add(tcNombre);
        }
        textopantallaprincipal.setText(tituloEstado.get(estado));
        txtNombre.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        btnGuardar.setDisable(estado == Estado.CONSULTA || estado == Estado.INICIO || txtNombre.getText().isEmpty());
        btnModificar.setDisable(estado != Estado.CONSULTA);
        btnEliminar.setDisable(estado != Estado.CONSULTA);
        btnNuevo.setDisable(estado == Estado.ALTA || estado == Estado.MODIFICACION);
    }

    private void guardar() {
        Proveedor p = new Proveedor(txtNombre.getText().toUpperCase());
        if (estado == Estado.ALTA) {
            p = servicio.nuevoProveedor(p);
            if (p == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Ya existe un proveedor con el mismo nombre.");
                a.showAndWait();
                txtNombre.clear();
                txtNombre.requestFocus();
            } else {
                estado = Estado.INICIO;
                mostrar();
                txtNombre.clear();
            }
        } else {
            p.setIdProveedor(tvPro.getSelectionModel().getSelectedItem().getIdProveedor());
            servicio.modificarProveedor(p);
            tvPro.refresh();
            estado = Estado.INICIO;
            mostrar();
            txtNombre.clear();
        }
    }

    private boolean datosAsociados() {
        return servicio.getPagos()
                .stream()
                .anyMatch(
                        p -> p.getProveedor().equals(tvPro.getSelectionModel().getSelectedItem()));
    }

    private void graficoTarta(Proveedor p) {
        Double dato1 = 0.0;
        Double dato2 = 0.0;
        if (p == null) {
            dato1 = servicio.getPagos().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            dato2 = servicio.getPagos().stream().filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
        } else {
            dato1 = servicio.getPagos().stream().filter(b -> b.getProveedor().equals(p)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            dato2 = servicio.getPagos().stream().filter(b -> b.getProveedor().equals(p)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
        }
        lblSaldoxPagar.setText(nfMoneda.format(dato1));
        lblSaldoCobrado.setText(nfMoneda.format(dato2));
        int fxp = (int) ((dato1 * 100) / (dato1 + dato2));
        int fxc = (int) ((dato2 * 100) / (dato1 + dato2));
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Facturas por pagar", fxp),
                        new PieChart.Data("Facturas pagadas", fxc));
        this.pieChart.setData(pieChartData);
        this.pieChart.setLabelsVisible(false);
    }
}
