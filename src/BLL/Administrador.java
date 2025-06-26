package BLL;

import java.awt.Frame;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import DLL.ControllerUsuario;

import javax.swing.JOptionPane;

public class Administrador extends Usuario {

	private String apellido;

	public Administrador(int id, String nombre, String password, int dni, String mail, String apellido) {
		super(id, nombre, password, dni, mail);
		this.apellido = apellido;

		System.out.println("✅ Administrador creado con ID en objeto (después de asignación): " + this.getId()); // 📌
																												// Verificación
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	@Override
	public String getTipoUsuario() {
		return "Administrador";
	}

	@Override
	public void mostrarMenu() {
		boolean salir = false;
		while (!salir) {
			String[] opciones = { "Ver Usuarios", "Modificar Usuario", "Eliminar Usuario", "Ver libros",
					"Agregar Libro", "Modificar Libro", "Eliminar Libro", "Ver Compras", "Salir" };
			int opcion = JOptionPane.showOptionDialog(null, "Menú Administrador", "Elige una opción",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
			switch (opcion) {
			case 0:
				mostrarUsuarios();
				break;
			case 1:
				mostrarUsuariosYModificar();
				break;
			case 2:
				eliminarUsuario();
				break;
			case 3:
				verLibrosDisponibles();
				break;
			case 4:
				crearLibro();

				break;
			case 5:
				modificarLibro();
				break;
			case 6:
				eliminarLibro();
				break;
			case 7:
				verComprasConDetalle();
				break;
			case 8:
				salir = true;
				break;
			default:
				JOptionPane.showMessageDialog(null, "Opción no válida.");
				break;
			}
		}
	}

	public Usuario seleccionarUsuario() {
		ControllerUsuario controller = new ControllerUsuario();
		LinkedList<Usuario> usuarios = controller.obtenerUsuarios();

		if (usuarios.isEmpty()) {
			JOptionPane.showMessageDialog(null, "📭 No hay usuarios registrados.");
			return null;
		}

		String[] nombresUsuarios = new String[usuarios.size()];
		for (int i = 0; i < usuarios.size(); i++) {
			nombresUsuarios[i] = usuarios.get(i).getNombre() + " - " + usuarios.get(i).getMail();
		}

		String seleccion = (String) JOptionPane.showInputDialog(null, "🔍 Seleccione un usuario:", "Elegir Usuario",
				JOptionPane.QUESTION_MESSAGE, null, nombresUsuarios, nombresUsuarios[0]);

		if (seleccion == null) {
			return null; // 📌 Cancelado por el usuario
		}

		for (Usuario usuario : usuarios) {
			System.out
					.println("🔍 Verificando usuario: " + usuario.getNombre() + " | ID en objeto: " + usuario.getId());

			if (usuario.getId() == 0) {
				System.out.println(
						"❌ Error: El usuario tiene ID = 0, revisa `obtenerUsuarios()` y `Administrador.java`.");
			}

			if (seleccion.contains(usuario.getMail())) {
				if (usuario instanceof Administrador) {
					return new Administrador(usuario.getId(), usuario.getNombre(), usuario.getPassword(),
							usuario.getDni(), usuario.getMail(), ((Administrador) usuario).getApellido());
				} else if (usuario instanceof Cliente) {
					return new Cliente(usuario.getId(), usuario.getNombre(), usuario.getPassword(), usuario.getDni(),
							usuario.getMail(), ((Cliente) usuario).getDireccion());
				} else if (usuario instanceof Autor) {
					Autor autor = (Autor) usuario;
					return new Autor(usuario.getId(), usuario.getNombre(), usuario.getPassword(), usuario.getDni(),
							usuario.getMail(), autor.isIndependiente(), autor.getEditorial());
				}
			}
		}

		return null;
	}

	public void verComprasConDetalle() {
	    List<Compra> compras = controller.obtenerComprasConDetalle();
	    if (compras.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay compras registradas.");
	        return;
	    }

	    // 1) Modelo y tabla de RESUMEN
	    String[] colsRes = { "ID Compra", "Cliente", "Total" };
	    DefaultTableModel mdlRes = new DefaultTableModel(colsRes, 0);
	    for (Compra c : compras) {
	        mdlRes.addRow(new Object[]{
	            c.getIdCompra(),
	            c.getNombreCliente(),
	            c.getTotalCabecera()
	        });
	    }
	    JTable tblRes = new JTable(mdlRes);
	    tblRes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    // 2) Modelo y tabla de DETALLE
	    String[] colsDet = { "Libro", "Cantidad", "Precio U.", "Subtotal" };
	    DefaultTableModel mdlDet = new DefaultTableModel(colsDet, 0);
	    JTable tblDet = new JTable(mdlDet);

	    // 3) Listener para actualizar detalle
	    tblRes.getSelectionModel().addListSelectionListener(e -> {
	        if (e.getValueIsAdjusting()) return;
	        int idx = tblRes.getSelectedRow();
	        if (idx < 0) return;
	        Compra sel = compras.get(idx);
	        mdlDet.setRowCount(0);
	        for (DetalleCompra d : sel.getDetalles()) {
	            mdlDet.addRow(new Object[]{
	                d.getTitulo(),
	                d.getCantidad(),
	                d.getPrecioUnitario(),
	                d.getSubtotal()
	            });
	        }
	    });

	    // 4) Forzar selección inicial para que cargue la primera compra
	    if (tblRes.getRowCount() > 0) {
	        tblRes.setRowSelectionInterval(0, 0);
	    }

	    // 5) Split pane
	    JSplitPane split = new JSplitPane(
	        JSplitPane.VERTICAL_SPLIT,
	        new JScrollPane(tblRes),
	        new JScrollPane(tblDet)
	    );
	    split.setResizeWeight(0.5);
	    split.setDividerLocation(150);

	    // 6) Mostrar en JDialog propio
	    JDialog dlg = new JDialog((Frame) null, "Compras de Clientes", true);
	    dlg.getContentPane().add(split);
	    dlg.setSize(600, 400);
	    dlg.setLocationRelativeTo(null);
	    dlg.setVisible(true);
	}



	public void mostrarUsuariosYModificar() {
		// 1) Traigo siempre la lista fresca desde la BD
		List<Usuario> lista = controller.obtenerUsuarios();
		// —— AÑADE ESTO PARA DEBUG ——
		System.out.println("=== DEBUG LISTA ===");
		lista.forEach(u -> System.out.println(" → Objeto: " + u.getClass().getSimpleName() + " | id interno = "
				+ u.getId() + " | hash=" + System.identityHashCode(u)));
		System.out.println("===================");
		// — — — — — — — — — —

		if (lista.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay usuarios para mostrar.");
			return;
		}
		if (lista.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay usuarios para mostrar.");
			return;
		}

		// 2) Preparo un array con “ID | Nombre” para cada usuario
		String[] opciones = lista.stream().map(u -> u.getId() + " | " + u.getNombre()).toArray(String[]::new);

		// 3) Muestro un dialog con botones, capturo el índice seleccionado
		int sel = JOptionPane.showOptionDialog(null, "Seleccione usuario a modificar", "Modificar Usuario",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
		// Si el usuario cierra o presiona Cancelar:
		if (sel < 0) {
			return;
		}

		// 4) Con ese índice obtengo el objeto real de la lista
		Usuario elegido = lista.get(sel);
		int idUsuario = elegido.getId(); // <- aquí YA DEBE SER 2,13, etc.
		String nombreOld = elegido.getNombre();
		String mailOld = elegido.getMail();

		System.out.println("Usuario seleccionado: " + nombreOld + " | ID Usuario: " + idUsuario);

		// 5) Pido las nuevas propiedades (pueden cancelar cada uno)
		String nuevoNombre = JOptionPane.showInputDialog(null, "Nuevo nombre:", nombreOld);
		String nuevoMail = JOptionPane.showInputDialog(null, "Nuevo email:", mailOld);
		String nuevaPass = JOptionPane.showInputDialog(null, "Nueva contraseña:");

		// 6) Si cancela, mantengo el valor previo
		if (nuevoNombre == null)
			nuevoNombre = nombreOld;
		if (nuevoMail == null)
			nuevoMail = mailOld;
		if (nuevaPass == null)
			nuevaPass = "";

		// 7) Llamo a la BLL para actualizar
		boolean ok = controller.modificarUsuario(idUsuario, nuevoNombre, nuevoMail, nuevaPass);
		if (!ok) {
			JOptionPane.showMessageDialog(null, "❌ No se pudo modificar el usuario.");
		}
	}

	public void crearLibro() {
		String titulo = JOptionPane.showInputDialog("Ingrese el título del libro:");
		String sipnosis = JOptionPane.showInputDialog("Ingrese la sinopsis:");
		int precio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el precio:"));
		int stock = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el stock:"));
		String estado = JOptionPane.showInputDialog("Ingrese el estado del libro:");

		Libro nuevoLibro = new Libro(titulo, sipnosis, precio, stock, estado);
		ControllerUsuario controller = new ControllerUsuario();
		controller.agregarLibro(nuevoLibro); //
	}

	public void verLibrosDisponibles() {
		ControllerUsuario controller = new ControllerUsuario();
		LinkedList<Libro> libros = controller.obtenerLibros(); // 📌 Obtiene los libros desde la BD

		if (libros.isEmpty()) {
			JOptionPane.showMessageDialog(null, "📭 No hay libros registrados en la base de datos.");
			return;
		}

		// 📌 Crear columnas para la tabla
		String[] columnNames = { "Título", "Sipnosis", "Precio", "Stock", "Estado" };
		Object[][] data = new Object[libros.size()][5];

		for (int i = 0; i < libros.size(); i++) {
			Libro libro = libros.get(i);
			data[i][0] = libro.getTitulo();
			data[i][1] = libro.getsipnosis();
			data[i][2] = libro.getPrecio();
			data[i][3] = libro.getStock();
			data[i][4] = libro.getEstado();
		}

		// 📌 Crear la tabla y agregarla a un `JScrollPane`
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);

		// 📌 Mostrar la tabla en un `JOptionPane`
		JOptionPane.showMessageDialog(null, scrollPane, "Libros Disponibles", JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarUsuarios() {
		ControllerUsuario controller = new ControllerUsuario();
		LinkedList<Usuario> usuarios = controller.obtenerUsuarios();

		if (usuarios.isEmpty()) {
			JOptionPane.showMessageDialog(null, "📭 No hay usuarios registrados.");
			return;
		}

		// 📌 Crear columnas para la tabla
		String[] columnNames = { "Nombre", "DNI", "Correo", "Tipo de Usuario" };
		Object[][] data = new Object[usuarios.size()][4];

		for (int i = 0; i < usuarios.size(); i++) {
			Usuario usuario = usuarios.get(i);
			data[i][0] = usuario.getNombre(); // 📌 Nombre
			data[i][1] = usuario.getDni(); // 📌 DNI
			data[i][2] = usuario.getMail(); // 📌 Correo
			data[i][3] = usuario instanceof Autor && ((Autor) usuario).isIndependiente() ? "Autor Independiente"
					: usuario.getTipoUsuario();

			// 📌 Si es autor e independiente, mostrar "Autor Independiente"
			if (usuario instanceof Autor autor && autor.isIndependiente()) {
				data[i][3] = "Autor Independiente";
			} else {
				data[i][3] = usuario.getClass().getSimpleName(); // 📌 Tipo normal de usuario
			}
		}

		// 📌 Crear la tabla y agregarla a un `JScrollPane`
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);

		// 📌 Mostrar la tabla en un `JOptionPane`
		JOptionPane.showMessageDialog(null, scrollPane, "Usuarios Registrados", JOptionPane.INFORMATION_MESSAGE);
	}

	public void eliminarUsuario() {
		Usuario usuarioSeleccionado = seleccionarUsuario();
		if (usuarioSeleccionado == null) {
			JOptionPane.showMessageDialog(null, "❌ No se seleccionó ningún usuario.");
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(null,
				"🗑️ ¿Seguro que quieres eliminar a " + usuarioSeleccionado.getNombre() + "?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION);

		if (confirmacion == JOptionPane.YES_OPTION) {
			ControllerUsuario controller = new ControllerUsuario();
			boolean exito = controller.eliminarUsuario(usuarioSeleccionado.getId());
			if (exito) {
				JOptionPane.showMessageDialog(null, "✅ Usuario eliminado correctamente.");
			} else {
				JOptionPane.showMessageDialog(null, "❌ No se pudo eliminar el usuario.");
			}
		}
	}

	public void modificarLibro() {
		ControllerUsuario controller = new ControllerUsuario();

		Libro libroSeleccionado = seleccionarLibro();
		if (libroSeleccionado == null)
			return;

		String nuevoTitulo = JOptionPane.showInputDialog("🖋️ Nuevo título:", libroSeleccionado.getTitulo());
		String nuevaSipnosis = JOptionPane.showInputDialog("📖 Nueva sinopsis:", libroSeleccionado.getSipnosis());
		double nuevoPrecio = Double
				.parseDouble(JOptionPane.showInputDialog("💰 Nuevo precio:", libroSeleccionado.getPrecio()));
		int nuevoStock = Integer.parseInt(JOptionPane.showInputDialog("📦 Nuevo stock:", libroSeleccionado.getStock()));
		String nuevoEstado = JOptionPane.showInputDialog("🔍 Nuevo estado:", libroSeleccionado.getEstado());

		boolean exito = controller.modificarLibro(libroSeleccionado.getId(), nuevoTitulo, nuevaSipnosis, nuevoPrecio,
				nuevoStock, nuevoEstado);

		if (exito) {
			JOptionPane.showMessageDialog(null, "✅ Libro modificado correctamente.");
		} else {
			JOptionPane.showMessageDialog(null, "❌ No se pudo modificar el libro.");
		}
	}

	private Libro seleccionarLibro() {
		ControllerUsuario controller = new ControllerUsuario();
		LinkedList<Libro> listaLibros = controller.obtenerLibros();

		if (listaLibros.isEmpty()) {
			JOptionPane.showMessageDialog(null, "❌ No hay libros disponibles.");
			return null;
		}

		String[] opciones = new String[listaLibros.size()];
		for (int i = 0; i < listaLibros.size(); i++) {
			opciones[i] = listaLibros.get(i).getTitulo() + " (ID: " + listaLibros.get(i).getId() + ")";
		}

		String seleccion = (String) JOptionPane.showInputDialog(null, "📚 Selecciona un libro:", "Gestión de Libros",
				JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (seleccion != null) {
			for (Libro libro : listaLibros) {
				if (seleccion.contains("(ID: " + libro.getId() + ")")) {
					return libro;
				}
			}
		}

		return null;
	}

	public void eliminarLibro() {
		ControllerUsuario controller = new ControllerUsuario();

		Libro libroSeleccionado = seleccionarLibro();
		if (libroSeleccionado == null)
			return;

		int confirmacion = JOptionPane.showConfirmDialog(null,
				"🗑️ ¿Seguro que quieres eliminar \"" + libroSeleccionado.getTitulo() + "\"?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION);

		if (confirmacion == JOptionPane.YES_OPTION) {
			boolean exito = controller.eliminarLibro(libroSeleccionado.getId());

			if (exito) {
				JOptionPane.showMessageDialog(null, "✅ Libro eliminado correctamente.");
			} else {
				JOptionPane.showMessageDialog(null, "❌ No se pudo eliminar el libro.");
			}
		}
	}
}
