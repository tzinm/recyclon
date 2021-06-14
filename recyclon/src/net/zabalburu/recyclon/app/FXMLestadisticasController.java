package net.zabalburu.recyclon.app;

import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import net.zabalburu.recyclon.modelo.Cobro;
import net.zabalburu.recyclon.modelo.Pago;
import net.zabalburu.recyclon.servicio.RecyclonServicio;

public class FXMLestadisticasController implements Initializable {

    private RecyclonServicio servicio = RecyclonServicio.getServicio();
    private static Date desde = null, hasta = null;

    @FXML
    private BorderPane borderPanePrincipal;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private BarChart<String, Number> bcCuentas;
    @FXML
    private PieChart pcClientes;
    @FXML
    private PieChart pcBancos;
    @FXML
    private PieChart pcProveedores;
    @FXML
    private Label lblpor;
    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private Button btnlimFDesde;
    @FXML
    private Button btnlimFHasta;
    @FXML
    private Label clientes;
    @FXML
    private Label proveedores;
    @FXML
    private Label bancos;
    @FXML
    private Label pRealizados;
    @FXML
    private Label cRealizados;
    @FXML
    private Label pPorEfectuar;
    @FXML
    private Label cPorEfectuar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graficoTarta();
        graficoBarras();
        datosGenerales();
        consultarEstado();
    }

    @FXML
    private void fechas(ActionEvent event) {
        if (dpDesde.getValue() != null) {
            this.desde = Date.from(dpDesde.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        if (dpHasta.getValue() != null) {
            this.hasta = Date.from(dpHasta.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        graficoTarta();
        graficoBarras();
        consultarEstado();
    }

    @FXML
    private void limpiarFDesde(ActionEvent event) {
        dpDesde.setValue(null);
        this.desde = null;
        graficoTarta();
        graficoBarras();
        consultarEstado();
    }

    @FXML
    private void limpiarFHasta(ActionEvent event) {
        dpHasta.setValue(null);
        this.hasta = null;
        graficoTarta();
        graficoBarras();
        consultarEstado();
    }

    @FXML
    private void mostrarPorcentajeB(MouseEvent event) {
        this.pcBancos.setLabelsVisible(true);
        if (this.pcBancos.getLabelsVisible()) {
            this.pcBancos.getData().forEach(d -> {
                Optional<Node> opTextNode = pcBancos.lookupAll(".chart-pie-label")
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

    @FXML
    private void mostrarPorcentajeC(MouseEvent event) {
        this.pcClientes.setLabelsVisible(true);
        if (this.pcClientes.getLabelsVisible()) {
            this.pcClientes.getData().forEach(d -> {
                Optional<Node> opTextNode = pcClientes.lookupAll(".chart-pie-label")
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

    @FXML
    private void mostrarPorcentajeP(MouseEvent event) {
        this.pcProveedores.setLabelsVisible(true);
        if (this.pcProveedores.getLabelsVisible()) {
            this.pcProveedores.getData().forEach(d -> {
                Optional<Node> opTextNode = pcProveedores.lookupAll(".chart-pie-label")
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

    private void datosGenerales() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        bancos.setText(nf.format(servicio.getBancos().size()));
        clientes.setText(nf.format(servicio.getClientes().size()));
        proveedores.setText(nf.format(servicio.getProveedores().size()));
        pRealizados.setText(nf.format(servicio.getPagos().stream().filter(b -> b.isEstado()).count()));
        cRealizados.setText(nf.format(servicio.getCobros().stream().filter(b -> b.isEstado()).count()));
        pPorEfectuar.setText(nf.format(servicio.getPagos().stream().filter(b -> !b.isEstado()).count()));
        cPorEfectuar.setText(nf.format(servicio.getCobros().stream().filter(b -> !b.isEstado()).count()));
    }

    private void graficoBarras() {
        bcCuentas.getData().clear();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        Double datob1 = 0.0;
        Double datob2 = 0.0;
        Double datoc2 = 0.0;
        Double datop2 = 0.0;
        xAxis.setLabel("Fecha");
        yAxis.setLabel("Euros");
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        XYChart.Series series4 = new XYChart.Series();
        series1.setName("Por cobrar");
        series2.setName("Por pagar");
        series3.setName("Cobrados");
        series4.setName("Pagados");
        if (desde != null && hasta == null) {
            datob1 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datob2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            datop2 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datoc2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
        } else if (hasta != null && desde == null) {
            datob1 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datob2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            datop2 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datoc2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
        } else if (hasta != null && desde != null) {
            datob1 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datob2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            datop2 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datoc2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
        } else {
            datob1 = servicio.getPagos().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datob2 = servicio.getCobros().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            datoc2 = servicio.getCobros().stream().filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            datop2 = servicio.getPagos().stream().filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
        }
        series1.getData().add(new XYChart.Data("Cuentas", datob1));
        series2.getData().add(new XYChart.Data("Cuentas", datob2));
        series3.getData().add(new XYChart.Data("Cuentas", datoc2));
        series4.getData().add(new XYChart.Data("Cuentas", datop2));
        bcCuentas.getData().addAll(series1, series2, series3, series4);
    }

    private void graficoTarta() {
        Double datob1 = 0.0;
        Double datob2 = 0.0;
        Double datoc1 = 0.0;
        Double datoc2 = 0.0;
        Double datop1 = 0.0;
        Double datop2 = 0.0;
        if (desde == null && hasta == null) {
            datob1 = servicio.getPagos().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
            datob2 = servicio.getCobros().stream().filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            datoc1 = datob2;
            datoc2 = servicio.getCobros().stream().filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            datop1 = datob1;
            datop2 = servicio.getPagos().stream().filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
        } else {
            if (desde != null && hasta == null) {
                datob1 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
                datob2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
                datop1 = datob1;
                datop2 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
                datoc1 = datob2;
                datoc2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            } else if (hasta != null && desde == null) {
                datob1 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
                datob2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
                datop1 = datob1;
                datop2 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
                datoc1 = datob2;
                datoc2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            } else if (hasta != null && desde != null) {
                datob1 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
                datob2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> !b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
                datop1 = datob1;
                datoc1 = datob2;
                datop2 = servicio.getPagos().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Pago::getImporte));
                datoc2 = servicio.getCobros().stream().filter(b -> b.getFVencimiento().after(desde) && b.getFVencimiento().before(hasta)).filter(b -> b.isEstado()).collect(Collectors.summingDouble(Cobro::getImporte));
            }
        }
        double fxp1 = ((datob1 * 100) / (datob1 + datob2));
        double fxc1 = ((datob2 * 100) / (datob1 + datob2));
        ObservableList<PieChart.Data> pieChartData1
                = FXCollections.observableArrayList(
                        new PieChart.Data("Facturas por pagar", Math.round(fxp1)),
                        new PieChart.Data("Facturas por cobrar", Math.round(fxc1)));
        this.pcBancos.setData(pieChartData1);
        this.pcBancos.setLabelsVisible(false);
        double fxp2 = ((datoc1 * 100) / (datoc1 + datoc2));
        double fxc2 = ((datoc2 * 100) / (datoc1 + datoc2));
        ObservableList<PieChart.Data> pieChartData2
                = FXCollections.observableArrayList(
                        new PieChart.Data("Facturas por cobrar", Math.round(fxp2)),
                        new PieChart.Data("Facturas cobradas", Math.round(fxc2)));
        this.pcClientes.setData(pieChartData2);
        this.pcClientes.setLabelsVisible(false);
        double fxp3 = ((datop1 * 100) / (datop1 + datop2));
        double fxc3 = ((datop2 * 100) / (datop1 + datop2));
        ObservableList<PieChart.Data> pieChartData3
                = FXCollections.observableArrayList(
                        new PieChart.Data("Facturas por pagar", Math.round(fxc1)),
                        new PieChart.Data("Facturas pagadas", Math.round(fxc3)));
        this.pcProveedores.setData(pieChartData3);
        this.pcProveedores.setLabelsVisible(false);
    }

    private void consultarEstado() {
        btnlimFDesde.setDisable(dpDesde.getValue() == null);
        btnlimFHasta.setDisable(dpHasta.getValue() == null);
    }

}
