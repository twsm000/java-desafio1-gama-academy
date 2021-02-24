package dao;

import beans.Cliente;
import util.TrataException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientesDB implements ClientesDAO {
    private final ConnectionManager manager;

    public ClientesDB(ConnectionManager manager) {
        this.manager = manager;
    }

    private static void validate(Cliente cliente) throws TrataException {
        if (cliente == null) {
            throw new TrataException("Cliente não informado!");
        }
        cliente.validate();
    }

    @Override
    public void inserir(Cliente cliente) throws TrataException {
        ClientesDB.validate(cliente);

        String sql =
            "INSERT INTO clientes (nome, cpf, tel) " +
            "VALUES (?, ?, ?)";
        try (PreparedStatement st = manager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, cliente.getNome());
            st.setString(2, cliente.getCpf());
            st.setString(3, cliente.getTelefone());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected != 1) {
                throw new TrataException("Ocorreu uma falha ao inserir o cliente.");
            }
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new TrataException(e.getMessage());
        }
    }

    @Override
    public void deletarPorCPF(String cpf) throws TrataException {
        String sql =
            "DELETE FROM clientes " +
            "WHERE cpf = ?;";
        int rowsAffected;
        try (PreparedStatement st = manager.getConnection().prepareStatement(sql)) {
            st.setString(1, cpf);
            st.executeUpdate();
            rowsAffected = st.getUpdateCount();
        } catch (SQLException e) {
            throw new TrataException(e.getMessage());
        }

        if (rowsAffected == 0) {
            throw new TrataException("Falha ao exluir registro: cliente não encontrado.");
        }
    }

    @Override
    public List<Cliente> listarTodos() throws TrataException {
        String sql = "SELECT * FROM clientes;";
        try (PreparedStatement st = manager.getConnection().prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) {
                    throw new TrataException("Nenhum cliente encontrado.");
                }

                List<Cliente> list = new ArrayList<>();
                do {
                    list.add(newCliente(rs));
                } while(rs.next());
                return list;
            }
        } catch (SQLException e) {
            throw new TrataException(e.getMessage());
        }
    }

    private Cliente newCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setTelefone(rs.getString("tel"));
        return c;
    }

    @Override
    public Cliente filtrarPorCPF(String cpf) throws TrataException {
        String sql =
            "SELECT * " +
            "  FROM clientes " +
            " WHERE cpf = ?;";
        try (PreparedStatement st = manager.getConnection().prepareStatement(sql)) {
            st.setString(1, cpf);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return newCliente(rs);
                }
                throw new TrataException(String.format("Cliente com CPF: %s não encontrado.\n", cpf));
            }
        } catch (SQLException e) {
            throw new TrataException(e.getMessage());
        }
    }
}
