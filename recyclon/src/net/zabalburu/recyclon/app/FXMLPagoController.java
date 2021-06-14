package net.zabalburu.recyclon.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import net.zabalburu.recyclon.modelo.Banco;
import net.zabalburu.recyclon.modelo.Pago;
import net.zabalburu.recyclon.modelo.Proveedor;
import net.zabalburu.recyclon.servicio.RecyclonServicio;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;


public class FXMLPagoController implements Initializable {

    private RecyclonServicio servicio = RecyclonServicio.getServicio();
    private PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    private enum Estado {
        INICIO, CONSULTA, MODIFICACION, ALTA
    }
    private Estado estado = Estado.INICIO;
    private EnumMap<Estado, String> estados = new EnumMap<Estado, String>(Estado.class);
    private List<String> formasPago = new ArrayList<String>();
    private Map<String, Boolean> estadosPago = new HashMap<>();
    private int idPago;
    private final int digitosFactura = servicio.getNumMaxPago("NUMFACTURA");
    private final int digitosImporte = servicio.getNumMaxPago("IMPORTE");
    private boolean importeNoValido = false;
    private boolean facturaNoValida = false;

    //Formateadores
    private NumberFormat nfMoneda = NumberFormat.getCurrencyInstance();
    private NumberFormat nfDecimal = NumberFormat.getInstance();

