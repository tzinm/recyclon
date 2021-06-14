package net.zabalburu.recyclon.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zabalburu.recyclon.dao.BancoDAO;
import net.zabalburu.recyclon.dao.PagoDAO;
import net.zabalburu.recyclon.dao.ProveedorDAO;
import net.zabalburu.recyclon.modelo.Pago;

public class PagoOracle implements PagoDAO {

    @Override
    public List<Pago> getPagos() {
        List<Pago> pagos = new ArrayList();
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("select * from pagos");
            while (rst.next()) {
                Pago pago = crearPago(rst);
                pagos.add(pago);
            }
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pagos;
    }

    @Override
    public Pago getPago(int idPago) {
        Pago pago = new Pago();
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("select * from pagos where id=?");
            pst.setInt(1, idPago);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                pago = crearPago(rst);
            }
            rst.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pago;
    }

    @Override
    public Pago nuevoPago(Pago p) {
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("select max(id) maxId from pagos");
            rst.next();
            p.setIdPago(rst.getInt("maxId") + 1);
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement(
                            "insert into pagos"
                            + "(id,idproveedor,importe,numfactura,"
                            + "fechavencimiento,formapago,idbanco,estado)"
                            + "values (?,?,?,?,?,?,?,?)");
            pst.setInt(1, p.getIdPago());
            pst.setInt(2, p.getProveedor().getIdProveedor());
            pst.setDouble(3, p.getImporte());
            pst.setInt(4, p.getNFactura());
            pst.setDate(5, new java.sql.Date(p.getFVencimiento().getTime()));
            pst.setString(6, p.getFPago());
            pst.setInt(7, p.getBanco().getIdBanco());
            pst.setInt(8, (p.isEstado() ? 1 : 0));
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    @Override
    public void eliminarPago(int idPago) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("delete from pagos where id=?");
            pst.setInt(1, idPago);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modificarPago(Pago p) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("update pagos set idproveedor=?,idbanco=?,importe=?,"
                            + "numfactura=?,fechavencimiento=?,formapago=?,estado=? "
                            + "where id=?");
            pst.setInt(1, p.getProveedor().getIdProveedor());
            pst.setInt(2, p.getBanco().getIdBanco());
            pst.setDouble(3, p.getImporte());
            pst.setInt(4, p.getNFactura());
            pst.setDate(5, new java.sql.Date(p.getFVencimiento().getTime()));
            pst.setString(6, p.getFPago());
            pst.setInt(7, (p.isEstado() ? 1 : 0));
            pst.setInt(8, p.getIdPago());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Pago crearPago(ResultSet rst) {
        Pago pago = new Pago();
        try {
            ProveedorDAO proveedor = new ProveedorOracle();
            pago.setIdPago(rst.getInt("id"));
            pago.setProveedor(proveedor.getProveedor(rst.getInt("idproveedor")));
            pago.setImporte(rst.getDouble("importe"));
            pago.setNFactura(rst.getInt("numfactura"));
            pago.setFVencimiento(rst.getDate("fechavencimiento"));
            pago.setFPago(rst.getString("formapago"));
            BancoDAO banco = new BancoOracle();
            pago.setBanco(banco.getBanco(rst.getInt("idbanco")));
            pago.setEstado(rst.getBoolean("estado"));
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pago;
    }

    @Override
    public int getCaracMaxPago(String columna) {
        int num = 0;
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("SELECT data_length tamaño FROM user_tab_columns "
                            + "WHERE table_name = 'PAGOS' AND column_name = '" + columna + "'");
            rst.next();
            num = rst.getInt("tamaño");
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }

    @Override
    public int getNumMaxPago(String columna) {
        int num = 0;
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("SELECT data_precision tamaño FROM user_tab_columns "
                            + "WHERE table_name = 'PAGOS' AND column_name = '" + columna + "'");
            rst.next();
            num = rst.getInt("tamaño");
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }
}
