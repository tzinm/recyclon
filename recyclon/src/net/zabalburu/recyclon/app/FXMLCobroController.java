package net.zabalburu.recyclon.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import net.zabalburu.recyclon.modelo.Banco;
import net.zabalburu.recyclon.modelo.Cliente;
import net.zabalburu.recyclon.modelo.Cobro;
import net.zabalburu.recyclon.servicio.RecyclonServicio;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class FXMLCobroController implements Initializable {

    private RecyclonServicio servicio = RecyclonServicio.getServicio();

    private enum Estado {
        INICIO, CONSULTA, MODIFICACION, ALTA
    }
    private Estado estado = Estado.INICIO;
    private Map<String, Boolean> estadosCobros = new HashMap<>();
    private List<String> formasPago = new ArrayList<String>();
    private PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    private final int digitosFactura = servicio.getNumMaxCobro("NUMFACTURA");
    private final int digitosImporte = servicio.getNumMaxCobro("IMPORTE");
    private boolean facturaNoValida = false;
    private boolean importeNovalido = false;
    private int idCobro;

    private NumberFormat nfMoneda = NumberFormat.getCurrencyInstance();
    private NumberFormat nfDecimal = NumberFormat.getInstance();

    @FXML
    private TextField txtFCliente;
    @FXML
    private Button btnFCliente;
    @FXML
    private Button btnLFDesde;
    @FXML
    private Button btnLFHasta;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private AnchorPane panelInfo;
    @FXML
    private Text textopantallaprincipal;
    @FXML
    private TextField txtImporte;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnExportar;
    @FXML
    private ComboBox<Cliente> cbxCliente;
    @FXML
    private TextField txtFactura;
    @FXML
    private ComboBox<String> cbxFormaPago;
    @FXML
    private ComboBox<Banco> cbxBanco;
    @FXML
    private CheckBox chkEstado;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private ComboBox<Banco> cbxFiltroBanco;
    @FXML
    private ComboBox<String> cbxFiltroEstado;
    @FXML
    private TableView<Cobro> tvCobros;
    @FXML
    private TableColumn<Cobro, String> tcCliente;
    @FXML
    private TableColumn<Cobro, Integer> tcNFactura;
    @FXML
    private TableColumn<Cobro, String> tcNCuenta;
    @FXML
    private TableColumn<Cobro, Date> tcFecha;
    @FXML
    private TableColumn<Cobro, String> tcFormaPago;
    @FXML
    private TableColumn<Cobro, String> tcBanco;
    @FXML
    private TableColumn<Cobro, Boolean> tcCobrado;
    @FXML
    private TableColumn<Cobro, Double> tcImporte;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private Button btnGuardar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estadosCobros.put("Cobrado", true);
        estadosCobros.put("Pendiente", false);
        formasPago.add("Transferencia");
        formasPago.add("Domiciliado");
        formasPago.add("Tarjeta de crédito");
        nfMoneda.setMinimumFractionDigits(2);
        nfMoneda.setMaximumFractionDigits(2);
        nfDecimal.setMaximumFractionDigits(2);
        AutoCompletionBinding<Cliente> acbCliente = TextFields.bindAutoCompletion(txtFCliente, servicio.getClientes());
        acbCliente.setOnAutoCompleted(evt -> {
            mostrarDatos();
            cambiarEstadoBotones();
        });
        tcCliente.setCellValueFactory(new PropertyValueFactory<Cobro, String>("cliente"));
        tcBanco.setCellValueFactory(new PropertyValueFactory<Cobro, String>("banco"));
        tcNCuenta.setCellValueFactory(new PropertyValueFactory<Cobro, String>("NCuentaCliente"));
        tcImporte.setCellValueFactory(new PropertyValueFactory<Cobro, Double>("importe"));
        tcImporte.setCellFactory(columna -> {
            return new TableCell<Cobro, Double>() {
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
        tcNFactura.setCellValueFactory(new PropertyValueFactory<Cobro, Integer>("NFactura"));
        tcNFactura.setStyle("-fx-alignment: CENTER;");
        tcFecha.setCellValueFactory(new PropertyValueFactory<Cobro, Date>("FVencimiento"));
        tcFecha.setCellFactory(columna -> {
            return new TableCell<Cobro, Date>() {
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
        tcFormaPago.setCellValueFactory(new PropertyValueFactory<Cobro, String>("FPago"));
        tcCobrado.setCellValueFactory(new PropertyValueFactory<Cobro, Boolean>("estado"));
        tcCobrado.setCellFactory(columna -> {
            return new TableCell<Cobro, Boolean>() {
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
                        this.setAlignment(Pos.CENTER);
                    } else {
                        this.setGraphic(null);
                    }
                }
            };
        });
        List<Banco> bancos = servicio.getBancos();
        bancos.sort((b1, b2) -> b1.getNombreBanco().compareToIgnoreCase(b2.getNombreBanco()));
        cbxFiltroBanco.setItems(FXCollections.observableArrayList(bancos));
        Banco banco = new Banco("Todos");
        banco.setIdBanco(0);
        cbxFiltroBanco.getItems().add(0, banco);
        cbxFiltroBanco.getSelectionModel().selectFirst();
        List<String> estados = estadosCobros.keySet().stream().collect(Collectors.toList());
        estados.sort((e1, e2) -> e1.compareToIgnoreCase(e2));
        cbxFiltroEstado.setItems(FXCollections.observableArrayList(estados));
        cbxFiltroEstado.getItems().add(0, "Todos");
        cbxFiltroEstado.getSelectionModel().selectFirst();
        txtFactura.textProperty().addListener(cambio -> {
            facturaNoValida = !txtFactura.getText().isEmpty() && !txtFactura.getText().matches("\\d{" + digitosFactura + "}");
            txtFactura.pseudoClassStateChanged(errorClass, facturaNoValida);
        });
        txtFactura.setAlignment(Pos.CENTER_RIGHT);
        txtFactura.setTextFormatter(new TextFormatter<String>(cambio -> {
            if (cambio.getText().matches("\\d*") && cambio.getControlNewText().length() <= digitosFactura) {
                return cambio;
            } else {
                cambio.setText("");
                cambio.setRange(cambio.getRangeStart(), cambio.getRangeStart());
                return cambio;
            }
        }));
        txtImporte.textProperty().addListener(cambio -> {
            importeNovalido = !txtImporte.getText().isEmpty()
                    && !txtImporte.getText().matches("^\\d{1,8}(,\\d{1,})?(\\s)?(\\u20AC)?");
            txtImporte.pseudoClassStateChanged(errorClass, importeNovalido);
        });
        txtImporte.setTextFormatter(new TextFormatter<String>(cambio -> {
            if ((cambio.getText().matches("\\d*") && cambio.getText().matches("[^,]")
                    && cambio.getControlNewText().length() == digitosImporte + 1)
                    || cambio.getText().matches("[a-zA-Z]")) {
                cambio.setText("");
                cambio.setRange(cambio.getRangeStart(), cambio.getRangeStart());
                return cambio;
            } else {
                return cambio;
            }
        }));
        cbxCliente.setItems(FXCollections.observableArrayList(servicio.getClientes())
                .sorted((c1, c2) -> c1.getNombrecliente().compareToIgnoreCase(c2.getNombrecliente())));
        cbxBanco.setItems(FXCollections.observableArrayList(servicio.getBancos())
                .sorted((b1, b2) -> b1.getNombreBanco().compareToIgnoreCase(b2.getNombreBanco())));
        cbxFormaPago.setItems(FXCollections.observableArrayList(formasPago)
                .sorted((fp1, fp2) -> fp1.compareToIgnoreCase(fp2)));
        mostrarDatos();
        cambiarEstadoBotones();
    }

    @FXML
    private void pulsarTecla(KeyEvent event) {
        if (estado == Estado.CONSULTA && event.getCode().equals(KeyCode.ESCAPE)) {
            estado = Estado.INICIO;
            cambiarEstadoBotones();
        } else if (!btnGuardar.isDisable() && event.getCode().equals(KeyCode.ENTER)) {
            almacenarInformacion();
        }
    }

    @FXML
    private void agregarPago(ActionEvent event) {
        estado = Estado.ALTA;
        cambiarEstadoBotones();
        limpiarCampos();
    }

    @FXML
    private void modificarPago(ActionEvent event) {
        estado = Estado.MODIFICACION;
        cambiarEstadoBotones();
    }

    @FXML
    private void eliminarPago(ActionEvent event) {
        estado = Estado.INICIO;
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("¿Está seguro que desea eliminar el cobro seleccionado?");
        Optional<ButtonType> seleccion = alerta.showAndWait();
        if (seleccion.isPresent() && seleccion.get() == ButtonType.OK) {
            servicio.eliminarCobro(tvCobros.getSelectionModel().getSelectedItem().getIdCobro());
        }
        cambiarEstadoBotones();
        mostrarDatos();
        limpiarCampos();
    }

    @FXML
    private void selCliente(ActionEvent event) {
        cambiarEstadoBotones();
    }

    @FXML
    private void selImporte(KeyEvent event) {
        cambiarEstadoBotones();
    }

    @FXML
    private void selNFactura(KeyEvent event) {
        cambiarEstadoBotones();
    }

    @FXML
    private void selCbxBanco(ActionEvent event) {
        cambiarEstadoBotones();
    }

    @FXML
    private void selFecha(ActionEvent event) {
        cambiarEstadoBotones();
    }

    @FXML
    private void selFormaPago(ActionEvent event) {
        cambiarEstadoBotones();
    }

    @FXML
    private void selChkEstado(ActionEvent event) {
    }

    @FXML
    private void selFDesde(ActionEvent event) {
        estado = Estado.INICIO;
        cambiarEstadoBotones();
        mostrarDatos();
    }

    @FXML
    private void limpiarFDesde(MouseEvent event) {
        dpDesde.setValue(null);
    }

    @FXML
    private void selFHasta(ActionEvent event) {
        estado = Estado.INICIO;
        cambiarEstadoBotones();
        mostrarDatos();
    }

    @FXML
    private void limpiarFHasta(MouseEvent event) {
        dpHasta.setValue(null);
    }

    @FXML
    private void selFiltroBanco(ActionEvent event) {
        estado = Estado.INICIO;
        cambiarEstadoBotones();
        mostrarDatos();
    }

    @FXML
    private void limpiarFCliente(ActionEvent event) {
        txtFCliente.clear();
        mostrarDatos();
    }

    @FXML
    private void selFiltroEstado(ActionEvent event) {
        estado = Estado.INICIO;
        cambiarEstadoBotones();
        mostrarDatos();
    }

    @FXML
    private void selecCobro(MouseEvent event) {
        estado = Estado.CONSULTA;
        if (!tvCobros.getSelectionModel().isEmpty()) {
            rellenarCampos();
        }
        cambiarEstadoBotones();
    }

    @FXML
    private void krFCliente(KeyEvent event) {
        mostrarDatos();
    }

    @FXML
    private void guardar(ActionEvent event) {
        almacenarInformacion();
    }

    @FXML
    private void exportarCsv(MouseEvent event) {
        //https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
        if (!tvCobros.getItems().isEmpty()) {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File ficheroSel = fc.showSaveDialog(btnExportar.getScene().getWindow());
            if (ficheroSel != null) {
                try {
                    BufferedWriter bwCsv = new BufferedWriter(new FileWriter(ficheroSel));
                    for (Cobro c : tvCobros.getItems()) {
                        bwCsv.write(c.getCliente() + ";"
                                + nfMoneda.format(c.getImporte()) + ";"
                                + c.getNFactura() + ";"
                                + (c.getNCuentaCliente() != null ? c.getNCuentaCliente() : "") + ";"
                                + DateFormat.getDateInstance(DateFormat.SHORT).format(c.getFVencimiento()) + ";"
                                + c.getFPago() + ";"
                                + c.getBanco() + ";"
                                + (c.isEstado() ? "cobrado" : "pendiente"));
                        bwCsv.newLine();
                    }
                    bwCsv.flush();
                    bwCsv.close();
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
            }
        } else {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Exportar a CSV");
            alerta.setHeaderText("Error al exportar");
            alerta.setContentText("Este mensaje informa al usuario que no es posible exportar una tabla vacía.");
            alerta.showAndWait();
        }
    }

    private void mostrarDatos() {
        List<Cobro> cobrosFiltrados = servicio.getCobros();
        if (dpDesde.getValue() != null) {
            cobrosFiltrados = servicio.getCobrosDesde(cobrosFiltrados, Date.from(dpDesde.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (dpHasta.getValue() != null) {
            cobrosFiltrados = servicio.getCobrosHasta(cobrosFiltrados, Date.from(dpHasta.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (!txtFCliente.getText().isEmpty() && servicio.getCobros().stream().anyMatch(c -> c.getCliente().getNombrecliente().equalsIgnoreCase(txtFCliente.getText()))) {
            cobrosFiltrados = servicio.getCobrosPorCliente(cobrosFiltrados, txtFCliente.getText());
        }
        if (cbxFiltroBanco.getValue().getIdBanco() != 0) {
            cobrosFiltrados = servicio.getCobrosPorBanco(cobrosFiltrados, cbxFiltroBanco.getValue());
        }
        if (cbxFiltroEstado.getSelectionModel().getSelectedIndex() != 0) {
            cobrosFiltrados = servicio.getCobroPorEstado(cobrosFiltrados, estadosCobros.get(cbxFiltroEstado.getValue()));
        }
        tvCobros.setItems(FXCollections.observableList(cobrosFiltrados));
        tvCobros.getSortOrder().add(tcCliente);
    }

    private void cambiarEstadoBotones() {
        btnLFDesde.setDisable(dpDesde.getValue() == null);
        btnLFHasta.setDisable(dpHasta.getValue() == null);
        btnFCliente.setDisable(txtFCliente.getText().isEmpty());
        btnGuardar.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA || validarDatos());
        btnModificar.setDisable(estado != Estado.CONSULTA);
        btnEliminar.setDisable(estado != Estado.CONSULTA);
        btnNuevo.setDisable(estado == Estado.ALTA || estado == Estado.MODIFICACION);
        cbxCliente.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        cbxFormaPago.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        cbxBanco.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        txtFactura.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        txtImporte.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        dpFecha.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
        chkEstado.setDisable(estado == Estado.INICIO || estado == Estado.CONSULTA);
    }

    private void limpiarCampos() {
        cbxCliente.setValue(null);
        cbxFormaPago.setValue(null);
        cbxBanco.setValue(null);
        txtFactura.clear();
        txtImporte.clear();
        dpFecha.setValue(null);
        chkEstado.setSelected(false);
    }

    private void rellenarCampos() {
        cbxCliente.setValue(tvCobros.getSelectionModel().getSelectedItem().getCliente());
        cbxFormaPago.setValue(tvCobros.getSelectionModel().getSelectedItem().getFPago());
        cbxBanco.setValue(tvCobros.getSelectionModel().getSelectedItem().getBanco());
        txtFactura.setText(Integer.toString(tvCobros.getSelectionModel().getSelectedItem().getNFactura()));
        txtImporte.setText(nfMoneda.format(tvCobros.getSelectionModel().getSelectedItem().getImporte()));
        dpFecha.setValue(new Date(tvCobros.getSelectionModel().getSelectedItem().getFVencimiento().getTime())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        chkEstado.setSelected(tvCobros.getSelectionModel().getSelectedItem().isEstado());
        idCobro = tvCobros.getSelectionModel().getSelectedItem().getIdCobro();
    }

    private boolean validarDatos() {
        return (cbxCliente.getSelectionModel().getSelectedIndex() <= -1
                || cbxFormaPago.getSelectionModel().getSelectedIndex() <= -1
                || cbxCliente.getSelectionModel().getSelectedIndex() <= -1
                || (txtFactura.getText().length() < digitosFactura || facturaNoValida)
                || (txtImporte.getText().isEmpty() || importeNovalido)
                || dpFecha.getValue() == null);
    }

    private void almacenarInformacion() {
        Cobro c = new Cobro();
        c.setBanco(cbxBanco.getSelectionModel().getSelectedItem());
        c.setCliente(cbxCliente.getSelectionModel().getSelectedItem());
        c.setEstado(chkEstado.isSelected());
        c.setFPago(cbxFormaPago.getSelectionModel().getSelectedItem());
        //https://stackoverflow.com/a/40143687/12868523
        c.setFVencimiento(Date.from(dpFecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        try {
            Number n = nfDecimal.parse(txtImporte.getText().replaceAll("[^\\d,]", ""));
            c.setImporte(n.doubleValue());
        } catch (ParseException ex) {
            Logger.getLogger(FXMLCobroController.class.getName()).log(Level.SEVERE, null, ex);
        }
        c.setNFactura(Integer.parseInt(txtFactura.getText()));
        if (estado == Estado.ALTA) {
            servicio.nuevoCobro(c);
        } else if (estado == Estado.MODIFICACION) {
            c.setIdCobro(idCobro);
            servicio.modificarCobro(c);
            tvCobros.refresh();
        }
        estado = Estado.INICIO;
        mostrarDatos();
        cambiarEstadoBotones();
    }
}
