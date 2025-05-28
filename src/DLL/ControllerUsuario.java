package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import BLL.Administrador;
import BLL.Autor;
import BLL.Cliente;
import BLL.ItemCarrito;
import BLL.Libro;
import BLL.Transaccion;
import BLL.Usuario;
import repository.Encriptador;
import repository.UsuarioRepository;

public class ControllerUsuario<T extends Usuario> implements UsuarioRepository,Encriptador{

    private static Connection con = Conexion.getInstance().getConnection();

    @Override
    public Usuario login() {
        String mail = JOptionPane.showInputDialog("Ingrese su correo:");
        String password = JOptionPane.showInputDialog("Ingrese su contraseña:");

        Usuario usuario = null;
        String sql = """
            SELECT u.id_usuario, u.nombre, u.dni, u.password, u.tipo_usuario, a.independiente, a.editorial
            FROM usuario u
            LEFT JOIN autor a ON u.id_usuario = a.fk_usuario
            WHERE u.mail = ?
        """;

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, mail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    int dni = rs.getInt("dni");
                    String passwordEncriptadaBD = rs.getString("password");
                    String tipoUsuario = rs.getString("tipo_usuario"); 

                    // 📌 Validar la contraseña ANTES de instanciar al usuario
                    if (!passwordEncriptadaBD.equals(encriptar(password))) {
                        JOptionPane.showMessageDialog(null, "❌ Error: Contraseña incorrecta. Verifique sus credenciales.");
                        return null;
                    }

                    // 📌 Identificar el tipo de usuario correctamente
                    switch (tipoUsuario.toLowerCase()) {
                        case "administrador":
                            usuario = new Administrador(id, nombre, passwordEncriptadaBD, dni, mail, "Apellido no especificado");
                            break;
                        case "cliente":
                            usuario = new Cliente(nombre, passwordEncriptadaBD, dni, mail, "Dirección no especificada");
                            break;
                        case "autor":
                            boolean independiente = rs.getBoolean("independiente");
                            String editorial = rs.getString("editorial");
                            usuario = new Autor(id, nombre, passwordEncriptadaBD, dni, mail, independiente, editorial);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "❌ Error: Tipo de usuario desconocido.");
                            return null;
                    }

