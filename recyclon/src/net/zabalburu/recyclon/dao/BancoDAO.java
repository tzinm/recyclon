package net.zabalburu.recyclon.dao;

import java.util.List;
import net.zabalburu.recyclon.modelo.Banco;

public interface BancoDAO {
    List<Banco> getBancos();
    Banco getBanco(Integer id);
    Banco nuevoBanco(Banco b);
    void eliminarBanco(int idBanco);
    void modificarBanco(Banco b);
    int getCaracMaxBanco(String columna);
}
