package net.zabalburu.recyclon.modelo;

public class Banco {
    private int idBanco;
    private String nombreBanco;
    
    public Banco() {
    }

    public Banco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }
    
    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
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
        final Banco other = (Banco) obj;
        if (this.idBanco != other.idBanco) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreBanco;
    }
}
