package dao;

import util.TrataException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager implements AutoCloseable {
    private static ConnectionManager instance;
    private final Connection connection;

    private ConnectionManager() throws TrataException {
        try {
            System.out.println("Inicializando conexão...");
            connection = DriverManager.getConnection("jdbc:sqlite:clientes.db");
        } catch (SQLException e) {
            throw new TrataException(e.getMessage());
        }
    }

    public static ConnectionManager getInstance() throws TrataException {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        if (connection == null) {
            return;
        }

        System.out.println("Finalizando conexão...");
        connection.close();
    }
}
