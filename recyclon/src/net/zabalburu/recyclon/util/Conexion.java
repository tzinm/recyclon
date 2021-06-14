package net.zabalburu.recyclon.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zabalburu.recyclon.modelo.Banco;
import net.zabalburu.recyclon.modelo.Cliente;
import net.zabalburu.recyclon.modelo.Cobro;
import net.zabalburu.recyclon.modelo.Pago;
import net.zabalburu.recyclon.modelo.Proveedor;
import net.zabalburu.recyclon.servicio.RecyclonServicio;
import oracle.jdbc.pool.OracleDataSource;

public class Conexion {

    private static Connection cnn;

    public static Connection getConexion() {
        if (cnn == null) {
            try {
                OracleDataSource ds = new OracleDataSource();
                ds.setDriverType("thin");
                ds.setServerName("localhost");
                ds.setPortNumber(1521);
                ds.setDatabaseName("ORCL");
                ds.setUser("programacion");
                ds.setPassword("tiger");
                cnn = ds.getConnection();
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
        }
        return cnn;
    }

    public static void cerrarConexion() {
        try {
            cnn.close();
        } catch (SQLException ex) {
        }
    }
}
