package DLL;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import BLL.*;
import repository.Encriptador;
import repository.UsuarioRepository;

public class ControllerUsuario<T extends Usuario> implements UsuarioRepository, Encriptador {

	private Connection getConnection() throws SQLException {
		return Conexion.getInstance().getConnection();
	}

	public Usuario login(String mail, String passPlain) {
		String sql = """
				    SELECT u.id_usuario, u.nombre, u.dni, u.mail,
				           u.tipo_usuario,
				           a.independiente, a.editorial,
				           cl.id_cliente      AS cliente_id,
				           cl.saldo           AS cliente_saldo,
				           cl.direccion       AS cliente_direccion,
				           adm.apellido       AS admin_apellido
				      FROM usuario u
				 LEFT JOIN autor a           ON u.id_usuario = a.fk_usuario
				 LEFT JOIN cliente cl        ON u.id_usuario = cl.fk_usuario
				 LEFT JOIN administrador adm ON u.id_usuario = adm.fk_usuario
				     WHERE u.mail = ?
				       AND u.password = SHA2(?,256)
				""";

		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, mail);
			ps.setString(2, passPlain);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					JOptionPane.showMessageDialog(null, "❌ Usuario o contraseña incorrectos.");
					return null;
				}

				int id = rs.getInt("id_usuario");
				String nombre = rs.getString("nombre");
				int dni = rs.getInt("dni");
				String tipo = rs.getString("tipo_usuario").toLowerCase();

				Usuario usuario;
				switch (tipo) {
				case "administrador" -> {
					String apellido = rs.getString("admin_apellido");
					Administrador adm = new Administrador(id, nombre, "", dni, mail, apellido);
					adm.setController(this);
					usuario = adm;
				}
				case "cliente" -> {
					int idCli = rs.getInt("cliente_id");
					double saldo = rs.getDouble("cliente_saldo");
					String dir = rs.getString("cliente_direccion");
					Cliente cli = new Cliente(id, nombre, "", dni, mail, dir);
					cli.setIdCliente(idCli);
					cli.setSaldo(saldo);
					cli.setController(this);
					usuario = cli;
				}
				case "autor" -> {
					boolean ind = rs.getBoolean("independiente");
					String ed = rs.getString("editorial");

					if (ind) {
						// Paso exactamente los 7 parámetros que pide tu constructor
						AutorIndependiente ai = new AutorIndependiente(id, // 1) id
								nombre, // 2) nombre
								passPlain, // 3) password plano (o vacío)
								dni, // 4) dni
								mail, // 5) mail
								true, // 6) independiente = true
								ed // 7) editorial (puede ser null)
						);
						ai.setController(this);
						usuario = ai;
					} else {
						Autor au = new Autor(id, nombre, passPlain, dni, mail, false, ed);
						au.setController(this);
						usuario = au;
					}
				}

				default -> {
					JOptionPane.showMessageDialog(null, "❌ Tipo de usuario desconocido.");
					return null;
				}
				}

				JOptionPane.showMessageDialog(null, "✅ Bienvenido, " + nombre + "!");
				return usuario;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "❌ Error de SQL: " + e.getMessage());
			return null;
		}
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
		int idUsuario = -1;
		String sql = """
				  INSERT INTO usuario
				    (nombre, mail, dni, password, tipo_usuario)
				  VALUES (?, ?, ?, SHA2(?,256), ?)
				""";
		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getMail());
			ps.setInt(3, usuario.getDni());
			ps.setString(4, usuario.getPassword()); // texto plano aquí
			ps.setString(5, tipoUsuario);

			int filas = ps.executeUpdate();
			System.out.println("► Filas insertadas: " + filas);

			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					idUsuario = keys.getInt(1);
					System.out.println("► KEY obtenida: " + idUsuario);

					// Sincronizo el objeto en memoria con su ID real
					usuario.setId(idUsuario);

					// Asignar rol específico
					asignarRol(idUsuario, tipoUsuario, datoAdicional1, datoAdicional2);
					JOptionPane.showMessageDialog(null,
							"Usuario registrado y asignado como " + tipoUsuario + " correctamente.");
				} else {
					System.err.println("⚠️ No se devolvieron generatedKeys.");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage(), "Error SQL",
					JOptionPane.ERROR_MESSAGE);
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
			insertRolSQL = independiente ? "INSERT INTO autor (fk_usuario, independiente) VALUES (?, ?)"
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

	public boolean agregarLibro(int idAutor, Libro libro) {
		String sql = """
				    INSERT INTO libro
				      (titulo, sipnosis, precio, stock, estado, fk_autor)
				    VALUES (?,?,?,?,?,?)
				""";
		try (Connection c = Conexion.getInstance().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, libro.getTitulo());
			ps.setString(2, libro.getSipnosis());
			ps.setDouble(3, libro.getPrecio());
			ps.setInt(4, libro.getStock());
			ps.setString(5, libro.getEstado());
			ps.setInt(6, idAutor);

			return ps.executeUpdate() == 1;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public LinkedList<Libro> obtenerLibros() {
		LinkedList<Libro> libros = new LinkedList<>();
		String query = "SELECT * FROM libro";

		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				libros.add(new Libro(rs.getInt("id_libro"), rs.getString("titulo"), rs.getString("sipnosis"),
						rs.getDouble("precio"), rs.getInt("stock"), rs.getString("estado")));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "❌ Error al obtener libros: " + e.getMessage());
			e.printStackTrace();
		}
		return libros;
	}

	@Override
	public LinkedList<Usuario> obtenerUsuarios() {
		LinkedList<Usuario> usuarios = new LinkedList<>();

		String sql = """
				SELECT u.id_usuario,
				       u.nombre,
				       u.dni,
				       u.mail,
				       u.password,
				       u.tipo_usuario,
				       a.apellido,
				       cl.direccion,
				       cl.saldo,
				       au.independiente,
				       au.editorial
				FROM usuario u
				LEFT JOIN administrador a ON u.id_usuario = a.fk_usuario
				LEFT JOIN cliente cl       ON u.id_usuario = cl.fk_usuario
				LEFT JOIN autor au         ON u.id_usuario = au.fk_usuario
				""";

		// Cada llamada abre y cierra su propia Connection
		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id_usuario");
				String nombre = rs.getString("nombre");
				String mail = rs.getString("mail");
				int dni = rs.getInt("dni");
				String pwd = rs.getString("password");
				String tipoUser = rs.getString("tipo_usuario");

				Usuario usuario = switch (tipoUser) {
				case "Administrador" -> {
					Administrador adm = new Administrador(id, nombre, pwd, dni, mail, rs.getString("apellido"));
					adm.setController(this);
					yield adm;
				}
				case "Cliente" -> {
					Cliente cli = new Cliente(id, nombre, pwd, dni, mail, rs.getString("direccion"),
							rs.getDouble("saldo"));
					cli.setController(this);
					yield cli;
				}
				case "Autor" -> {
					Autor auObj = new Autor(id, nombre, pwd, dni, mail, rs.getBoolean("independiente"),
							rs.getString("editorial"));
					auObj.setController(this);
					yield auObj;
				}
				default -> {
					System.err.println("⚠️ Tipo de usuario desconocido: " + tipoUser);
					yield null;
				}
				};

				if (usuario != null) {
					usuarios.add(usuario);
				}
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "❌ Error al obtener usuarios: " + e.getMessage(), "Error SQL",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return usuarios;
	}

	public boolean modificarUsuario(int idUsuario, String nuevoNombre, String nuevoMail, String nuevaPassword) {
		String query = """
				UPDATE usuario
				SET nombre   = ?,
				mail     = ?,
				password = SHA2(?,256)
				WHERE id_usuario = ?
				""";
		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {

			stmt.setString(1, nuevoNombre);
			stmt.setString(2, nuevoMail);
// dejo el plain-text aquí y que MySQL lo transforme
			stmt.setString(3, nuevaPassword);
			stmt.setInt(4, idUsuario);

			int filas = stmt.executeUpdate();
			return filas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean eliminarUsuario(int idUsuario) {
		String[] tablasRelacionadas = { "autor", "cliente", "administrador" };
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

	public boolean modificarLibro(int idLibro, String nuevoTitulo, String nuevaSipnosis, double nuevoPrecio,
			int nuevoStock, String nuevoEstado) {
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
		String sqlTx = "INSERT INTO transaccion (total, fk_cliente) VALUES (?, ?)";
		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement psTx = con.prepareStatement(sqlTx, Statement.RETURN_GENERATED_KEYS)) {

			// 1. Grabar la transacción y obtener su ID
			psTx.setDouble(1, transaccion.getTotal());
			psTx.setInt(2, transaccion.getCliente().getId());
			psTx.executeUpdate();
			ResultSet rsKeys = psTx.getGeneratedKeys();
			int idTx = rsKeys.next() ? rsKeys.getInt(1) : -1;

			// 2. Actualizar stock de cada libro
			String sqlStock = "UPDATE libro SET stock = stock - ? WHERE id_libro = ?";
			try (PreparedStatement psStock = con.prepareStatement(sqlStock)) {
				for (ItemCarrito item : transaccion.getItems()) {
					psStock.setInt(1, item.getCantidad());
					psStock.setInt(2, item.getLibro().getId());
					psStock.executeUpdate();
				}
			}

			// 3. Grabar cada ítem en transaccion_libro
			String sqlDet = "INSERT INTO transaccion_libro (fk_transaccion, fk_libro, cantidad, precio_unitario) "
					+ "VALUES (?, ?, ?, ?)";
			try (PreparedStatement psDet = con.prepareStatement(sqlDet)) {
				for (ItemCarrito item : transaccion.getItems()) {
					psDet.setInt(1, idTx);
					psDet.setInt(2, item.getLibro().getId());
					psDet.setInt(3, item.getCantidad());
					psDet.setDouble(4, item.getLibro().getPrecio());
					psDet.executeUpdate();
				}
			}

			JOptionPane.showMessageDialog(null, "Compra registrada con éxito. Total: $" + transaccion.getTotal());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al registrar compra: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public LinkedList<Transaccion> obtenerTransaccionesPorCliente(int idCliente) {
		LinkedList<Transaccion> transacciones = new LinkedList<>();
		String sqlTx = "SELECT id_transaccion, total FROM transaccion WHERE fk_cliente = ?";

		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement psTx = con.prepareStatement(sqlTx)) {

			psTx.setInt(1, idCliente);
			try (ResultSet rsTx = psTx.executeQuery()) {
				while (rsTx.next()) {
					int idTx = rsTx.getInt("id_transaccion");
					double total = rsTx.getDouble("total");

					// Leer detalle de ítems
					String sqlIt = "SELECT tl.cantidad, tl.precio_unitario, l.id_libro, l.titulo "
							+ "FROM transaccion_libro tl " + "JOIN libro l ON tl.fk_libro = l.id_libro "
							+ "WHERE tl.fk_transaccion = ?";
					List<ItemCarrito> items = new ArrayList<>();
					try (PreparedStatement psIt = con.prepareStatement(sqlIt)) {
						psIt.setInt(1, idTx);
						try (ResultSet rsIt = psIt.executeQuery()) {
							while (rsIt.next()) {
								Libro lib = new Libro(rsIt.getInt("id_libro"), rsIt.getString("titulo"), "", // sinopsis
																												// irrelevante
																												// aquí
										rsIt.getDouble("precio_unitario"), 0, // stock no importa
										"" // estado no importa
								);
								int qty = rsIt.getInt("cantidad");
								items.add(new ItemCarrito(lib, qty));
							}
						}
					}

					// Armar transacción con su lista de ítems
					Cliente cliStub = new Cliente(idCliente, "", "", 0, "", "");
					transacciones.add(new Transaccion(idTx, cliStub, total, items));
				}
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al obtener compras: " + ex.getMessage());
			ex.printStackTrace();
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
		String sql = "SELECT c.id_cliente FROM cliente c " + "JOIN usuario u ON c.fk_usuario = u.id_usuario "
				+ "WHERE u.mail = ?";
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

	public List<Compra> obtenerComprasConDetalle() {
		String sql = """
				    SELECT t.id_transaccion    AS id_cmp,
				           t.fk_cliente        AS id_cliente,
				           u.nombre            AS nombre_cliente,
				           t.total             AS total_cmp,
				           tl.fk_libro         AS id_libro,
				           l.titulo            AS titulo_libro,
				           tl.cantidad,
				           tl.precio_unitario
				      FROM transaccion t
				      JOIN usuario u          ON t.fk_cliente      = u.id_usuario
				      JOIN transaccion_libro tl ON t.id_transaccion = tl.fk_transaccion
				      JOIN libro   l          ON tl.fk_libro       = l.id_libro
				     ORDER BY t.id_transaccion, tl.fk_libro
				""";

		Map<Integer, Compra> mapa = new LinkedHashMap<>();
		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int idCmp = rs.getInt("id_cmp");
				Compra cmp = mapa.get(idCmp);
				if (cmp == null) {
					int idCli = rs.getInt("id_cliente");
					String nomCli = rs.getString("nombre_cliente");
					double tot = rs.getDouble("total_cmp");
					cmp = new Compra(idCmp, idCli, nomCli, tot);
					mapa.put(idCmp, cmp);
				}

				DetalleCompra det = new DetalleCompra(rs.getInt("id_libro"), rs.getString("titulo_libro"),
						rs.getInt("cantidad"), rs.getDouble("precio_unitario"));
				cmp.getDetalles().add(det);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "❌ Error al obtener compras: " + ex.getMessage(), "Error SQL",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}

		return new ArrayList<>(mapa.values());
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

		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idAutor);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					// construyo Libro según tu constructor
					Libro lib = new Libro(rs.getInt("id_libro"), rs.getString("titulo"), rs.getString("sipnosis"),
							rs.getDouble("precio"), rs.getInt("stock"), rs.getString("estado"));
					// asigno el total de unidades vendidas
					lib.setVentas(rs.getInt("ventas"));
					lista.add(lib);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "❌ Error cargando libros/ventas: " + e.getMessage(), "Error SQL",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return lista;
	}

	/** Autor inserta un nuevo proyecto con estado "PENDIENTE" */
	public boolean agregarProyectoLibro(int autorId, Libro libro) throws SQLException {

		String sql = """
				    INSERT INTO libro
				      (titulo, sipnosis, precio, stock, estado, fk_autor)
				    VALUES (?,?,?,?, 'PENDIENTE', ?)
				""";
		try (Connection con = Conexion.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, libro.getTitulo());
			ps.setString(2, libro.getSipnosis());
			ps.setDouble(3, libro.getPrecio());
			ps.setInt(4, libro.getStock());
			ps.setInt(5, autorId);
			return ps.executeUpdate() == 1;
		}
	}

	/** Admin carga todos los proyectos con estado "PENDIENTE" */
	public List<Libro> obtenerProyectosPendientes() throws SQLException {
		String sql = """
				    SELECT id_libro, titulo, sipnosis, precio, stock, estado
				      FROM libro
				     WHERE estado = 'PENDIENTE'
				""";
		List<Libro> lista = new ArrayList<>();
		try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(new Libro(rs.getInt("id_libro"), rs.getString("titulo"), rs.getString("sipnosis"),
						rs.getInt("precio"), rs.getInt("stock"), rs.getString("estado")));
			}
		}
		return lista;
	}

	/** Admin aprueba o rechaza un proyecto cambiando su estado */
	public boolean actualizarEstadoProyecto(int idLibro, String nuevoEstado) throws SQLException {
		String sql = """
				    UPDATE libro
				       SET estado = ?
				     WHERE id_libro = ?
				""";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setString(1, nuevoEstado); // "APROBADO" o "RECHAZADO"
			ps.setInt(2, idLibro);
			return ps.executeUpdate() == 1;
		}
	}

	/** Autor consulta todos sus proyectos, con su estado actual */
	public List<Libro> obtenerProyectosDeAutor(int autorId) throws SQLException {
		String sql = """
				    SELECT id_libro, titulo, sipnosis, precio, stock, estado
				      FROM libro
				     WHERE fk_autor = ?
				""";
		List<Libro> lista = new ArrayList<>();
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setInt(1, autorId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(new Libro(rs.getInt("id_libro"), rs.getString("titulo"), rs.getString("sipnosis"),
							rs.getInt("precio"), rs.getInt("stock"), rs.getString("estado")));
				}
			}
		}
		return lista;
	}

	/** Carga todos los libros "en venta" (estado = "APROBADO") */
	public List<Libro> obtenerLibrosEnVenta() throws SQLException {
		String sql = """
				    SELECT id_libro, titulo, sipnosis, precio, stock, estado
				      FROM libro
				     WHERE estado = 'APROBADO'
				""";
		List<Libro> lista = new ArrayList<>();
		try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				lista.add(new Libro(rs.getInt("id_libro"), rs.getString("titulo"), rs.getString("sipnosis"),
						rs.getInt("precio"), rs.getInt("stock"), rs.getString("estado")));
			}
		}
		return lista;
	}
	/** Sólo devuelve libros con estado = 'APROBADO' */
	public List<Libro> obtenerLibrosDisponibles() throws SQLException {
	    String sql = """
	        SELECT id_libro, titulo, sipnosis, precio, stock, estado
	          FROM libro
	         WHERE estado = 'APROBADO'
	    """;
	    List<Libro> lista = new ArrayList<>();
	    try (PreparedStatement ps = getConnection().prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            lista.add(new Libro(
	                rs.getInt("id_libro"),
	                rs.getString("titulo"),
	                rs.getString("sipnosis"),
	                rs.getDouble("precio"),
	                rs.getInt("stock"),
	                rs.getString("estado")
	            ));
	        }
	    }
	    return lista;
	}

}