    @FXML
    private TextField txtFilProveedor;
    @FXML
    private Button btnLProveedor;
    @FXML
    private Button btnlimFDesde;
    @FXML
    private Button btnlimFHasta;
    @FXML
    private AnchorPane anchorPaneprincipal;
    @FXML
    private TextField txtImporte;
    @FXML
    private TableView<Pago> tvPago;
    @FXML
    private TableColumn<Pago, String> tcProv;
    @FXML
    private TableColumn<Pago, Double> tcImporte;
    @FXML
    private TableColumn<Pago, Integer> tcFactura;
    @FXML
    private TableColumn<Pago, Date> tcFecha;
    @FXML
    private TableColumn<Pago, String> tcFPago;
    @FXML
    private TableColumn<Pago, String> tcBanco;
    @FXML
    private TableColumn<Pago, Boolean> tcEstado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCsv;
    @FXML
    private ComboBox<Proveedor> cbxProv;
    @FXML
    private TextField txtfactura;
    @FXML
    private ComboBox<String> cbxFPago;
    @FXML
    private ComboBox<Banco> cbxBanco;
    @FXML
    private ComboBox<Banco> cbxBanco2;
    @FXML
    private CheckBox chkEstado;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private ComboBox<String> cbxEstado;
    @FXML
    private Button btnGuardar;
    @FXML
    private AnchorPane panelInfo;
    @FXML
    private Text textopantallaprincipal;
    @FXML
    private DatePicker dpFecha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crearListas();
        estados.put(Estado.INICIO, "Resumen general de pagos");
        estados.put(Estado.CONSULTA, "Listado de pagos");
        estados.put(Estado.MODIFICACION, "Modificar pago");
        estados.put(Estado.ALTA, "Nuevo pago");
        nfMoneda.setMinimumFractionDigits(2);
        nfMoneda.setMaximumFractionDigits(2);
        nfDecimal.setMaximumFractionDigits(2);
        AutoCompletionBinding<Proveedor> acbProveedores = TextFields.bindAutoCompletion(txtFilProveedor, servicio.getProveedores());
        acbProveedores.setOnAutoCompleted(evt -> {
            mostrarDatosTV();
            consultarEstados();
        });
        //https://edencoding.com/javafx-textfield/
        txtfactura.textProperty().addListener(cambio -> {
            facturaNoValida = !txtfactura.getText().isEmpty() && !txtfactura.getText().matches("\\d{" + digitosFactura + "}");
            txtfactura.pseudoClassStateChanged(errorClass, facturaNoValida);
        });
        txtfactura.setTextFormatter(new TextFormatter<String>(cambio -> {
            if (cambio.getText().matches("\\d*") && cambio.getControlNewText().length() <= digitosFactura) {
                return cambio;
            } else {
                cambio.setText("");
                cambio.setRange(cambio.getRangeStart(), cambio.getRangeStart());
                return cambio;
            }
        }
        ));
        txtfactura.setAlignment(Pos.CENTER_RIGHT);
        txtImporte.textProperty().addListener(cambio -> {
            importeNoValido = !txtImporte.getText().isEmpty()
                    && !txtImporte.getText().matches("^\\d{1,8}(,\\d{1,})?(\\s)?(\\u20AC)?");
            txtImporte.pseudoClassStateChanged(errorClass, importeNoValido);
        });
        txtImporte.setTextFormatter(new TextFormatter<String>(cambio -> {
            if ((cambio.getText().matches("\\d*") && cambio.getText().matches("[^,]") && cambio.getControlNewText().length() == digitosImporte + 1)
                    || cambio.getText().matches("[a-zA-Z]")) {
                cambio.setText("");
                cambio.setRange(cambio.getRangeStart(), cambio.getRangeStart());
                return cambio;
            } else {
                return cambio;
            }
        }));
        tcProv.setCellValueFactory(new PropertyValueFactory<Pago, String>("proveedor"));
        tcImporte.setCellValueFactory(new PropertyValueFactory<Pago, Double>("importe"));
        tcImporte.setCellFactory(columna -> {
            return new TableCell<Pago, Double>() {
                @Override
                protected void updateItem(Double importe, boolean empty) {
                    super.updateItem(importe, empty);
                    if (importe == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(nfMoneda.format(importe));
                        this.setAlignment(Pos.CENTER_RIGHT);
                    }
                }
            };
        });
        tcFactura.setCellValueFactory(new PropertyValueFactory<Pago, Integer>("NFactura"));
        tcFactura.setStyle("-fx-alignment: CENTER;");
        tcFecha.setCellValueFactory(new PropertyValueFactory<Pago, Date>("FVencimiento"));
        tcFecha.setCellFactory(columna -> {
            return new TableCell<Pago, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(DateFormat.getDateInstance().format(item));
                        this.setAlignment(Pos.CENTER);
                    }
                }
            };
        });
        tcFPago.setCellValueFactory(new PropertyValueFactory<Pago, String>("FPago"));
        tcBanco.setCellValueFactory(new PropertyValueFactory<Pago, String>("banco"));
        tcEstado.setCellValueFactory(new PropertyValueFactory<Pago, Boolean>("estado"));
        tcEstado.setCellFactory(t -> {
            return new TableCell<Pago, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        if (item) {
                            Image icono = new Image(getClass().getResource("/net/zabalburu/recyclon/img/checked.png").toString());
                            this.setGraphic(new ImageView(icono));
                        } else {
                            Image icono = new Image(getClass().getResource("/net/zabalburu/recyclon/img/unchecked.png").toString());
                            this.setGraphic(new ImageView(icono));
                        }
                    } else {
                        this.setGraphic(null);
                    }
                }
            };
        });
        tcEstado.setStyle("-fx-alignment: CENTER");
        List<Banco> bancos = servicio.getBancos();
        bancos.sort((b1, b2) -> b1.getNombreBanco().compareToIgnoreCase((b2.getNombreBanco())));
        cbxBanco.setItems(FXCollections.observableArrayList(bancos));
        Banco b = new Banco("Todos");
        b.setIdBanco(0);
        cbxBanco.getItems().add(0, b);
        cbxBanco.getSelectionModel().selectFirst();
        cbxBanco2.setItems(FXCollections.observableArrayList(servicio.getBancos())
                .sorted((b1, b2) -> b1.getNombreBanco().compareToIgnoreCase(b2.getNombreBanco())));
        List<String> estados = estadosPago.keySet().stream().collect(Collectors.toList());
        estados.sort((e1, e2) -> e1.compareToIgnoreCase(e2));
        cbxEstado.setItems(FXCollections.observableArrayList(estados));
        cbxEstado.getItems().add(0, "Todos");
        cbxEstado.getSelectionModel().selectFirst();
        cbxFPago.setItems(FXCollections.observableList(formasPago)
                .sorted((fp1, fp2) -> fp1.compareToIgnoreCase(fp2)));
        cbxProv.setItems(FXCollections.observableList(servicio.getProveedores())
                .sorted((prov1, prov2) -> prov1.getNombreProveedor().compareToIgnoreCase(prov2.getNombreProveedor())));
        mostrarDatosTV();
        consultarEstados();
    }

    @FXML
    private void selecPago(MouseEvent event) {
        estado = estado.CONSULTA;
        if (!tvPago.getSelectionModel().isEmpty()) {
            idPago = tvPago.getSelectionModel().getSelectedItem().getIdPago();
            cbxProv.setValue(tvPago.getSelectionModel().getSelectedItem().getProveedor());
            txtImporte.setText(nfMoneda.format(tvPago.getSelectionModel().getSelectedItem().getImporte()));
            txtfactura.setText(Integer.toString(tvPago.getSelectionModel().getSelectedItem().getNFactura()));
            cbxFPago.setValue(tvPago.getSelectionModel().getSelectedItem().getFPago());
            cbxBanco2.setValue(tvPago.getSelectionModel().getSelectedItem().getBanco());
            chkEstado.setSelected(tvPago.getSelectionModel().getSelectedItem().isEstado());
            dpFecha.setValue(
                    new Date(tvPago.getSelectionModel().getSelectedItem().getFVencimiento().getTime())
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        consultarEstados();
    }

    @FXML
    private void limProveedor(ActionEvent event) {
        txtFilProveedor.clear();
        mostrarDatosTV();
        consultarEstados();
    }

    @FXML
    private void limpiarFDesde(ActionEvent event) {
        dpDesde.setValue(null);
        consultarEstados();
    }

    @FXML
    private void limpiarFHasta(ActionEvent event) {
        dpHasta.setValue(null);
        consultarEstados();
    }

    @FXML
    private void agregar(ActionEvent event) {
        estado = Estado.ALTA;
        limpiarTexto();
        consultarEstados();
    }

    @FXML
    private void modificar(ActionEvent event) {
        estado = Estado.MODIFICACION;
        consultarEstados();
    }

    @FXML
    private void eliminar(ActionEvent event) {
        estado = Estado.INICIO;
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("¿Estás seguro que deseas eliminar el pago seleccioando?");
        Optional<ButtonType> seleccion = alerta.showAndWait();
        if (seleccion.isPresent() && seleccion.get() == ButtonType.OK) {
            servicio.eliminarPago(tvPago.getSelectionModel().getSelectedItem().getIdPago());
        }
        consultarEstados();
        mostrarDatosTV();
        limpiarTexto();
    }

    @FXML
    private void exportarCsv(MouseEvent event) throws IOException {
        if (!tvPago.getItems().isEmpty()) {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File ficheroSel = fc.showSaveDialog(btnCsv.getScene().getWindow());
            if (ficheroSel != null) {
                BufferedWriter bwCsv = new BufferedWriter(new FileWriter(ficheroSel));
                for (Pago p : tvPago.getItems()) {
                    bwCsv.write(p.getProveedor() + ";"
                            + nfMoneda.format(p.getImporte()) + ";"
                            + p.getNFactura() + ";"
                            + DateFormat.getDateInstance(DateFormat.SHORT).format(p.getFVencimiento()) + ";"
                            + p.getFPago() + ";"
                            + p.getBanco() + ";"
                            + (p.isEstado() ? "pagado" : "pendiente"));
                    bwCsv.newLine();
                }
                bwCsv.flush();
                bwCsv.close();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Exportar a CSV");
            alerta.setHeaderText("Error al exportar");
            alerta.setContentText("Este mensaje informa al usuario que no es posible exportar una tabla vacía.");
        }
    }

    @FXML
    private void proveedor(ActionEvent event) {
        consultarEstados();
    }

    @FXML
    private void importe(KeyEvent event) {
        consultarEstados();
    }

    @FXML
    private void nFactura(KeyEvent event) {
        consultarEstados();
    }

    @FXML
    private void formaPago(ActionEvent event) {
        consultarEstados();
    }

    @FXML
    private void banco(ActionEvent event) {
        consultarEstados();
    }

    @FXML
    private void fecha(ActionEvent event) {
        consultarEstados();
    }

    @FXML
    private void kpFecha(KeyEvent event) {
        consultarEstados();
    }

    @FXML
    private void krFProveedor(KeyEvent event) {
        mostrarDatosTV();
    }

    @FXML
    private void fechaDesde(ActionEvent event) {
        estado = Estado.INICIO;
        mostrarDatosTV();
        consultarEstados();
    }

    @FXML
    private void fechaHasta(ActionEvent event) {
        estado = Estado.INICIO;
        mostrarDatosTV();
        consultarEstados();
    }

    @FXML
    private void selecBanco(ActionEvent event) {
        estado = Estado.INICIO;
        consultarEstados();
        mostrarDatosTV();
    }

    @FXML
    private void selecEstado(ActionEvent event) {
        estado = Estado.INICIO;
        consultarEstados();
        mostrarDatosTV();
    }

    @FXML
    private void guardar(ActionEvent event) {
        almacenarInformacion();
    }

    @FXML
    private void seleccionTeclado(MouseEvent event) {
    }

    public void crearListas() {
        formasPago.add("Domiciliado");
        formasPago.add("Transferencia");
        formasPago.add("Tarjeta de crédito");
        estadosPago.put("Pagado", true);
        estadosPago.put("Pendiente", false);
    }

    private void mostrarDatosTV() {
        List<Pago> pagosFiltrados = servicio.getPagos();
        if (dpDesde.getValue() != null) {
            pagosFiltrados = servicio.getPagosDesde(pagosFiltrados, Date.from(dpDesde.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (dpHasta.getValue() != null) {
            pagosFiltrados = servicio.getPagosHasta(pagosFiltrados, Date.from(dpHasta.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (!txtFilProveedor.getText().isEmpty() && servicio.getProveedores().stream().anyMatch(p -> p.getNombreProveedor().equalsIgnoreCase(txtFilProveedor.getText()))) {
            pagosFiltrados = servicio.getPagosPorProveedor(pagosFiltrados, txtFilProveedor.getText());
        }
        if (cbxBanco.getValue().getIdBanco() != 0) {
            pagosFiltrados = servicio.getPagosPorBanco(pagosFiltrados, cbxBanco.getValue());
        }
        if (cbxEstado.getSelectionModel().getSelectedIndex() != 0) {
            pagosFiltrados = servicio.getPagosPorEstado(pagosFiltrados, estadosPago.get(cbxEstado.getValue()));
        }
        tvPago.setItems(FXCollections.observableList(pagosFiltrados));
        tvPago.getSortOrder().add(tcProv);
    }

    private void consultarEstados() {
        textopantallaprincipal.setText(estados.get(estado));
        btnAgregar.setDisable(estado == Estado.MODIFICACION || estado == Estado.ALTA);
        btnModificar.setDisable(estado != Estado.CONSULTA);
        btnEliminar.setDisable(estado != Estado.CONSULTA);
        btnLProveedor.setDisable(txtFilProveedor.getText().isEmpty());
        btnlimFDesde.setDisable(dpDesde.getValue() == null);
        btnlimFHasta.setDisable(dpHasta.getValue() == null);
        bloquearEdicion(estado == Estado.CONSULTA || estado == Estado.INICIO);
        btnGuardar.setDisable(estado == Estado.CONSULTA || estado == Estado.INICIO || validarDatos());
    }

    private void bloquearEdicion(boolean permiso) {
        cbxBanco2.setDisable(permiso);
        cbxProv.setDisable(permiso);
        cbxFPago.setDisable(permiso);
        txtImporte.setDisable(permiso);
        txtfactura.setDisable(permiso);
        chkEstado.setDisable(permiso);
        dpFecha.setDisable(permiso);
    }

    private void limpiarTexto() {
        cbxBanco2.setValue(null);
        cbxProv.setValue(null);
        cbxFPago.setValue(null);
        txtImporte.clear();
        txtfactura.clear();
        chkEstado.setSelected(false);
        dpFecha.setValue(null);
    }

    private void almacenarInformacion() {
        Pago pago = new Pago();
        pago.setBanco(cbxBanco2.getValue());
        pago.setEstado(chkEstado.isSelected());
        pago.setFPago(cbxFPago.getValue());
        pago.setFVencimiento(Date.from(dpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        try {
            Number n = nfDecimal.parse(txtImporte.getText().replaceAll("[^\\d,]", ""));
            pago.setImporte(n.doubleValue());
        } catch (ParseException ex) {}
        pago.setNFactura(Integer.parseInt(txtfactura.getText()));
        pago.setProveedor(cbxProv.getValue());
        if (estado == Estado.ALTA) {
            servicio.nuevoPago(pago);
        } else if (estado == Estado.MODIFICACION) {
            pago.setIdPago(idPago);
            servicio.modificarPago(pago);
            tvPago.refresh();
        }
        estado = Estado.INICIO;
        mostrarDatosTV();
        consultarEstados();
    }

    private boolean validarDatos() {
        return (cbxProv.getSelectionModel().getSelectedIndex() <= -1
                || (txtImporte.getText().isEmpty() || importeNoValido)
                || (txtfactura.getText().length() < digitosFactura || facturaNoValida)
                || cbxFPago.getSelectionModel().getSelectedIndex() <= -1
                || cbxBanco2.getSelectionModel().getSelectedIndex() <= -1
                || dpFecha.getValue() == null);
    }
}
