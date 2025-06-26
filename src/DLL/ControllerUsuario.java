package DLL;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import BLL.*;
import repository.Encriptador;
import repository.UsuarioRepository;

public class ControllerUsuario<T extends Usuario> implements UsuarioRepository, Encriptador {

	public Usuario login(String mail, String passPlain) {
	    Usuario usuario = null;
	    String sql = """
	        SELECT
	            u.id_usuario,
	            u.nombre,
	            u.dni,
	            u.tipo_usuario,
	            a.independiente,
	            a.editorial,
	            cl.id_cliente   AS cliente_id,
	            cl.saldo        AS cliente_saldo,
	            cl.direccion    AS cliente_direccion,
	            adm.apellido    AS admin_apellido
	        FROM usuario u
	        LEFT JOIN autor a           ON u.id_usuario = a.fk_usuario
	        LEFT JOIN cliente cl        ON u.id_usuario = cl.fk_usuario
	        LEFT JOIN administrador adm ON u.id_usuario = adm.fk_usuario
	        WHERE
	            u.mail = ?
	          AND u.password = SHA2(?,256)
	    """;

	    try (Connection con = Conexion.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {

	        // 1) Pongo mail y password en claro; MySQL se encarga de hashearlo
	        stmt.setString(1, mail);
	        stmt.setString(2, passPlain);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (!rs.next()) {
	                JOptionPane.showMessageDialog(null,
	                    "❌ Error: usuario o contraseña incorrectos.");
	                return null;
	            }

	            // 2) Extraigo datos comunes
	            int    id       = rs.getInt("id_usuario");
	            String nombre   = rs.getString("nombre");
	            int    dni      = rs.getInt("dni");
	            String tipo     = rs.getString("tipo_usuario").toLowerCase();

	            // 3) Construyo el objeto según tipo y le inyecto el controller
	            switch (tipo) {
	                case "administrador" -> {
	                    String apellido = rs.getString("admin_apellido");
	                    Administrador adm = new Administrador(
	                        id, nombre,  "", dni, mail, apellido
	                    );
	                    adm.setController(this);
	                    usuario = adm;
	                    
	                }
	                case "cliente" -> {
	                    double saldo    = rs.getDouble("cliente_saldo");
	                    String dir      = rs.getString("cliente_direccion");
	                    int    idCliente = rs.getInt("cliente_id");
	                    Cliente cli = new Cliente(
	                        id, nombre,  "", dni, mail, dir
	                    );
	                    cli.setSaldo(saldo);
	                    cli.setIdCliente(idCliente);
	                    cli.setController(this);
	                    usuario = cli;
	                }
	                case "autor" -> {
	                    boolean ind = rs.getBoolean("independiente");
	                    String ed   = rs.getString("editorial");
	                    Autor au = new Autor(
	                        id, nombre, "", dni, mail, ind, ed
	                    );
	                    au.setController(this);
	                    usuario = au;
	                }
	                default -> {
	                    JOptionPane.showMessageDialog(null,
	                        "❌ Error: tipo de usuario desconocido.");
	                    return null;
	                }
	            }

	            JOptionPane.showMessageDialog(null,
	                "✅ Inicio de sesión exitoso. ¡Bienvenido, " + nombre + "!");
	        }

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null,
	            "❌ Error al iniciar sesión: " + ex.getMessage());
	    }

