package net.zabalburu.recyclon.app;

import java.net.URL;
import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import net.zabalburu.recyclon.modelo.Cliente;
import net.zabalburu.recyclon.modelo.Cobro;
import net.zabalburu.recyclon.servicio.RecyclonServicio;

public class FXMLClienteController implements Initializable {

    private RecyclonServicio servicio = RecyclonServicio.getServicio();
    private final int digitosNombre = servicio.getCaracMaxCliente("NOMBRE");
    private final int digitosNumCuenta = servicio.getCaracMaxCliente("NCUENTA");
    private NumberFormat nfMoneda = NumberFormat.getCurrencyInstance();
    private boolean nCuentaNoValida = false;

    private enum Estado {
        INICIO, CONSULTA, MODIFICAR, NUEVO
    };
    private Estado estado = Estado.INICIO;
    private EnumMap<Estado, String> tituloEstado = new EnumMap<Estado, String>(Estado.class);

    @FXML
    private TableView<Cliente> tvCli;
    @FXML
    private TableColumn<Cliente, String> tcCuenta;
    @FXML
    private TableColumn<Cliente, String> tcNombre;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCuenta;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private AnchorPane panelInfo;
    @FXML
    private Text textopantallaprincipal;
    @FXML
    private Text lblEstadoFormulario;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tituloEstado.put(Estado.INICIO, "Vista general de clientes");
        tituloEstado.put(Estado.CONSULTA, "Vista general de clientes");
        tituloEstado.put(Estado.NUEVO, "Nuevo cliente");
        tituloEstado.put(Estado.MODIFICAR, "Modificar cliente");
        txtNombre.setTextFormatter(new TextFormatter<String>(cambio -> {
            if (cambio.getControlNewText().length() <= digitosNombre) {
                return cambio;
            } else {
                cambio.setText("");
                return cambio;
            }
        }));
        PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        txtCuenta.textProperty().addListener(event -> {
            nCuentaNoValida = !txtCuenta.getText().isEmpty()
                    && !txtCuenta.getText().matches("^[A-Z]{2}\\d{22}");
            txtCuenta.pseudoClassStateChanged(
                    PseudoClass.getPseudoClass("error"),
                    nCuentaNoValida);
        });
        txtCuenta.setTextFormatter(new TextFormatter<String>(cambio -> {
            if ((cambio.getText().matches("[^a-zA-Z]") && cambio.getControlNewText().length() < 3)
                    || (cambio.getText().matches("[^\\d]") && cambio.getControlNewText().length() > 2)
                    || cambio.getControlNewText().length() > digitosNumCuenta) {
                cambio.setText("");
                cambio.setRange(cambio.getRangeStart(), cambio.getRangeStart());
                return cambio;
            } else {
                if (cambio.getText().matches("[a-z]")) {
                    cambio.setText(cambio.getText().toUpperCase());
                }
                return cambio;
            }
        }));
        tcCuenta.setCellValueFactory(new PropertyValueFactory<Cliente, String>("numCuenta"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombrecliente"));
        tcNombre.setSortType(TableColumn.SortType.ASCENDING);
        graficoTarta(null);
        modoDeEstado();
        mostrarDatosTV();
    }

    @FXML
    private void pulsarTecla(KeyEvent event) {
        if (estado == estado.CONSULTA && event.getCode().equals(KeyCode.ESCAPE)) {
            estado = Estado.INICIO;
            modoDeEstado();
            limpiarTxt();
            btnNuevo.requestFocus();
            graficoTarta(null);
        } else if (!btnGuardar.isDisable() && event.getCode().equals(KeyCode.ENTER)) {
            guardar(new ActionEvent());
        }
    }

    @FXML
    public void seleccionTv(MouseEvent me) {
        this.estado = Estado.CONSULTA;
        if (!tvCli.getSelectionModel().isEmpty()) {
            Cliente e = tvCli.getSelectionModel().getSelectedItem();
            graficoTarta(e);
            this.pieChart.setLabelsVisible(false);
            txtNombre.setText(e.getNombrecliente());
            txtCuenta.setText(e.getNumCuenta() == null ? "" : e.getNumCuenta());
        }
        modoDeEstado();
    }

    @FXML
    public void agregar(ActionEvent event) {
        this.estado = Estado.NUEVO;
        limpiarTxt();
        modoDeEstado();
        txtNombre.requestFocus();
    }

    @FXML
    public void eliminar(ActionEvent event) {
        Cliente e = tvCli.getSelectionModel().getSelectedItem();
        Alert a = new Alert(Alert.AlertType.ERROR);
        if (servicio.getCobros().stream().anyMatch(co -> co.getCliente().equals(e))) {
            a.setContentText("No se puede eliminar un Cliente que tenga Cobros Asignados");
            a.showAndWait();
            tvCli.requestFocus();
        } else {
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Eliminar Cliente");
            a.setContentText("Esta operación no se puede deshacer");
            Optional<ButtonType> res = a.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                tvCli.getItems().remove(e);
                servicio.eliminarCliente(e.getIdCliente());
                Alert al = new Alert(Alert.AlertType.INFORMATION);
                al.setHeaderText("");
                al.setContentText("Cliente eliminado con éxito!");
            }
            this.estado = Estado.INICIO;
            limpiarTxt();
            modoDeEstado();
        }
    }

