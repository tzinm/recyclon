package net.zabalburu.recyclon.modelo;

import java.util.Date;

public class Cobro {
    private int idCobro;
    private Cliente cliente;
    private double importe;
    private int nFactura;
    private String nCuentaCliente;
    private Date fVencimiento;
    private String fPago;
    private Banco banco;
    private boolean estado;

    public Cobro() {
    }

    public Cobro(Cliente cliente, double importe, int nFactura, String nCuentaCliente, Date fVencimiento, String fPago, Banco banco, boolean estado) {
        this.cliente = cliente;
        this.importe = importe;
        this.nFactura = nFactura;
        this.nCuentaCliente = nCuentaCliente;
        this.fVencimiento = fVencimiento;
        this.fPago = fPago;
        this.banco = banco;
        this.estado = estado;
    }

    public int getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(int idCobro) {
        this.idCobro = idCobro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getNFactura() {
        return nFactura;
    }

    public void setNFactura(int nFactura) {
        this.nFactura = nFactura;
    }

    public String getNCuentaCliente() {
        return nCuentaCliente;
    }

    public void setNCuentaCliente(String nCuentaCliente) {
        this.nCuentaCliente = nCuentaCliente;
    }

    public Date getFVencimiento() {
        return fVencimiento;
    }

    public void setFVencimiento(Date fVencimiento) {
        this.fVencimiento = fVencimiento;
    }

    public String getFPago() {
        return fPago;
    }

    public void setFPago(String fPago) {
        this.fPago = fPago;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cobro other = (Cobro) obj;
        if (this.idCobro != other.idCobro) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cobro{" + "cliente=" + cliente + ", importe=" + importe + ", nFactura=" + nFactura + ", nCuentaCliente=" + nCuentaCliente + ", fVencimiento=" + fVencimiento + ", fPago=" + fPago + ", banco=" + banco + ", estado=" + estado + '}';
    }

}