	    return usuario;
	}

    public static int obtenerNuevoIdUsuario() {
        String query = "SELECT MAX(id_usuario) FROM usuario"; 
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1; 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al obtener ID: " + e.getMessage());
            e.printStackTrace();
        }
        return 1; 
    }

    @Override
    public int agregarUsuario(Usuario usuario, String tipoUsuario, String datoAdicional1, String datoAdicional2) {
        String insertUsuarioSQL = "INSERT INTO usuario (nombre, mail, dni, password, tipo_usuario) VALUES (?, ?, ?, ?, ?)";
        int idUsuario = -1;
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmtUsuario = con.prepareStatement(insertUsuarioSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
        	stmtUsuario.setString(1, usuario.getNombre());
        	stmtUsuario.setString(2, usuario.getMail());
        	stmtUsuario.setInt(3, usuario.getDni());
        	stmtUsuario.setString(4, encriptar(usuario.getPassword())); 
        	stmtUsuario.setString(5, tipoUsuario);

            int filasUsuario = stmtUsuario.executeUpdate();
            if (filasUsuario > 0) {
                ResultSet generatedKeys = stmtUsuario.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idUsuario = generatedKeys.getInt(1);
                    asignarRol(idUsuario, tipoUsuario, datoAdicional1, datoAdicional2);
                    JOptionPane.showMessageDialog(null, "Usuario registrado y asignado como " + tipoUsuario + " correctamente.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return idUsuario;
    }

    private void asignarRol(int idUsuario, String categoria, String datoAdicional1, String datoAdicional2) {
        String insertRolSQL = "";

        switch (categoria.toLowerCase()) {
            case "administrador":
                insertRolSQL = "INSERT INTO administrador (fk_usuario, apellido) VALUES (?, ?)";
                break;
            case "cliente":
                insertRolSQL = "INSERT INTO cliente (fk_usuario, direccion, saldo) VALUES (?, ?, ?)";
                break;
            case "autor":
                boolean independiente = datoAdicional1.trim().equalsIgnoreCase("true");
                insertRolSQL = independiente 
                    ? "INSERT INTO autor (fk_usuario, independiente) VALUES (?, ?)"
                    : "INSERT INTO autor (fk_usuario, independiente, editorial) VALUES (?, ?, ?)";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Error: Rol no válido.");
                return;
        } 

        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmtRol = con.prepareStatement(insertRolSQL)) {
            stmtRol.setInt(1, idUsuario);

            if ("autor".equalsIgnoreCase(categoria)) {
                boolean independiente = datoAdicional1.trim().equalsIgnoreCase("true");
                stmtRol.setBoolean(2, independiente);
                if (!independiente) {
                    stmtRol.setString(3, datoAdicional2);
                }
            } else {
                stmtRol.setString(2, datoAdicional1);
                if ("cliente".equalsIgnoreCase(categoria)) {
                    stmtRol.setDouble(3, 0.0);
                }
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
                       a.apellido, cl.direccion, cl.saldo, au.independiente, au.editorial
                FROM usuario u
                LEFT JOIN administrador a ON u.id_usuario = a.fk_usuario
                LEFT JOIN cliente cl ON u.id_usuario = cl.fk_usuario
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
                case "Cliente" -> {
                    Cliente cliente = new Cliente(id, nombre, password, dni, mail, rs.getString("direccion"));
                    cliente.setController(this); 
                    cliente.setSaldo(rs.getDouble("saldo"));
                    yield cliente;
                }
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
            stmt.setString(3, encriptar(nuevaPassword));
            stmt.setInt(4, idUsuario); 
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
        try (Connection con = Conexion.getInstance().getConnection()) {
            // Check existence
            String consultaExistencia = "SELECT * FROM usuario WHERE id_usuario = ?";
            try (PreparedStatement stmtVerificacion = con.prepareStatement(consultaExistencia)) {
                stmtVerificacion.setInt(1, idUsuario);
                ResultSet rs = stmtVerificacion.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "❌ Error: No se encontró el usuario con ID: " + idUsuario);
                    return false;
                }
            }
            // Delete related records
            for (String tabla : tablasRelacionadas) {
                String queryRelacionada = "DELETE FROM " + tabla + " WHERE fk_usuario = ?";
                try (PreparedStatement stmtRelacionada = con.prepareStatement(queryRelacionada)) {
                    stmtRelacionada.setInt(1, idUsuario);
                    stmtRelacionada.executeUpdate();
                }
            }
            // Delete user
            String queryUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
            try (PreparedStatement stmtUsuario = con.prepareStatement(queryUsuario)) {
                stmtUsuario.setInt(1, idUsuario);
                int filasAfectadasUsuario = stmtUsuario.executeUpdate();
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
        try (Connection con = Conexion.getInstance().getConnection()) {
            // Delete related itemcarrito records
            String queryReferencias = "DELETE FROM itemcarrito WHERE fk_libro = ?";
            try (PreparedStatement stmtRef = con.prepareStatement(queryReferencias)) {
                stmtRef.setInt(1, idLibro);
                stmtRef.executeUpdate();
            }

            // Delete the book itself
            String queryLibro = "DELETE FROM libro WHERE id_libro = ?";
            try (PreparedStatement stmtLibro = con.prepareStatement(queryLibro)) {
                stmtLibro.setInt(1, idLibro);
                int filasAfectadasLibro = stmtLibro.executeUpdate();
                if (filasAfectadasLibro > 0) {
                    JOptionPane.showMessageDialog(null, "✅ Libro eliminado correctamente.");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Error: No se encontró el libro.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
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
    public void actualizarSaldoCliente(String email, double nuevoSaldo) {
        int idCliente = obtenerIdClientePorEmail(email);
        if (idCliente == -1) {
            JOptionPane.showMessageDialog(null, "❌ Error: No se encontró el cliente para el usuario.");
            return;
        }
        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE cliente SET saldo = ? WHERE id_cliente = ?")) {
            stmt.setDouble(1, nuevoSaldo);
            stmt.setInt(2, idCliente);
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("✅ Saldo actualizado correctamente para id_cliente = " + idCliente);
            } else {
                JOptionPane.showMessageDialog(null, "❌ Error: No se pudo actualizar el saldo.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al actualizar saldo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int obtenerIdClientePorEmail(String email) {
        String sql = "SELECT c.id_cliente FROM cliente c " +
                     "JOIN usuario u ON c.fk_usuario = u.id_usuario " +
                     "WHERE u.mail = ?";
        try (Connection con = Conexion.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                System.out.println("✅ Found id_cliente = " + idCliente + " for email = " + email);
                return idCliente;
            } else {
                System.out.println("❌ No client found for email = " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Libro> obtenerLibrosYVentasPorAutor(int idAutor) {
        List<Libro> lista = new LinkedList<>();
        String sql = """
            SELECT 
              l.id_libro,
              l.titulo,
              l.sipnosis,
              l.precio,
              l.stock,
              l.estado,
              COALESCE(SUM(tl.cantidad), 0) AS ventas
            FROM libro l
            LEFT JOIN transaccion_libro tl
              ON l.id_libro = tl.fk_libro
            WHERE l.fk_autor = ?
            GROUP BY 
              l.id_libro, l.titulo, l.sipnosis,
              l.precio, l.stock, l.estado
            ORDER BY l.titulo
        """;

        try (
          Connection con = Conexion.getInstance().getConnection();
          PreparedStatement ps = con.prepareStatement(sql)
        ) {
          ps.setInt(1, idAutor);
          try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
              // construyo Libro según tu constructor
              Libro lib = new Libro(
                rs.getInt("id_libro"),
                rs.getString("titulo"),
                rs.getString("sipnosis"),
                rs.getDouble("precio"),
                rs.getInt("stock"),
                rs.getString("estado")
              );
              // asigno el total de unidades vendidas
              lib.setVentas(rs.getInt("ventas"));
              lista.add(lib);
            }
          }
        } catch (SQLException e) {
          JOptionPane.showMessageDialog(null,
            "❌ Error cargando libros/ventas: " + e.getMessage(),
            "Error SQL", JOptionPane.ERROR_MESSAGE
          );
          e.printStackTrace();
        }

        return lista;
    }


}
