import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

public abstract class Usuario {
	private String mail;
	private String nombre;
	private int dni;
	private String password;
	private static LinkedList<Usuario> usuarios = new LinkedList<>();
	private static LinkedList<Cliente> clientes = new LinkedList<>();
	private static LinkedList<Autor> autor = new LinkedList<>();
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

	public Usuario(String nombre, String password, int dni, String mail) {
		this.nombre = nombre;
		this.password = password;
		this.mail = mail;
		this.dni = dni;
		usuarios.add(this);
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
	
	public static void ActualizarBios(){
		
		// Crear un Administrador predeterminado
		Administrador  admin = new Administrador("SuperAdmin", "admin@libreria.com", 12345678, "admin123");
		Usuario.getUsuarios().add(admin); // Agregar el Administrador a la lista global
	}
	


	public static void crearUsuario() {

		int opcion;

		opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Gestión de Usuarios",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TiposUsuario.values(),
				TiposUsuario.values()[0]);

		switch (opcion) {
		case 0: // Crear Cliente
			// Solicitar datos del cliente
			String nombre = validarCaracteres("Ingrese el nombre del cliente:");
			int dni = validarDni("Ingrese el DNI del cliente:");
			String password = JOptionPane.showInputDialog("Ingrese la contraseña del cliente:");
			String direccion = validarCaracteres("Ingrese su dirección:");
			String email = validarEmail("Ingrese el email del cliente:");

			// Crear y agregar cliente
			Cliente nuevoCliente = new Cliente(nombre, password, dni, email, direccion);
			clientes.add(nuevoCliente); // Lista global 'clientes'
			JOptionPane.showMessageDialog(null, "Cliente creado exitosamente.");
			break;

		case 1: // Crear Autor Independiente
			// Solicitar datos básicos del autor independiente
			nombre = validarCaracteres("Ingrese el nombre del autor:");
			dni = validarDni("Ingrese el DNI del autor:");
			password = JOptionPane.showInputDialog("Ingrese la contraseña del autor:");
			email = validarEmail("Ingrese el email del cliente:");

			// Crear y agregar autor independiente
			Autor autorIndependiente = new Autor(nombre, password, dni, email, true);
			Indepen.add(autorIndependiente); // Lista global 'autores'

			JOptionPane.showMessageDialog(null, "Autor independiente creado exitosamente.");
			break;

		case 2: // Crear Autor Tradicional
			// Solicitar datos básicos del autor tradicional
			nombre = validarCaracteres("Ingrese el nombre del autor:");
			dni = validarDni("Ingrese el DNI del autor:");
			password = JOptionPane.showInputDialog("Ingrese la contraseña del autor:");
			email = validarEmail("Ingrese el email del cliente:");

			// Solicitar la editorial
			String editorial = validarCaracteres("Ingrese el nombre de la editorial:");
			if (editorial != null && !editorial.trim().isEmpty()) {
				Autor autorTradicional = new Autor(nombre, password, dni, email, false, editorial);
				autorTradicional.setEditorial(editorial); // Método para establecer la editorial
				autor.add(autorTradicional); // Lista global 'autores'
				JOptionPane.showMessageDialog(null, "Autor tradicional creado exitosamente.");
			} else {
				JOptionPane.showMessageDialog(null, "Editorial no ingresada. No se pudo crear el autor tradicional.");
			}

		case 3: // Salir
			JOptionPane.showMessageDialog(null, "Saliendo del sistema de creacion de usuarios.");
			break;

		default:
			JOptionPane.showMessageDialog(null, "Opción no válida.");
			break;
		}

	}

	// Método para iniciar sesión
	public static void iniciarSesion() {
		String email = JOptionPane.showInputDialog("Ingrese su email:");
		String contrasena = JOptionPane.showInputDialog("Ingrese su contraseña:");

		if (email == null || contrasena == null || email.trim().isEmpty() || contrasena.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe ingresar ambos datos para iniciar sesión.");
			return;
		}

		Usuario usuarioLogueado = null;

		// Buscar al usuario en la lista global
		for (Usuario usuario : usuarios) {
			if (usuario.getMail().equalsIgnoreCase(email) && usuario.verificarPassword(contrasena)) {
				usuarioLogueado = usuario;
				break;
			}
		}

		if (usuarioLogueado != null) {
			JOptionPane.showMessageDialog(null,
					"Bienvenido " + usuarioLogueado.getNombre() + "\nTipo: " + usuarioLogueado.getTipoUsuario());
			usuarioLogueado.mostrarMenu(); // Polimorfismo: llama al menú correspondiente
		} else {
			JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
		}
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
