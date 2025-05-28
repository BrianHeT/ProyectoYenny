package DLL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/libreria";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Conexion instance;
    private Connection conect;

    private Conexion() {
        try {
            conect = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión establecida correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (conect == null || conect.isClosed()) {
                conect = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("🔄 Reconectando...");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return conect;
    }

    public void cerrarConexion() {
        if (conect != null) {
            try {
                conect.close();
                System.out.println("✅ Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
