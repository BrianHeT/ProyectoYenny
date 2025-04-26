
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Cliente extends Usuario {

	private String direccion;
	private Carrito Carrito;

	

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

	public Cliente(String nombre, String password, int dni, String mail, String direccion) {
		super(nombre, password, dni, mail);
		this.direccion = direccion;
		this.Carrito = new Carrito(); // Inicializamos el carrito
	}

	@Override
	public String getTipoUsuario() {
		return "Cliente";
	}

	@Override
	public void mostrarMenu() {
		boolean salir = false;
		
		
		int opcion;
		do {
		
			 opcion= JOptionPane.showOptionDialog(null, "Seleccione una opción", "Gestión de Usuarios",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TipoOpcionCliente.values(),
				TipoOpcionCliente.values()[0]);
		
			
				
			
				
			switch (opcion) {
			case 0:
				JOptionPane.showMessageDialog(null, "Mostrando Catálogo... [Prototipo]");
				mostrarCatalogo();
				 agregarLibroAlCarrito();
				break;
			case 1:
				JOptionPane.showMessageDialog(null, "Mostrando Promociones... [Prototipo]");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Ver Carrito... [Prototipo]");
				verLibrosDelCarrito();
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "Realizando Compra... [Prototipo]");
				realizarPago();
				break;
			case 4:
				JOptionPane.showMessageDialog(null, "Ver Estado de envio... [Prototipo]");
				break;
			case 5:
				JOptionPane.showMessageDialog(null, "Ver mis compras... [Prototipo]");
				break;
			case 6:
				JOptionPane.showMessageDialog(null, "Cambios y devoluciones... [Prototipo]");
				break;
			case 7:
				JOptionPane.showMessageDialog(null, "Salir...");
				break;
			default:
				break;
			}
			} while (opcion!=7);
		
	}
	// metodo para ver la lista de libros --aun no implementado--
	  private void mostrarCatalogo() {
	        // Se crean algunos libros de ejemplo
	        Libro libro1 = new Libro("Cien Años de Soledad", "Gabriel García Márquez", 25.50);
	        Libro libro2 = new Libro("1984", "George Orwell", 20.00);
	        Libro libro3 = new Libro("El Principito", "Antoine de Saint-Exupéry", 15.75);
	        
	        String catalogo = "Catálogo de Libros:\n"
	                        + "1) " + libro1.toString() + "\n"
	                        + "2) " + libro2.toString() + "\n"
	                        + "3) " + libro3.toString();
	        JOptionPane.showMessageDialog(null, catalogo, "Catálogo", JOptionPane.INFORMATION_MESSAGE);
	    }
		
		 // Método para agregar un libro al carrito
	    private void agregarLibroAlCarrito() {
	        // Para este prototipo, pediremos al usuario que ingrese el número del libro del catálogo
	        String opcion = JOptionPane.showInputDialog("Ingrese el número del libro a agregar (1, 2 o 3):");
	        int num;
	        try {
	            num = Integer.parseInt(opcion.trim());
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(null, "Opción inválida. Debe ser un número.");
	            return;
	        }
	        Libro libroSeleccionado = null;
	        switch (num) {
	            case 1:
	                libroSeleccionado = new Libro("Cien Años de Soledad", "Gabriel García Márquez", 25.50);
	                break;
	            case 2:
	                libroSeleccionado = new Libro("1984", "George Orwell", 20.00);
	                break;
	            case 3:
	                libroSeleccionado = new Libro("El Principito", "Antoine de Saint-Exupéry", 15.75);
	                break;
	            default:
	                JOptionPane.showMessageDialog(null, "Libro no encontrado en el catálogo.");
	                return;
	        }
	        String cant = JOptionPane.showInputDialog("Ingrese la cantidad de unidades:");
	        int cantidad;
	        try {
	            cantidad = Integer.parseInt(cant.trim());
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(null, "Cantidad inválida.");
	            return;
	        }
	        Carrito.agregarItem(libroSeleccionado, cantidad);
	        JOptionPane.showMessageDialog(null, "Libro agregado al carrito.");
	    }
	    
	    // Método para "realizar el pago"
	    private void realizarPago() {
	        double total = Carrito.calcularTotal();
	        int confirmar = JOptionPane.showConfirmDialog(null, "El total a pagar es: $" + total + "\n¿Desea proceder con el pago?", "Confirmar Pago", JOptionPane.YES_NO_OPTION);
	        if (confirmar == JOptionPane.YES_OPTION) {
	            JOptionPane.showMessageDialog(null, "Pago realizado. ¡Gracias por su compra!");
	            Carrito.vaciar(); // Después del pago se vacía el carrito
	        } else {
	            JOptionPane.showMessageDialog(null, "Pago cancelado.");
	        }
	    }
	    public void verLibrosDelCarrito() {
	        if (Carrito.getItems().isEmpty()) {
	            JOptionPane.showMessageDialog(null, "El carrito está vacío.", "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(null, Carrito.toString(), "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
	        }
	    }

}
