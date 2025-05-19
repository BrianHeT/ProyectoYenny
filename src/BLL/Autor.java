package BLL;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import repository.TipoOpcionAutor;

public class Autor extends Usuario {
	private boolean independiente;
    private String editorial;
    private LinkedList<Libro> librosEnviados;

    // ✅ Corrección: Agregar `id` en el constructor y pasarlo a `Usuario`
    public Autor(int id, String nombre, String password, int dni, String mail, boolean independiente, String editorial) {
        super(id, nombre, password, dni, mail); // 📌 Ahora `id` se asigna correctamente
        this.independiente = independiente;
        this.editorial = editorial;
        this.librosEnviados = new LinkedList<>(); // 📌 Se inicializa correctamente
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 public void mostrarMenu2() {
	        int opcion;
	        do {
	            opcion = JOptionPane.showOptionDialog(null, "Menú de Autor", "Opciones de Publicación",
	                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
	                    new String[]{"Gestionar Libros", "Ver Estadísticas", "Publicar Historia", "Salir"}, "Gestionar Libros");

	            switch (opcion) {
	                case 0:
	                    JOptionPane.showMessageDialog(null, "Accediendo a gestión de libros...");
	                    // Implementar lógica de gestión de libros
	                    break;
	                case 1:
	                    JOptionPane.showMessageDialog(null, "Mostrando estadísticas...");
	                    // Implementar lógica de estadísticas
	                    break;
	                case 2:
	                    break;
	                case 3:
	                    JOptionPane.showMessageDialog(null, "Cerrando sesión de autor...");
	                    break;
	            }
	        } while (opcion != 3);
	    }

}
