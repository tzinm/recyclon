package net.zabalburu.recyclon.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zabalburu.recyclon.dao.BancoDAO;
import net.zabalburu.recyclon.dao.ClienteDAO;
import net.zabalburu.recyclon.dao.CobroDAO;
import net.zabalburu.recyclon.dao.ProveedorDAO;
import net.zabalburu.recyclon.modelo.Cobro;
import net.zabalburu.recyclon.modelo.Pago;

public class CobroOracle implements CobroDAO {

    @Override
    public List<Cobro> getCobros() {
        List<Cobro> cobros = new ArrayList();
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("select * from cobros");
            while (rst.next()) {
                Cobro cobro = crearCobro(rst);
                cobros.add(cobro);
            }
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cobros;
    }

    @Override
    public Cobro getCobro(int idCobro) {
        Cobro cobro = new Cobro();
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("select * from cobros where id=?");
            pst.setInt(1, idCobro);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                cobro = crearCobro(rst);
            }
            rst.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cobro;
    }

    @Override
    public Cobro nuevoCobro(Cobro c) {
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("select max(id) maxId from cobros");
            rst.next();
            c.setIdCobro(rst.getInt("maxId") + 1);
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement(
                            "insert into cobros"
                            + "(id,idcliente,idbanco,importe,"
                            + "numfactura,fecha,formapago,estado)"
                            + "values (?,?,?,?,?,?,?,?)");
            pst.setInt(1, c.getIdCobro());
            pst.setInt(2, c.getCliente().getIdCliente());
            pst.setInt(3, c.getBanco().getIdBanco());
            pst.setDouble(4, c.getImporte());
            pst.setInt(5, c.getNFactura());
            pst.setDate(6, new java.sql.Date(c.getFVencimiento().getTime()));
            pst.setString(7, c.getFPago());
            pst.setInt(8, (c.isEstado() ? 1 : 0));
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    @Override
    public void eliminarCobro(int idCobro) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("delete from cobros where id=?");
            pst.setInt(1, idCobro);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modificarCobro(Cobro c) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("update cobros set idcliente=?,idbanco=?,"
                            + "importe=?,numfactura=?,"
                            + "fecha=?,formapago=?,estado=? "
                            + "where id=?");
            pst.setInt(1, c.getCliente().getIdCliente());
            pst.setInt(2, c.getBanco().getIdBanco());
            pst.setDouble(3, c.getImporte());
            pst.setInt(4, c.getNFactura());
            pst.setDate(5, new java.sql.Date(c.getFVencimiento().getTime()));
            pst.setString(6, c.getFPago());
            pst.setInt(7, (c.isEstado() ? 1 : 0));
            pst.setInt(8, c.getIdCobro());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Cobro crearCobro(ResultSet rst) {
        Cobro cobro = new Cobro();
        try {
            ClienteDAO cliente = new ClienteOracle();
            cobro.setIdCobro(rst.getInt("id"));
            cobro.setCliente(cliente.getCliente(rst.getInt("idcliente")));
            BancoDAO banco = new BancoOracle();
            cobro.setBanco(banco.getBanco(rst.getInt("idbanco")));
            cobro.setNCuentaCliente(cliente.getCliente(rst.getInt("idcliente")).getNumCuenta());
            cobro.setImporte(rst.getDouble("importe"));
            cobro.setNFactura(rst.getInt("numfactura"));
            cobro.setFVencimiento(rst.getDate("fecha"));
            cobro.setFPago(rst.getString("formapago"));
            cobro.setEstado(rst.getBoolean("estado"));
        } catch (SQLException ex) {
            Logger.getLogger(PagoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cobro;
    }

    @Override
    public int getCaracMaxCobro(String columna) {
        int num = 0;
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("SELECT data_length tamaño FROM user_tab_columns "
                            + "WHERE table_name = 'COBROS' AND column_name = '" + columna + "'");
            rst.next();
            num = rst.getInt("tamaño");
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(CobroOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }

    @Override
    public int getNumMaxCobro(String columna) {
        int num = 0;
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("SELECT data_precision tamaño FROM user_tab_columns "
                            + "WHERE table_name = 'COBROS' AND column_name = '" + columna + "'");
            rst.next();
            num = rst.getInt("tamaño");
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(CobroOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }

}
