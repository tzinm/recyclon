package net.zabalburu.recyclon.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zabalburu.recyclon.dao.ClienteDAO;
import net.zabalburu.recyclon.modelo.Cliente;

public class ClienteOracle implements ClienteDAO {

    @Override
    public List<Cliente> getClientes() {
        List<Cliente> clientes = new ArrayList();
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("select * from clientes");
            while (rst.next()) {
                Cliente cliente = crearCliente(rst);
                clientes.add(cliente);
            }
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientes;
    }

    @Override
    public Cliente getCliente(int id) {
        Cliente cliente = null;
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("select * from clientes c where c.id=?");
            pst.setInt(1, id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                cliente = crearCliente(rst);
            }
            rst.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cliente;
    }

    @Override
    public Cliente nuevoCliente(Cliente c) {
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("select max(id) maxId from clientes");
            rst.next();
            c.setIdCliente(rst.getInt("maxId") + 1);
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("insert into clientes(id, nombre, ncuenta) values (?,?,?)");
            pst.setInt(1, c.getIdCliente());
            pst.setString(2, c.getNombrecliente());
            pst.setString(3, c.getNumCuenta());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            c = null;
        }
        return c;
    }

    @Override
    public void eliminarCliente(int id) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("delete from clientes where id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modificarCliente(Cliente c) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("update clientes set nombre=?,ncuenta=? where id=?");

            pst.setString(1, c.getNombrecliente());
            pst.setString(2, c.getNumCuenta());
            pst.setInt(3, c.getIdCliente());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Cliente crearCliente(ResultSet rst) {
        Cliente cliente = new Cliente();
        try {
            cliente.setIdCliente(rst.getInt("id"));
            cliente.setNombrecliente(rst.getString("nombre"));
            cliente.setNumCuenta(rst.getString("ncuenta"));
        } catch (SQLException ex) {
            Logger.getLogger(ClienteOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cliente;
    }

    @Override
    public int getCaracMaxCliente(String columna) {
        int num = 0;
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("SELECT DATA_LENGTH TAMAÑO FROM user_tab_columns "
                            + "WHERE table_name = 'CLIENTES' AND column_name = '" + columna + "'");
            rst.next();
            num = rst.getInt("tamaño");
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }
}
