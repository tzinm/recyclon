package net.zabalburu.recyclon.modelo;

import java.util.Date;

public class Pago {

    private int idPago;
    private Proveedor proveedor;
    private double importe;
    private int nFactura;
    private Date fVencimiento;
    private String fPago;
    private Banco banco;
    private boolean estado;

    public Pago() {
    }

    public Pago(Proveedor proveedor, double importe, int nFactura, Date fVencimiento, String fPago, Banco banco, boolean estado) {
        this.proveedor = proveedor;
        this.importe = importe;
        this.nFactura = nFactura;
        this.fVencimiento = fVencimiento;
        this.fPago = fPago;
        this.banco = banco;
        this.estado = estado;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
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
        int hash = 7;
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
        final Pago other = (Pago) obj;
        if (this.idPago != other.idPago) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pago{" + "proveedor=" + proveedor + ", importe=" + importe + ", nFactura=" + nFactura + ", fVencimiento=" + fVencimiento + ", fPago=" + fPago + ", banco=" + banco + ", estado=" + estado + '}';
    }

}