    @FXML
    public void guardar(ActionEvent event) {
        Cliente c = new Cliente((txtCuenta.getText().isEmpty() ? null : txtCuenta.getText()), txtNombre.getText().toUpperCase());
        if (this.estado == Estado.NUEVO) {
            c = servicio.nuevoCliente(c);
            if (c == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("El nombre de cliente y/o el número de cuenta no se puede repetir.");
                a.showAndWait();
                limpiarTxt();
                txtNombre.requestFocus();
            } else {
                estado = Estado.INICIO;
                mostrarDatosTV();
                limpiarTxt();
                modoDeEstado();
            }
        } else {
            c.setIdCliente(tvCli.getSelectionModel().getSelectedItem().getIdCliente());
            servicio.modificarCliente(c);
            mostrarDatosTV();
            tvCli.refresh();
            this.estado = Estado.INICIO;
            modoDeEstado();
        }
    }

    @FXML
    public void modificar(ActionEvent event) {
        this.estado = Estado.MODIFICAR;
        modoDeEstado();
        txtNombre.requestFocus();
    }

    @FXML
    private void validarNombre(KeyEvent event) {
        modoDeEstado();
    }

    @FXML
    private void validarNCuenta(KeyEvent event) {
        modoDeEstado();
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

    private void mostrarDatosTV() {
        tvCli.setItems(FXCollections.observableList(
                servicio.getClientes()));
        tvCli.getSortOrder().add(tcNombre);
        tvCli.sort();
    }

    private void modoDeEstado() {
        textopantallaprincipal.setText(tituloEstado.get(estado));
        txtNombre.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        txtCuenta.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        btnNuevo.setDisable(estado == Estado.MODIFICAR || estado == Estado.NUEVO);
        btnModificar.setDisable(estado != Estado.CONSULTA);
        btnEliminar.setDisable(estado != Estado.CONSULTA);
        btnGuardar.setDisable(estado == Estado.INICIO
                || estado == Estado.CONSULTA
                || (txtNombre.getText().isEmpty() || nCuentaNoValida));
    }

    private void limpiarTxt() {
        txtNombre.clear();
        txtCuenta.clear();
    }

    private void graficoTarta(Cliente c) {
        Double dato1 = 0.0;
        Double dato2 = 0.0;
        if (c == null) {
            dato1 = servicio.getCobros().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            dato2 = servicio.getCobros().stream().filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));

        } else {
            dato1 = servicio.getCobros().stream().filter(b -> b.getCliente().equals(c)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            dato2 = servicio.getCobros().stream().filter(b -> b.getCliente().equals(c)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
        }
        lblSaldoxPagar.setText(nfMoneda.format(dato1));
        lblSaldoCobrado.setText(nfMoneda.format(dato2));
        int fxp = (int) ((dato1 * 100) / (dato1 + dato2));
        int fxc = (int) ((dato2 * 100) / (dato1 + dato2));
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Facturas por Cobrar", fxp),
                        new PieChart.Data("Facturas Cobradas", fxc));
        this.pieChart.setData(pieChartData);
        this.pieChart.setLabelsVisible(false);
    }

    private void mostrarPorc(MouseEvent event) {
        this.pieChart.getData().get(0).getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                lblpor1.setVisible(true);
                lblpor1.setTranslateX(e.getX() + 150);
                lblpor1.setTranslateY(e.getY() - 200);
                lblpor1.setText(String.valueOf(pieChart.getData().get(0).getPieValue()) + "%");
            }
        });
        this.pieChart.getData().get(1).getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                lblpor2.setVisible(true);
                lblpor2.setTranslateX(e.getX() - 150);
                lblpor2.setTranslateY(e.getY() - 200);
                lblpor2.setText(String.valueOf(pieChart.getData().get(1).getPieValue()) + "%");
            }
        });
    }

    private void quitarPorc(MouseEvent event) {
        this.pieChart.getData().get(0).getNode().addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                lblpor1.setVisible(false);
            }
        });
        this.pieChart.getData().get(1).getNode().addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                lblpor2.setVisible(false);
            }
        });

    }
}
