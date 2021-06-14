package net.zabalburu.recyclon.dao;

import java.util.List;
import net.zabalburu.recyclon.modelo.Pago;

public interface PagoDAO {
    List<Pago> getPagos();
    Pago getPago(int idPago);
    Pago nuevoPago(Pago p);
    void eliminarPago(int idPago);
    void modificarPago(Pago pago);
    int getCaracMaxPago(String columna);
    int getNumMaxPago(String columna);
}
