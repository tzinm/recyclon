package net.zabalburu.recyclon.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zabalburu.recyclon.dao.ProveedorDAO;
import net.zabalburu.recyclon.modelo.Proveedor;

public class ProveedorOracle implements ProveedorDAO {

    @Override
    public List<Proveedor> getProveedores() {
        List<Proveedor> proveedores = new ArrayList();
        try {
            Statement stmt = Conexion.getConexion().createStatement();
            ResultSet rst = stmt.executeQuery("select * from proveedores");
            while (rst.next()) {
                Proveedor proveedor = crearProveedor(rst);
                proveedores.add(proveedor);
            }
            rst.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return proveedores;
    }

    @Override
    public Proveedor getProveedor(int id) {
        Proveedor proveedor = null;
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("select * from proveedores where id=?");
            pst.setInt(1, id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                proveedor = crearProveedor(rst);
            }
            rst.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return proveedor;
    }

    @Override
    public Proveedor nuevoProveedor(Proveedor p) {
        try {
            Statement stmt = Conexion.getConexion().createStatement();
            ResultSet rst = stmt.executeQuery("select max(id) maxId from proveedores");
            rst.next();
            p.setIdProveedor(rst.getInt("maxId")+1);
            rst.close();
            stmt.close();
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("insert into proveedores(id,nombre) values (?,?)");
            pst.setInt(1, p.getIdProveedor());
            pst.setString(2, p.getNombreProveedor());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            p = null;
        }
        return p;
    }

    @Override
    public void eliminarProveedor(int id) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("delete from proveedores where id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modificarProveedor(Proveedor p) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("update proveedores set nombre=? where id=?");
            pst.setString(1, p.getNombreProveedor());
            pst.setInt(2, p.getIdProveedor());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Proveedor crearProveedor(ResultSet rst) {
        Proveedor proveedor = new Proveedor();
        try {
            proveedor.setIdProveedor(rst.getInt("id"));
            proveedor.setNombreProveedor(rst.getString("nombre"));
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return proveedor;
    }

    @Override
    public int getCaracMaxProv(String columna) {
        int num = 0;
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("SELECT DATA_LENGTH TAMAÑO FROM user_tab_columns "
                            + "WHERE table_name = 'BANCOS' AND column_name = '" + columna + "'");
            rst.next();
            num = rst.getInt("tamaño");
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }    
}

