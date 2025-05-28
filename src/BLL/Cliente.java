package BLL;

import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import DLL.ControllerUsuario;
import repository.TipoOpcionCliente;

public class Cliente extends Usuario {
	
	private int id;
	private String direccion;
	private Carrito Carrito;
	
	 public Cliente(int id, String nombre, String password, int dni, String mail, String direccion) {
	        super(id, nombre, password, dni, mail);
	        this.direccion = direccion;
	    }




	public Cliente(String nombre, String password, int dni, String mail, String direccion) {
		super(dni, nombre, password, dni, mail);
		this.direccion = direccion;
		this.Carrito = new Carrito(); // Inicializamos el carrito
	}

	

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Carrito getCarrito() {
		return Carrito;
	}

	public void setCarrito(Carrito carrito) {
		Carrito = carrito;
	}

	

	@Override
	public String getTipoUsuario() {
		return "Cliente";
	}

	
	@Override
	public void mostrarMenu() {        
	    ControllerUsuario controller = new ControllerUsuario(); // Instancia de ControllerUsuario
	    int opcion;

	    do {
	        opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Gestión de Usuarios",
	                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TipoOpcionCliente.values(),
	                TipoOpcionCliente.values()[0]);

	        switch (opcion) {
	            case 0:
	                JOptionPane.showMessageDialog(null, "Mostrando Catálogo...");
	                verLibrosDisponibles();
	              	LinkedList<Libro> libros = controller.obtenerLibros();

	            	if (libros.isEmpty()) {
	            	    JOptionPane.showMessageDialog(null, "No hay libros disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
	            	    return;
	            	}

	            	// Crear un array con los títulos de los libros
	            	String[] opcionesLibros = new String[libros.size()];
	            	for (int i = 0; i < libros.size(); i++) {
	            	    opcionesLibros[i] = libros.get(i).getId() + " - " + libros.get(i).getTitulo(); // ID y título
	            	}

	            	// Mostrar lista desplegable
	            	JComboBox<String> comboLibros = new JComboBox<>(opcionesLibros);
	            	JOptionPane.showMessageDialog(null, comboLibros, "Seleccione un libro", JOptionPane.QUESTION_MESSAGE);

	            	int indexSeleccionado = comboLibros.getSelectedIndex();
	            	if (indexSeleccionado == -1) {
	            	    JOptionPane.showMessageDialog(null, "No se seleccionó ningún libro.", "Error", JOptionPane.ERROR_MESSAGE);
	            	    return;
	            	}

	            	// Obtener el libro seleccionado
	            	Libro libroSeleccionado = libros.get(indexSeleccionado);

	            	// Pedir cantidad
	            	String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad:");
	            	try {
	            	    int cantidad = Integer.parseInt(cantidadStr);
	            	    agregarLibroAlCarrito(libroSeleccionado.getId(), cantidad); // Agregar al carrito
	            	    JOptionPane.showMessageDialog(null, "Libro agregado: " + libroSeleccionado.getTitulo() + " x " + cantidad);
	            	} catch (NumberFormatException e) {
	            	    JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
	            	}

	            case 1:
	            	

	            case 2:
	            	verLibrosDelCarrito();
	                break;

	            case 3:
	                realizarCompra(); // Llamar al método de compra
	                break;

	            case 4:
	                JOptionPane.showMessageDialog(null, "Ver Estado de Envío... [Prototipo]");
	                break;

	            case 5:
	                JOptionPane.showMessageDialog(null, "Ver Mis Compras... ");
	                verMisCompras(); // Ver historial de compras

	                break;

	            case 6:
	                JOptionPane.showMessageDialog(null, "Cambios y Devoluciones... [Prototipo]");
	                break;

	            case 7:
	                JOptionPane.showMessageDialog(null, "Saliendo del sistema...");
	                break;

	            default:
	                break;
	        }
	    } while (opcion != 7);
	}
	// metodo para ver la lista de libros --aun no implementado--
	
		
		 // Método para agregar un libro al carrito
	 

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
	    public void agregarLibroAlCarrito(int idLibro, int cantidad) {
	        ControllerUsuario controller = new ControllerUsuario();
	        LinkedList<Libro> libros = controller.obtenerLibros();

	        for (Libro libro : libros) {
	            if (libro.getId() == idLibro) {
	                Carrito.agregarItem(libro, cantidad);
	                JOptionPane.showMessageDialog(null, "Libro agregado al carrito: " + libro.getTitulo());
	                return;
	            }
	        }
	        JOptionPane.showMessageDialog(null, "Libro no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    public void realizarCompra() {
	        if (Carrito.getItems().isEmpty()) {
	            JOptionPane.showMessageDialog(null, "El carrito está vacío. No puedes realizar una compra.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        GestionPagos gestionPagos = GestionPagos.getInstance(); // Obtener instancia de GestionPagos
	        gestionPagos.realizarPago(this); // Enviar el cliente para que gestione la compra
	    }
	    public void verLibrosDelCarrito() {
	        if (Carrito.getItems().isEmpty()) {
	            JOptionPane.showMessageDialog(null, "El carrito está vacío.", "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
	            return;
	        }

	        StringBuilder contenidoCarrito = new StringBuilder("Libros en tu carrito:\n");
	        for (ItemCarrito item : Carrito.getItems()) {
	            contenidoCarrito.append("- ").append(item.getLibro().getTitulo())
	                .append(" x ").append(item.getCantidad())
	                .append(" ($").append(item.getLibro().getPrecio()).append(" c/u)\n");
	        }
	        
	        contenidoCarrito.append("\nTotal: $").append(Carrito.calcularTotal());
	        JOptionPane.showMessageDialog(null, contenidoCarrito.toString(), "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
	    }
	    public void verMisCompras() {
	        ControllerUsuario controller = new ControllerUsuario();
	        LinkedList<Transaccion> transacciones = controller.obtenerTransaccionesPorCliente(this.getId());

	        if (transacciones.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "Aún no has realizado compras.", "Mis Compras", JOptionPane.INFORMATION_MESSAGE);
	            return;
	        }

	        StringBuilder historialCompras = new StringBuilder("Historial de Compras:\n");
	        for (Transaccion transaccion : transacciones) {
	            historialCompras.append("- Compra por $").append(transaccion.getTotal()).append("\n");
	        }

	        JOptionPane.showMessageDialog(null, historialCompras.toString(), "Mis Compras", JOptionPane.INFORMATION_MESSAGE);
	    }

}
