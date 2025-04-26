
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Autor extends Usuario {
	private boolean independiente;
	private String editorial;
	private LinkedList<Libro> librosEnviados;

	public Autor(String nombre, String password, int dni, String mail, boolean independiente, String editorial) {
		super(nombre, password, dni, mail);
		this.independiente = independiente;
		this.editorial = editorial;
		this.librosEnviados = librosEnviados;
	}

	public Autor(String nombre, String password, int dni, String mail, boolean independiente) {
		super(nombre, password, dni, mail);
		this.independiente = independiente;
		this.editorial = editorial;
		this.librosEnviados = librosEnviados;

	}

	public void setLibrosEnviados(LinkedList<Libro> librosEnviados) {
		this.librosEnviados = librosEnviados;
	}

	public boolean isIndependiente() {
		return independiente;
	}

	public void setIndependiente(boolean independiente) {
		this.independiente = independiente;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	@Override
	public String getTipoUsuario() {
		return "Autor" + (independiente ? " (Independiente)" : " (Tradicional)");
	}

	@Override

	public void mostrarMenu() {

		int opcion;
		do {

			opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Gestión de Usuarios",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TipoOpcionAutor.values(),
					TipoOpcionAutor.values()[0]);

			switch (opcion) {
			case 0:
				JOptionPane.showMessageDialog(null, "Enviando Proyecto... [Prototipo]");
				// enviarLibro();
				break;
			case 1:
				JOptionPane.showMessageDialog(null, "Mostrando Proyectos... [Prototipo]");
				// verLibros();
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Ver estado de proyecto... [Prototipo]");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "saliendo...");
				break;
			default:
				break;
			}

		} while (opcion != 3);

	}

	public void enviarLibro() {
		String titulo = Usuario.validarCaracteres("Ingrese el título del libro:");
		String sinopsis = JOptionPane.showInputDialog("Ingrese una sinopsis:");
		double precio = 0;

		try {
			precio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el precio:"));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Precio inválido.");
			return;
		}

		Libro nuevoLibro = new Libro(titulo, sinopsis, precio);
		librosEnviados.add(nuevoLibro);
		JOptionPane.showMessageDialog(null, "Libro enviado correctamente.");
	}

	public void verLibros() {
		if (librosEnviados.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No has enviado ningún libro aún.");
			return;
		}

		String mensaje = "Tus libros enviados:\n\n";
		for (Libro libro : librosEnviados) {
			mensaje += libro.toString() + "\n\n";
		}

		JOptionPane.showMessageDialog(null, mensaje);
	}

	public LinkedList<Libro> getLibrosEnviados() {
		return librosEnviados;
	}
}
