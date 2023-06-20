package orm;

import java.sql.*;

public class ConnectionManager {
    private static final String url = "jdbc:postgresql://localhost:5432/elaboratoswe";
    private static final String username = "user";
    private static final String password = "password";
    private static Connection con = null;

    private ConnectionManager(){}

    static public Connection getConnection() throws SQLException {
        if (con == null)
            con = DriverManager.getConnection(url, username, password);

        return con;
    }
}
