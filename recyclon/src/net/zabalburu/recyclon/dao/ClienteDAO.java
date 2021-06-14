package net.zabalburu.recyclon.dao;

import java.util.List;
import net.zabalburu.recyclon.modelo.Cliente;

public interface ClienteDAO {
    List<Cliente> getClientes();
    Cliente getCliente(int id);
    Cliente nuevoCliente(Cliente c);
    void eliminarCliente(int id);
    void modificarCliente(Cliente c);
    int getCaracMaxCliente(String columna);
}