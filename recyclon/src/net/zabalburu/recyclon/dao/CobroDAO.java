package net.zabalburu.recyclon.dao;

import java.util.List;
import net.zabalburu.recyclon.modelo.Cobro;

public interface CobroDAO {
    List<Cobro> getCobros();
    Cobro getCobro(int idCobro);
    Cobro nuevoCobro(Cobro c);
    void eliminarCobro(int idCobro);
    void modificarCobro(Cobro c);
    int getCaracMaxCobro(String columna);
    int getNumMaxCobro(String columna);
}
