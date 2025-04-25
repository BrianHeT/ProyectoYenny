
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Cliente extends Usuario {

	private String direccion;

	

	public Cliente(String nombre, String password, int dni, String mail, String direccion) {
		super(nombre, password, dni, mail);
		this.direccion = direccion;
	}

	@Override
	public String getTipoUsuario() {
		return "Cliente";
	}

	@Override
	public void mostrarMenu() {
		boolean salir = false;
		while (!salir) {
			String menu = "Menú Cliente:\n" + "1) Ver Catálogo\n" + "2) Ver Carrito\n" + "3) Realizar Compra\n"
					+ "4) Salir";
			String opcion = JOptionPane.showInputDialog(menu);
			if (opcion == null)
				break; // Salir si se cancela
			switch (opcion) {
			case "1":
				JOptionPane.showMessageDialog(null, "Mostrando Catálogo... [Prototipo]");
				verLibrosDisponibles();
				break;
			case "2":
				JOptionPane.showMessageDialog(null, "Mostrando Carrito... [Prototipo]");
				break;
			case "3":
				JOptionPane.showMessageDialog(null, "Realizando Compra... [Prototipo]");
				break;
			case "4":
				salir = true;
				break;
			default:
				JOptionPane.showMessageDialog(null, "Opción no válida");
				break;
			}
		}
	}
	// metodo para ver la lista de libros --aun no implementado--
		public void verLibrosDisponibles() {
		    LinkedList<Libro> disponibles = Libro.getListaLibros();

		    if (disponibles.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "No hay libros disponibles por el momento.");
		        return;
		    }

		    String mensaje = "Libros disponibles:\n\n"
		    		+ "Recomendacion semanal:\n"
		    		+ "'El señor de los anillos'\n"
		    		+ "'Harry Potter'\n"
		    		+ "'Los Juegos del Hambre\n'";
		    for (Libro libro : disponibles) {
		        mensaje += libro.toString() + "\n----------------------\n";
		    }

		    JOptionPane.showMessageDialog(null, mensaje);
		}
}
