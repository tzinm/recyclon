package net.zabalburu.recyclon.dao;

import java.util.List;
import net.zabalburu.recyclon.modelo.Proveedor;

public interface ProveedorDAO {
    List<Proveedor> getProveedores();
    Proveedor getProveedor(int id);
    Proveedor nuevoProveedor(Proveedor p);
    void eliminarProveedor(int id);
    void modificarProveedor(Proveedor p);
    int getCaracMaxProv(String columna);
}
