package at.technikumwien.swe.datalayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String USER = "postgres";
    private static final String PASS = "1234";
    private static final String HOST = "127.0.0.1";
    private static final String DB_NAME = "postgres";
    private static final int PORT = 5432;
    private static volatile Database instance = null;
    Connection connection = null;

    private Database() {
        connect();
    }

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private void connect() {
        try {
            if (connection != null && connection.isValid(3)) return;

            String url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
            connection = DriverManager.getConnection(url, USER, PASS);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("DB connection failed" + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
