package net.zabalburu.recyclon.app;

import java.net.URL;
import java.text.DecimalFormat;
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
import net.zabalburu.recyclon.modelo.Banco;
import net.zabalburu.recyclon.modelo.Cobro;
import net.zabalburu.recyclon.modelo.Pago;
import net.zabalburu.recyclon.servicio.RecyclonServicio;

public class FXMLBancoController implements Initializable {

    private RecyclonServicio servicio = RecyclonServicio.getServicio();
    private final int digitosBanco = servicio.getCaracMaxBanco("NOMBRE");
    private DecimalFormat f = new DecimalFormat("#.0");
    private NumberFormat nfMoneda = NumberFormat.getCurrencyInstance();
    private enum Estado {
        INICIO, CONSULTA, MODIFICAR, NUEVO
    };
    private Estado estado = Estado.INICIO;
    private EnumMap<Estado, String> tituloEstado = new EnumMap<Estado, String>(Estado.class);

    @FXML
    private PieChart pieChart;
    @FXML
    private Label lblpor1;
    @FXML
    private Label lblpor2;
    @FXML
    private TextField txtNombre;
    @FXML
    private TableView<Banco> tvBanco;
    @FXML
    private TableColumn<Banco, String> tcNombre;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Label lblSaldoxPagar;
    @FXML
    private Label lblSaldoCobrado;
    @FXML
    private AnchorPane panelInfo;
    @FXML
    private Text textopantallaprincipal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tituloEstado.put(Estado.INICIO, "Vista general de bancos");
        tituloEstado.put(Estado.CONSULTA, "Vista general de bancos");
        tituloEstado.put(Estado.NUEVO, "Nuevo banco");
        tituloEstado.put(Estado.MODIFICAR, "Modificar banco");
        txtNombre.setTextFormatter(new TextFormatter<String>(cambio -> {
            if (cambio.getControlNewText().length() <= digitosBanco) {
                return cambio;
            } else {
                cambio.setText("");
                cambio.setRange(cambio.getRangeStart(), cambio.getRangeStart());
                return cambio;
            }
        }));
        tcNombre.setCellValueFactory(new PropertyValueFactory<Banco, String>("nombreBanco"));
        tcNombre.setSortType(TableColumn.SortType.ASCENDING);
        mostrarDatos();
        graficoTarta(null);
        modoDeEstado();
    }
    
    @FXML
    private void validarNombre(KeyEvent event) {
        modoDeEstado();
    }

    @FXML
    public void agregar(ActionEvent event) {
        limpiarTxt();
        desHabilitarTxt(false);
        txtNombre.requestFocus();
        this.estado = Estado.NUEVO;
        modoDeEstado();
    }

    @FXML
    public void eliminar(ActionEvent event) {
        Banco b = tvBanco.getSelectionModel().getSelectedItem();
        Alert a = new Alert(Alert.AlertType.ERROR);
        if (servicio.getPagos().stream().anyMatch(ban -> ban.getBanco().equals(b)) || servicio.getCobros().stream().anyMatch(ban -> ban.getBanco().equals(b))) {
            a.setContentText("No se puede eliminar un Banco que tenga Pagos y Cobros Asignados");
            a.showAndWait();
        } else {
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Eliminar Banco");
            a.setContentText("Esta operación no se puede deshacer");
            Optional<ButtonType> res = a.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                tvBanco.getItems().remove(b);
                servicio.eliminarBanco(b.getIdBanco());
                Alert al = new Alert(Alert.AlertType.INFORMATION);
                al.setHeaderText("");
                al.setContentText("Banco eliminado con éxito!");
            }
        }
        limpiarTxt();
        this.estado = Estado.INICIO;
        modoDeEstado();
    }

    @FXML
    public void modificar(ActionEvent event) {
        this.estado = Estado.MODIFICAR;
        modoDeEstado();
        desHabilitarTxt(false);
        txtNombre.requestFocus();
    }

    @FXML
    public void seleccionTv(MouseEvent event) {
        this.estado = Estado.CONSULTA;
        if (!tvBanco.getSelectionModel().isEmpty()) {
            Banco b = tvBanco.getSelectionModel().getSelectedItem();
            graficoTarta(b);
            this.pieChart.setLabelsVisible(false);
            txtNombre.setText(b.getNombreBanco());
            desHabilitarTxt(Boolean.TRUE);
        }
        modoDeEstado();
    }

    @FXML
    public void guardar(ActionEvent event) {
        Banco b = new Banco(txtNombre.getText().toUpperCase());
        if (this.estado == Estado.NUEVO) {
            b = servicio.nuevoBanco(b);
            if (b == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Ya existe un banco con el mismo nombre.");
                a.showAndWait();
                limpiarTxt();
                txtNombre.requestFocus();
            } else {
                tvBanco.getItems().add(b);
                desHabilitarTxt(Boolean.TRUE);
                this.estado = Estado.INICIO;
                modoDeEstado();
            }
        } else if (this.estado == Estado.MODIFICAR) {
            b.setIdBanco(tvBanco.getSelectionModel().getSelectedItem().getIdBanco());
            servicio.modificarBanco(b);
            tvBanco.getItems().set(tvBanco.getSelectionModel().getSelectedIndex(), b);
            desHabilitarTxt(Boolean.TRUE);
            this.estado = Estado.INICIO;
            modoDeEstado();
        }
    }

    @FXML
    private void pulsarTecla(KeyEvent event) {
        if (estado == Estado.CONSULTA && event.getCode().equals(KeyCode.ESCAPE)) {
            estado = Estado.INICIO;
            mostrarDatos();
            modoDeEstado();
            txtNombre.clear();
            graficoTarta(null);
            btnAgregar.requestFocus();
        }else if (!btnGuardar.isDisable() && event.getCode().equals(KeyCode.ENTER)) {
            guardar(new ActionEvent());
        }
    }

    @FXML
    private void mostrarTartaGeneral(MouseEvent e) {
        graficoTarta(null);
        this.pieChart.setLabelsVisible(false);
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

    private void modoDeEstado() {
        textopantallaprincipal.setText(tituloEstado.get(estado));
        txtNombre.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        btnAgregar.setDisable(estado == Estado.MODIFICAR || estado == Estado.NUEVO);
        btnModificar.setDisable(estado != Estado.CONSULTA);
        btnEliminar.setDisable(estado != Estado.CONSULTA);
        btnGuardar.setDisable(estado == Estado.INICIO
                || estado == Estado.CONSULTA
                || txtNombre.getText().isEmpty());
    }

    private void graficoTarta(Banco banco) {
        Double dato1 = 0.0;
        Double dato2 = 0.0;
        if (banco == null) {
            dato1 = servicio.getPagos().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            dato2 = servicio.getCobros().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
        } else {
            dato1 = servicio.getPagos().stream().filter(b -> b.getBanco().equals(banco)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            dato2 = servicio.getCobros().stream().filter(b -> b.getBanco().equals(banco)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
        }
        lblSaldoxPagar.setText(nfMoneda.format(dato1));
        lblSaldoCobrado.setText(nfMoneda.format(dato2));
        double fxp = ((dato1 * 100) / (dato1 + dato2));
        double fxc = ((dato2 * 100) / (dato1 + dato2));
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Facturas por Pagar", Math.round(fxp)),
                        new PieChart.Data("Facturas por Cobrar", Math.round(fxc)));

        this.pieChart.setData(pieChartData);
        this.pieChart.setLabelsVisible(false);
    }

    protected void mostrarDatos() {
        tvBanco.setItems(FXCollections.observableList(servicio.getBancos()));
        tvBanco.getSortOrder().add(tcNombre);
        tvBanco.sort();
    }

    private void limpiarTxt() {
        txtNombre.clear();
    }

    private void desHabilitarTxt(Boolean b) {
        txtNombre.setDisable(b);
    }
}
