package dao;

import beans.Cliente;
import util.TrataException;

import java.util.List;

public interface ClientesDAO {
    void inserir(Cliente cliente) throws TrataException;
    void deletarPorCPF(String cpf) throws TrataException;
    List<Cliente> listarTodos() throws TrataException;
    Cliente filtrarPorCPF(String cpf) throws TrataException;
}
