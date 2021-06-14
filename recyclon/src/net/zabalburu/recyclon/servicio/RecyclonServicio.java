package net.zabalburu.recyclon.servicio;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import net.zabalburu.recyclon.dao.BancoDAO;
import net.zabalburu.recyclon.dao.ClienteDAO;
import net.zabalburu.recyclon.dao.CobroDAO;
import net.zabalburu.recyclon.dao.PagoDAO;
import net.zabalburu.recyclon.dao.ProveedorDAO;
import net.zabalburu.recyclon.modelo.Banco;
import net.zabalburu.recyclon.modelo.Cliente;
import net.zabalburu.recyclon.modelo.Cobro;
import net.zabalburu.recyclon.modelo.Pago;
import net.zabalburu.recyclon.modelo.Proveedor;
import net.zabalburu.recyclon.util.BancoOracle;
import net.zabalburu.recyclon.util.ClienteOracle;
import net.zabalburu.recyclon.util.CobroOracle;
import net.zabalburu.recyclon.util.PagoOracle;
import net.zabalburu.recyclon.util.ProveedorOracle;

public class RecyclonServicio {

    public static RecyclonServicio getServicio() {
        return new RecyclonServicio();
    }

    private RecyclonServicio() {
    }

    private BancoDAO bancoDao = new BancoOracle();
    private ClienteDAO clienteDao = new ClienteOracle();
    private ProveedorDAO proveedorDao = new ProveedorOracle();
    private PagoDAO pagoDao = new PagoOracle();
    private CobroDAO cobroDao = new CobroOracle();

    public List<Banco> getBancos() {
        return bancoDao.getBancos();
    }

    public Banco getBanco(Integer id) {
        return bancoDao.getBanco(id);
    }

    public Banco nuevoBanco(Banco b) {
        return bancoDao.nuevoBanco(b);
    }

    public void eliminarBanco(int idBanco) {
        bancoDao.eliminarBanco(idBanco);
    }

    public void modificarBanco(Banco b) {
        bancoDao.modificarBanco(b);
    }

    public int getCaracMaxBanco(String columna) {
        return bancoDao.getCaracMaxBanco(columna);
    }

    public List<Cliente> getClientes() {
        return clienteDao.getClientes();
    }

    public Cliente getCliente(int id) {
        return clienteDao.getCliente(id);
    }

    public Cliente nuevoCliente(Cliente c) {
        return clienteDao.nuevoCliente(c);
    }

    public void eliminarCliente(int id) {
        clienteDao.eliminarCliente(id);
    }

    public void modificarCliente(Cliente c) {
        clienteDao.modificarCliente(c);
    }

    public int getCaracMaxCliente(String columna) {
        return clienteDao.getCaracMaxCliente(columna);
    }

    public List<Pago> getPagos() {
        return pagoDao.getPagos();
    }

    public Pago getPago(int idPago) {
        return pagoDao.getPago(idPago);
    }

    public Pago nuevoPago(Pago p) {
        return pagoDao.nuevoPago(p);
    }

    public void eliminarPago(int idPago) {
        pagoDao.eliminarPago(idPago);
    }

    public void modificarPago(Pago p) {
        pagoDao.modificarPago(p);
    }

    public int getCaracMaxPago(String columna) {
        return pagoDao.getCaracMaxPago(columna);
    }

    public int getNumMaxPago(String columna) {
        return pagoDao.getNumMaxPago(columna);
    }

    public List<Proveedor> getProveedores() {
        return proveedorDao.getProveedores();
    }

    public Proveedor getProveedor(int id) {
        return proveedorDao.getProveedor(id);
    }

    public Proveedor nuevoProveedor(Proveedor p) {
        return proveedorDao.nuevoProveedor(p);
    }

    public void eliminarProveedor(int id) {
        proveedorDao.eliminarProveedor(id);
    }

    public void modificarProveedor(Proveedor p) {
        proveedorDao.modificarProveedor(p);
    }

    public int getCaracMaxProv(String columna) {
        return proveedorDao.getCaracMaxProv(columna);
    }

    public List<Cobro> getCobros() {
        return cobroDao.getCobros();
    }

    public Cobro getCobro(int idCobro) {
        return cobroDao.getCobro(idCobro);
    }

    public Cobro nuevoCobro(Cobro c) {
        return cobroDao.nuevoCobro(c);
    }

    public void eliminarCobro(int idCobro) {
        cobroDao.eliminarCobro(idCobro);
    }

    public void modificarCobro(Cobro c) {
        cobroDao.modificarCobro(c);
    }

    public int getCaracMaxCobro(String columna) {
        return cobroDao.getCaracMaxCobro(columna);
    }

    public int getNumMaxCobro(String columna) {
        return cobroDao.getNumMaxCobro(columna);
    }

    public List<Pago> getPagosPorProveedor(List<Pago> pagos, String nProveedor) {
        return pagos.stream()
                .filter(p -> p.getProveedor().getNombreProveedor().equalsIgnoreCase(nProveedor))
                .collect(Collectors.toList());
    }

    public List<Cobro> getCobrosPorCliente(List<Cobro> cobros, String nCliente) {
        return cobros.stream()
                .filter(c -> c.getCliente().getNombrecliente().equalsIgnoreCase(nCliente))
                .collect(Collectors.toList());
    }

    public List<Pago> getPagosPorBanco(List<Pago> pagos, Banco b) {
        return pagos.stream()
                .filter(p -> p.getBanco().equals(b))
                .collect(Collectors.toList());
    }

    public List<Cobro> getCobrosPorBanco(List<Cobro> cobros, Banco b) {
        return cobros.stream()
                .filter(c -> c.getBanco().equals(b))
                .collect(Collectors.toList());
    }

    public List<Pago> getPagosPorEstado(List<Pago> pagos, Boolean estado) {
        return pagos.stream()
                .filter(p -> p.isEstado() == estado)
                .collect(Collectors.toList());
    }

    public List<Cobro> getCobroPorEstado(List<Cobro> cobros, Boolean estado) {
        return cobros.stream()
                .filter(c -> c.isEstado() == estado)
                .collect(Collectors.toList());
    }

    public List<Pago> getPagosDesde(List<Pago> pagos, Date desde) {
        return pagos.stream()
                .filter(p -> p.getFVencimiento().compareTo(desde) >= 0)
                .collect(Collectors.toList());
    }

    public List<Pago> getPagosHasta(List<Pago> pagos, Date hasta) {
        GregorianCalendar gcHasta = new GregorianCalendar();
        gcHasta.setTime(hasta);
        gcHasta.set(GregorianCalendar.DATE, gcHasta.get(GregorianCalendar.DATE) + 1);
        return pagos.stream()
                .filter(p -> p.getFVencimiento().before(gcHasta.getTime()))
                .collect(Collectors.toList());
    }

    public List<Cobro> getCobrosDesde(List<Cobro> cobros, Date desde) {
        return cobros.stream()
                .filter(c -> c.getFVencimiento().compareTo(desde) >= 0)
                .collect(Collectors.toList());
    }

    public List<Cobro> getCobrosHasta(List<Cobro> cobros, Date hasta) {
        return cobros.stream()
                .filter(c -> c.getFVencimiento().compareTo(hasta) <= 0)
                .collect(Collectors.toList());
    }
}
