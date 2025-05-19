package BLL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import DLL.ControllerUsuario;
import repository.Encriptador;
import repository.TiposUsuario;

public abstract  class Usuario implements Encriptador {
    private int id;
	private String mail;
	private String nombre;
	private int dni;
	private String password;
	private static LinkedList<Usuario> usuarios = new LinkedList<>();
	private static LinkedList<Cliente> clientes = new LinkedList<>();
	private static LinkedList<Autor> autor = new LinkedList<>();
	
	

   

    // ✅ Constructor con `id` para objetos recuperados de la BD
	  public Usuario(int id, String nombre, String password, int dni, String mail) {
	        this.id = id; // 📌 Verifica que `id` se almacene correctamente
	        this.nombre = nombre;
	        this.password = password;
	        this.dni = dni;
	        this.mail = mail;

	        System.out.println("✅ Usuario creado correctamente con ID: " + this.id); // 📌 Confirmación en consola
	    }



	
	 
	 public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		private static LinkedList<Autor> Indepen = new LinkedList<>();

		public static LinkedList<Autor> getAutor() {
			return autor;
		}

		public static void setAutor(LinkedList<Autor> autor) {
			Usuario.autor = autor;
		}

		public static LinkedList<Autor> getIndepen() {
			return Indepen;
		}

		public static void setIndepen(LinkedList<Autor> indepen) {
			Indepen = indepen;
		}

