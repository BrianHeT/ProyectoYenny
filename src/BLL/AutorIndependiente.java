package BLL;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class AutorIndependiente extends Autor {
	private int id;
    public AutorIndependiente(String nombre, String password, int dni, String mail, boolean independiente,
			String editorial, int id) {
		super(nombre, password, dni, mail, independiente, editorial);
		this.id = id;
        this.librosEnviados = new LinkedList<>();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLibrosEnviados(LinkedList<Libro> librosEnviados) {
		this.librosEnviados = librosEnviados;
	}

	private LinkedList<Libro> librosEnviados;

    public AutorIndependiente(String nombre, String password, int dni, String mail) {
        super(nombre, password, dni, mail, true); // true = independiente
        this.librosEnviados = new LinkedList<>();
    }

    @Override
    public String getTipoUsuario() {
        return "Autor Independiente";
    }

    @Override
    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            String opcion = JOptionPane.showInputDialog(
                "Menú Autor Independiente:\n" +
                "1) Enviasfdssdsdsdr nuevo libro\n" +
                "2) Ver mis libros enviados\n" +
                "3) Salir"
            );

            if (opcion == null) break;

            switch (opcion) {
                case "1":
                    enviarLibro();
                    break;
                case "2":
                    verLibros();
                    break;
                case "3":
                    salir = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
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