                    JOptionPane.showMessageDialog(null, "✅ Inicio de sesión exitoso. Bienvenido, " + nombre + "!");
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Error: Usuario no encontrado.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
        }

        return usuario;
    }
    
    public static int obtenerNuevoIdUsuario() {
        String query = "SELECT MAX(id_usuario) FROM usuario"; // 📌 Consulta SQL

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) + 1; // 📌 Calcula el siguiente ID disponible
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al obtener ID: " + e.getMessage());
            e.printStackTrace();
        }

        return 1; // 📌 Si la tabla está vacía, comienza desde 1
    }
    @Override
    public void agregarUsuario(Usuario usuario, String tipoUsuario, String datoAdicional1, String datoAdicional2) {
        String insertUsuarioSQL = "INSERT INTO usuario (nombre, mail, dni, password, tipo_usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstance().getConnection();
        		PreparedStatement stmtUsuario = con.prepareStatement(insertUsuarioSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtUsuario.setString(1, usuario.getNombre());
            stmtUsuario.setString(2, usuario.getMail());
            stmtUsuario.setInt(3, usuario.getDni());
            stmtUsuario.setString(4, usuario.getPassword());
            stmtUsuario.setString(5, tipoUsuario); // 📌 Agregar el tipo de usuario

            int filasUsuario = stmtUsuario.executeUpdate();
            if (filasUsuario > 0) {
                ResultSet generatedKeys = stmtUsuario.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idUsuario = generatedKeys.getInt(1);
                    asignarRol(idUsuario, tipoUsuario, datoAdicional1, datoAdicional2);
                    JOptionPane.showMessageDialog(null, "Usuario registrado y asignado como " + tipoUsuario + " correctamente.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void asignarRol(int idUsuario, String categoria, String datoAdicional1, String datoAdicional2) {
        String insertRolSQL = "";

        switch (categoria.toLowerCase()) {
            case "administrador":
                insertRolSQL = "INSERT INTO administrador (fk_usuario, apellido) VALUES (?, ?)";
                break;
            case "cliente":
                insertRolSQL = "INSERT INTO cliente (fk_usuario, direccion) VALUES (?, ?)";
                break;
            case "autor":
                boolean independiente = datoAdicional1.trim().equalsIgnoreCase("true");
                insertRolSQL = independiente 
                    ? "INSERT INTO autor (fk_usuario, independiente) VALUES (?, ?)"  // 📌 Solo dos parámetros si es independiente
                    : "INSERT INTO autor (fk_usuario, independiente, editorial) VALUES (?, ?, ?)";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Error: Rol no válido.");
                return;
        }

        try (
        		Connection con = Conexion.getInstance().getConnection();
        		PreparedStatement stmtRol = con.prepareStatement(insertRolSQL)) {
            stmtRol.setInt(1, idUsuario);
            
            if ("autor".equalsIgnoreCase(categoria)) {
                boolean independiente = datoAdicional1.trim().equalsIgnoreCase("true");
                stmtRol.setBoolean(2, independiente);

                if (!independiente) { // 📌 Solo asignar editorial si NO es independiente
                    stmtRol.setString(3, datoAdicional2);
                }
            } else { // 📌 Para Administrador y Cliente
                stmtRol.setString(2, datoAdicional1);
            }

            stmtRol.executeUpdate();
            JOptionPane.showMessageDialog(null, "Rol asignado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al asignar rol: " + e.getMessage());
            e.printStackTrace();
        }
    }

  

    public void agregarLibro(Libro libro) {
        try (Connection con = Conexion.getInstance().getConnection();
        		PreparedStatement stmt = con.prepareStatement(
            "INSERT INTO libro (titulo, sipnosis, precio, stock, estado) VALUES (?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getsipnosis());
            stmt.setDouble(3, libro.getPrecio());
            stmt.setInt(4, libro.getStock());
            stmt.setString(5, libro.getEstado());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Libro guardado en la base de datos!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar libro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public LinkedList<Libro> obtenerLibros() {
        LinkedList<Libro> libros = new LinkedList<>();
        String query = "SELECT * FROM libro";

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                libros.add(new Libro(
                    rs.getInt("id_libro"),
                    rs.getString("titulo"),
                    rs.getString("sipnosis"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al obtener libros: " + e.getMessage());
            e.printStackTrace();
        }
        return libros;
    }
    public LinkedList<Usuario> obtenerUsuarios() {
        LinkedList<Usuario> usuarios = new LinkedList<>();

        String query = """
            SELECT u.id_usuario, u.nombre, u.dni, u.mail, u.password, u.tipo_usuario,
                   a.apellido, c.direccion, au.independiente, au.editorial
            FROM usuario u
            LEFT JOIN administrador a ON u.id_usuario = a.fk_usuario
            LEFT JOIN cliente c ON u.id_usuario = c.fk_usuario
            LEFT JOIN autor au ON u.id_usuario = au.fk_usuario;
        """;

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String mail = rs.getString("mail");
                int dni = rs.getInt("dni");
                String password = rs.getString("password");
                String tipoUsuario = rs.getString("tipo_usuario");

                Usuario usuario = switch (tipoUsuario) {
                    case "Administrador" -> new Administrador(id, nombre, password, dni, mail, rs.getString("apellido"));
                    case "Cliente" -> new Cliente(id, nombre, password, dni, mail, rs.getString("direccion"));
                    case "Autor" -> new Autor(id, nombre, password, dni, mail, rs.getBoolean("independiente"), rs.getString("editorial"));
                    default -> {
                        JOptionPane.showMessageDialog(null, "❌ Tipo de usuario desconocido: " + tipoUsuario);
                        yield null;
                    }
                };

                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al obtener usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }
    
    public boolean modificarUsuario(int idUsuario, String nuevoNombre, String nuevoMail, String nuevaPassword) {
    	
        String query = """
            UPDATE usuario 
            SET nombre = ?, mail = ?, password = ? 
            WHERE id_usuario = ?;
        """;

        try (Connection con = Conexion.getInstance().getConnection();
        		PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevoMail);
            stmt.setString(3, nuevaPassword);
            stmt.setInt(4, idUsuario); // ✅ Asegura que este parámetro se configure correctamente
            System.out.println("✅ ID Usuario recibido: " + idUsuario);
            System.out.println("✅ ID Usuario recibido para modificar: " + idUsuario);
            if (idUsuario == 0) {
                JOptionPane.showMessageDialog(null, "❌ Error: El usuario no tiene un ID válido.");
                return false;
            }
            System.out.println("🔍 Ejecutando UPDATE con ID: " + idUsuario);
            
            int filasAfectadas = stmt.executeUpdate();
            System.out.println("🔄 Filas afectadas: " + filasAfectadas);

            return filasAfectadas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al modificar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        String[] tablasRelacionadas = {"autor", "cliente", "administrador"};

        try {
            // 🔍 Verificar si el usuario existe antes de eliminarlo
            String consultaExistencia = "SELECT * FROM usuario WHERE id_usuario = ?";
            try (Connection con = Conexion.getInstance().getConnection();
            		PreparedStatement stmtVerificacion = con.prepareStatement(consultaExistencia)) {
                stmtVerificacion.setInt(1, idUsuario);
                ResultSet rs = stmtVerificacion.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "❌ Error: No se encontró el usuario con ID: " + idUsuario);
                    return false;
                }
                System.out.println("✅ Usuario encontrado: " + rs.getString("nombre"));
            }

            // 🔥 Eliminar registros relacionados en las tablas con `fk_usuario`
            for (String tabla : tablasRelacionadas) {
                String queryRelacionada = "DELETE FROM " + tabla + " WHERE fk_usuario = ?";
                try (PreparedStatement stmtRelacionada = con.prepareStatement(queryRelacionada)) {
                    stmtRelacionada.setInt(1, idUsuario);
                    int filasAfectadasRelacionadas = stmtRelacionada.executeUpdate();
                    System.out.println("🔄 Filas eliminadas en " + tabla + ": " + filasAfectadasRelacionadas);
                }
            }

            // 🔥 Finalmente, eliminamos al usuario en la tabla `usuario`
            String queryUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
            try (Connection con = Conexion.getInstance().getConnection();
            		PreparedStatement stmtUsuario = con.prepareStatement(queryUsuario)) {
                stmtUsuario.setInt(1, idUsuario);
                int filasAfectadasUsuario = stmtUsuario.executeUpdate();
                System.out.println("✅ Usuario eliminado correctamente. Filas afectadas: " + filasAfectadasUsuario);

                if (filasAfectadasUsuario > 0) {
                    JOptionPane.showMessageDialog(null, "✅ Usuario eliminado correctamente.");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Error: No se encontró el usuario en la tabla `usuario`.");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean modificarLibro(int idLibro, String nuevoTitulo, String nuevaSipnosis, double nuevoPrecio, int nuevoStock, String nuevoEstado) {
        String sql = "UPDATE libro SET titulo = ?, sipnosis = ?, precio = ?, stock = ?, estado = ? WHERE id_libro = ?";

        try (Connection con = Conexion.getInstance().getConnection();
        		PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nuevoTitulo);
            stmt.setString(2, nuevaSipnosis);
            stmt.setDouble(3, nuevoPrecio);
            stmt.setInt(4, nuevoStock);
            stmt.setString(5, nuevoEstado);
            stmt.setInt(6, idLibro);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLibro(int idLibro) {
        try {
            // 🔍 Eliminar referencias en la tabla itemcarrito
            String queryReferencias = "DELETE FROM itemcarrito WHERE fk_libro = ?";
            try (Connection con = Conexion.getInstance().getConnection();
            		PreparedStatement stmtRef = con.prepareStatement(queryReferencias)) {
                stmtRef.setInt(1, idLibro);
                int filasAfectadasRef = stmtRef.executeUpdate();
                System.out.println("🔄 Filas eliminadas en itemcarrito: " + filasAfectadasRef);
            }

            // 🔥 Finalmente, eliminar el libro de la tabla libro
            String queryLibro = "DELETE FROM libro WHERE id_libro = ?";
            try (PreparedStatement stmtLibro = con.prepareStatement(queryLibro)) {
                stmtLibro.setInt(1, idLibro);
                int filasAfectadasLibro = stmtLibro.executeUpdate();
                System.out.println("✅ Libro eliminado correctamente. Filas afectadas: " + filasAfectadasLibro);

                return filasAfectadasLibro > 0;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }
    public void registrarTransaccion(Transaccion transaccion) {
        String sqlTransaccion = "INSERT INTO transaccion (total, fk_cliente) VALUES (?, ?)";

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement pstmtTransaccion = con.prepareStatement(sqlTransaccion, Statement.RETURN_GENERATED_KEYS)) {

            // Guardar la transacción en la base de datos
            pstmtTransaccion.setDouble(1, transaccion.getTotal());
            pstmtTransaccion.setInt(2, transaccion.getCliente().getId());
            pstmtTransaccion.executeUpdate();

            // Obtener el ID de la transacción recién creada
            ResultSet rs = pstmtTransaccion.getGeneratedKeys();
            int idTransaccion = -1;
            if (rs.next()) {
                idTransaccion = rs.getInt(1);
            }

            // Restar el stock de cada libro (SIN guardar en `transaccion_libro`)
            String sqlActualizarStock = "UPDATE libro SET stock = stock - ? WHERE id_libro = ?";
            try (PreparedStatement pstmtStock = con.prepareStatement(sqlActualizarStock)) {
                for (ItemCarrito item : transaccion.getItems()) {
                    pstmtStock.setInt(1, item.getCantidad());
                    pstmtStock.setInt(2, item.getLibro().getId());
                    pstmtStock.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(null, "Compra registrada con éxito.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar compra: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public LinkedList<Transaccion> obtenerTransaccionesPorCliente(int idCliente) {
    	LinkedList<Transaccion> transacciones = new LinkedList<>();
        String sqlTransaccion = "SELECT id_transaccion, total FROM transaccion WHERE fk_cliente = ?";

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement pstmtTransaccion = con.prepareStatement(sqlTransaccion)) {

            pstmtTransaccion.setInt(1, idCliente);
            ResultSet rsTransaccion = pstmtTransaccion.executeQuery();

            while (rsTransaccion.next()) {
                int idTransaccion = rsTransaccion.getInt("id_transaccion");
                double total = rsTransaccion.getDouble("total");

                transacciones.add(new Transaccion(idTransaccion, new Cliente(idCliente, "", "", 0, "", ""), total, new ArrayList<>()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener compras: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return transacciones;
    }

}