	public String getNombre() {

		return nombre;
	}

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", password=" + password + "]";
	}

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public static LinkedList<Usuario> getUsuarios() {
		return usuarios;
	}

	public static void setUsuarios(LinkedList<Usuario> usuarios) {
		Usuario.usuarios = usuarios;
	}

	public static LinkedList<Cliente> getClientes() {
		return clientes;
	}

	public static void setClientes(LinkedList<Cliente> clientes) {
		Usuario.clientes = clientes;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Verifica si la contraseña ingresada coincide
	public boolean verificarPassword(String pass) {
		return this.password.equals(pass);
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	// Método abstracto para identificar el tipo de usuario
	public abstract String getTipoUsuario();

	// Cada subclase implementa su propio menú
	public abstract void mostrarMenu();

	// Método para verificar si el email es único
	public static boolean esEmailUnico(String email) {
		for (Usuario usuario : usuarios) {
			if (usuario.getMail().equalsIgnoreCase(email)) {
				return false; // Email ya existe
			}
		}
		return true;
	}

	public static boolean esDniUnico(int dni) {
	    for (Usuario usuario : usuarios) {
	        if (usuario.getDni() == dni) { // Comparar DNI con los existentes
	            return false; // El DNI ya existe
	        }
	    }
	    return true; // El DNI es único
	}
	
	
	


	// 📌 Método para capturar datos y enviarlos al controlador
	// 📌 Método para capturar datos y enviarlos al controlador
	public static void registrarUsuario(ControllerUsuario controller) {
	    String nombre = JOptionPane.showInputDialog("Ingrese su nombre:");
	    String mail = JOptionPane.showInputDialog("Ingrese su correo:");
	    int dni = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su DNI:"));
	    String password = JOptionPane.showInputDialog("Ingrese su contraseña:");

	    // 📌 Obtener el próximo ID disponible en la BD
	    int nuevoId = controller.obtenerNuevoIdUsuario();
	    System.out.println("✅ ID asignado: " + nuevoId); // 📌 Verificación en consola

	    // 📌 Preguntar el tipo de usuario
	    String[] opciones = {"Administrador", "Cliente", "Autor"};
	    int seleccion = JOptionPane.showOptionDialog(null, "Seleccione el tipo de usuario", "Registro",
	            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

	    String tipoUsuario = opciones[seleccion]; // Guardar la selección del usuario

	    // 📌 Instanciar el tipo de usuario correcto
	    Usuario nuevoUsuario = null;
	    String datoAdicional1 = "";
	    String datoAdicional2 = "";

	    switch (tipoUsuario.toLowerCase()) {
	        case "administrador":
	            datoAdicional1 = JOptionPane.showInputDialog("Ingrese su apellido:");
	            nuevoUsuario = new Administrador(nuevoId, nombre, mail, dni, password, datoAdicional1);
	            break;
	        case "cliente":
	            datoAdicional1 = JOptionPane.showInputDialog("Ingrese su dirección:");
	            nuevoUsuario = new Cliente(nuevoId, nombre, password, dni, mail, datoAdicional1);
	            break;
	        case "autor":
	            boolean independiente = JOptionPane.showConfirmDialog(null, "¿Es autor independiente?", "Autor Independiente",
	                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	            datoAdicional1 = String.valueOf(independiente);
	            datoAdicional2 = independiente ? "Independiente" : JOptionPane.showInputDialog("Ingrese el nombre de la editorial:");

	            nuevoUsuario = new Autor(nuevoId, nombre, password, dni, mail, independiente, datoAdicional2); // 📌 Se pasa `nuevoId`
	            break;
	        default:
	            JOptionPane.showMessageDialog(null, "Error: Tipo de usuario inválido.");
	            return;
	    }

	    // 📌 Guardar el usuario con su tipo
	    controller.agregarUsuario(nuevoUsuario, tipoUsuario, datoAdicional1, datoAdicional2);
	    JOptionPane.showMessageDialog(null, "✅ Usuario registrado correctamente.");
	}


	public void mostrarLibros() {
	    ControllerUsuario controller = new ControllerUsuario();
	    LinkedList<Libro> libros = controller.obtenerLibros();

	    if (libros.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "📭 No hay libros registrados en la base de datos.");
	        return;
	    }

	    StringBuilder lista = new StringBuilder("📚 Lista de libros disponibles:\n\n");

	    for (Libro libro : libros) {
	        lista.append("🔹 **Título:** ").append(libro.getTitulo()).append("\n")
	             .append("   📖 **Sinopsis:** ").append(libro.getsipnosis()).append("\n")
	             .append("   💰 **Precio:** ").append(libro.getPrecio()).append("\n")
	             .append("   📦 **Stock:** ").append(libro.getStock()).append("\n")
	             .append("   🏷️ **Estado:** ").append(libro.getEstado()).append("\n\n");
	    }

	    JOptionPane.showMessageDialog(null, lista.toString(), "Libros Disponibles", JOptionPane.INFORMATION_MESSAGE);
	}

	public static String validarCaracteres(String mensaje) {
		String palabra = "";
		boolean flag;
		do {
			palabra = JOptionPane.showInputDialog(mensaje);
			if (palabra == null) {
				palabra = "";
			}

			flag = true;
			for (int i = 0; i < palabra.length(); i++) {
				if (!Character.isLetter(palabra.charAt(i))) {
					flag = false;
					JOptionPane.showMessageDialog(null, "Por favor ingrese solo letras.");
					break;
				}
			}
		} while (palabra.isEmpty() || !flag);

		return palabra;
	}

	public static int validarNumeros(String mensaje) {
		boolean flag;
		String num = "";
		do {
			flag = true;
			num = JOptionPane.showInputDialog(mensaje);
			while (num.isEmpty()) {
				num = JOptionPane.showInputDialog(mensaje);
			}
			for (int i = 0; i < num.length(); i++) {
				if (!Character.isDigit(num.charAt(i))) {
					flag = false;
					break;
				}
			}
		} while (!flag);

		return Integer.parseInt(num);
	}

	public static int validarDni(String mensaje) {
		boolean flag;
		String num = "";
		do {
			flag = true;
			num = JOptionPane.showInputDialog(mensaje);
			while (num == null || num.isEmpty()) { // Validar que no esté vacío
				num = JOptionPane.showInputDialog(mensaje);
			}
			if (num.length() != 8) { // Verificar que tenga exactamente 8 dígitos
				JOptionPane.showMessageDialog(null, "El DNI debe tener 8 números");
				flag = false;
			} else {
				for (int i = 0; i < num.length(); i++) {
					if (!Character.isDigit(num.charAt(i))) { // Verificar que solo contenga números
						flag = false;
						JOptionPane.showMessageDialog(null, "El DNI debe contener solo números");
						break;
					}
				}
				if (flag && !esDniUnico(Integer.parseInt(num))) { // Verificar que el DNI sea único
					flag = false;
					JOptionPane.showMessageDialog(null, "El DNI ingresado ya está en uso. Intente con otro.");
				}
			}
		} while (!flag);

		return Integer.parseInt(num); // Convertir a entero y retornar
	}

	public static String validarEmail(String mensaje) {
		String email;
		do {
			email = JOptionPane.showInputDialog(mensaje);
			if (email == null || email.trim().isEmpty() || !email.contains("@") || !esEmailUnico(email)) {
				JOptionPane.showMessageDialog(null, "Debe ingresar un email válido y único.");
				email = null; // Forzar repetición del bucle
			}
		} while (email == null);
		return email.trim();
	}
	
}
