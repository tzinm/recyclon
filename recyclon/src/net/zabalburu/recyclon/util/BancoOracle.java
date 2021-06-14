package net.zabalburu.recyclon.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zabalburu.recyclon.dao.BancoDAO;
import net.zabalburu.recyclon.modelo.Banco;

public class BancoOracle implements BancoDAO {

    @Override
    public List<Banco> getBancos() {
        List<Banco> bancos = new ArrayList();
        try {    
            Statement stmt = Conexion.getConexion().createStatement();
            ResultSet rst = stmt.executeQuery("select * from bancos b order by id");
            while (rst.next()) {
                Banco banco = crearBanco(rst);
                bancos.add(banco);
            }
            rst.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bancos;
    }

    @Override
    public Banco getBanco(Integer id) {
        Banco banco = null;
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("select * from bancos b where b.id=?");
            pst.setInt(1, id);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                banco = crearBanco(rst);
            }
            rst.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return banco;
    }

    @Override
    public Banco nuevoBanco(Banco b) {
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("select max(id) maxId from bancos");
            rst.next();
            b.setIdBanco(rst.getInt("maxId")+1);
            rst.close();
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("insert into bancos (id, nombre) values (?,?)");
            pst.setInt(1, b.getIdBanco());
            pst.setString(2, b.getNombreBanco());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            b = null;
        }
        return b;
    }

    @Override
    public void eliminarBanco(int idBanco) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("delete from bancos where id=?");
            pst.setInt(1, idBanco);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modificarBanco(Banco b) {
        try {
            PreparedStatement pst = Conexion.getConexion()
                    .prepareStatement("update bancos set nombre=? where id=?");
            pst.setString(1, b.getNombreBanco());
            pst.setInt(2, b.getIdBanco());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Banco crearBanco(ResultSet rst) {
        Banco banco = new Banco();
        try {
            banco.setIdBanco(rst.getInt("id"));
            banco.setNombreBanco(rst.getString("nombre"));
        } catch (SQLException ex) {
            Logger.getLogger(BancoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return banco;
    }

    @Override
    public int getCaracMaxBanco(String columna) {
        int num = 0;
        try {
            ResultSet rst = Conexion.getConexion().createStatement()
                    .executeQuery("SELECT DATA_LENGTH TAMAÑO FROM user_tab_columns "
                            + "WHERE table_name = 'BANCOS' AND column_name = '" + columna + "'");
            rst.next();
            num = rst.getInt("tamaño");
            rst.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }    
}

