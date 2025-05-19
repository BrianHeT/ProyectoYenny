package BLL;

import java.util.LinkedList;

import javax.swing.JOptionPane;

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
		int opcion;
		do {
		
			 opcion= JOptionPane.showOptionDialog(null, "Seleccione una opción", "Gestión de Usuarios",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TipoOpcionCliente.values(),
				TipoOpcionCliente.values()[0]);
		
			
				
			
				
			switch (opcion) {
			case 0:
				JOptionPane.showMessageDialog(null, "Mostrando Catálogo... [Prototipo]");
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
	
		
		 // Método para agregar un libro al carrito
	 

	    public void verLibrosDelCarrito() {
	        if (Carrito.getItems().isEmpty()) {
	            JOptionPane.showMessageDialog(null, "El carrito está vacío.", "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(null, Carrito.toString(), "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
	        }
	    }

}
