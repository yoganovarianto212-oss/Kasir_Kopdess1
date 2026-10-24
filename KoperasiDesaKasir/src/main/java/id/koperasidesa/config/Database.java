package id.koperasidesa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String URL = System.getProperty("koperasi.db.url",
            "jdbc:mysql://localhost:3306/koperasi_desa?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true");
    private static final String USER = System.getProperty("koperasi.db.user", "root");
    private static final String PASSWORD = System.getProperty("koperasi.db.password", "");

    private Database() {}
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
