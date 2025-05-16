package BLL;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

public class Administrador extends Usuario {

	private int id;
	
	private String apellido;
	
	public Administrador(String nombre, String password, int dni, String mail, int id, String apellido) {
		super(nombre, password, dni, mail);
		this.id = id;
		this.apellido = apellido;
	}

    public Administrador(String nombre, String mail, int dni, String password, String apellido) {
        super(nombre, password, dni, mail); // Pasa los argumentos al constructor de Usuario
		this.apellido = apellido;

    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
            String[] opciones = {"Ver Usuarios", "Modificar Usuario", "Eliminar Usuario","Ver libros","Agregar Libro","Modificar Libro","Eliminar Libro","Ver Compras", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "Menú Administrador", 
                                                    "Elige una opción", 
                                                    JOptionPane.DEFAULT_OPTION, 
                                                    JOptionPane.INFORMATION_MESSAGE, 
                                                    null, opciones, opciones[0]);
            switch (opcion) {
            case 0:
                mostrarUsuarios();
                break;
            case 1:
                modificarUsuario();
                break;
            case 2:
                eliminarUsuario();
                break;
            case 3:
            	Libro.mostrarLibros();
                break;
            case 4:
            	Libro.agregarLibro();
                break;
            case 5:
                Libro.modificarLibro();
                break;
            case 6:
                Libro.eliminarLibro();
                break;
            case 7:
                JOptionPane.showMessageDialog(null, "Ver compras... [Prototipo]");
                GestionPagos.getInstance().mostrarTransacciones(); // Usar la instancia única             
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
    
    // Método para mostrar todos los usuarios registrados
    public void mostrarUsuarios() {
    	StringBuilder listaUsuarios = new StringBuilder("Lista de Usuarios Creados:\n");
    	for (Usuario usuario : Usuario.getUsuarios()) { // Iterar sobre la lista global de usuarios
    		listaUsuarios.append(usuario.getTipoUsuario())
    		.append(" - Nombre: ").append(usuario.getNombre())
    		.append(", Email: ").append(usuario.getMail()) // Usa correctamente getMail()
    		.append(", DNI: ").append(usuario.getDni()).append("\n");
    	}
    	JOptionPane.showMessageDialog(null, listaUsuarios.toString(), "Usuarios Registrados", JOptionPane.INFORMATION_MESSAGE);
    }
    public void modificarUsuario() {
        LinkedList<Usuario> lista = Usuario.getUsuarios();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios para modificar.");
            return;
        }

        // Crear lista para mostrar nombres
        String[] opciones = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            opciones[i] = lista.get(i).getNombre() + " (" + lista.get(i).getMail() + ")";
        }

        // Elegir usuario
        int elegido = JOptionPane.showOptionDialog(null, "Seleccione un usuario para modificar:", 
                        "Modificar Usuario", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, 
                        opciones, opciones[0]);

        if (elegido >= 0) {
            Usuario usuario = lista.get(elegido);

            String[] campos = {"Nombre", "Email", "Contraseña", "Cancelar"};
            int campo = JOptionPane.showOptionDialog(null, "¿Qué desea modificar?", 
                            "Modificar Datos", JOptionPane.DEFAULT_OPTION, 
                            JOptionPane.INFORMATION_MESSAGE, null, campos, campos[0]);

            switch (campo) {
                case 0:
                    String nuevoNombre = Usuario.validarCaracteres("Nuevo nombre:");
                    usuario.setNombre(nuevoNombre);
                    break;
                case 1:
                    String nuevoEmail = Usuario.validarEmail("Nuevo email:");
                    usuario.setMail(nuevoEmail);
                    break;
                case 2:
                    String nuevaPass = JOptionPane.showInputDialog("Nueva contraseña:");
                    usuario.setPassword(nuevaPass);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Modificación cancelada.");
                    return;
            }

            JOptionPane.showMessageDialog(null, "Usuario modificado exitosamente.");
        }
    }
    public void eliminarUsuario() {
        LinkedList<Usuario> lista = Usuario.getUsuarios();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios para eliminar.");
            return;
        }

        String[] opciones = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            opciones[i] = lista.get(i).getNombre() + " (" + lista.get(i).getMail() + ")";
        }

        int elegido = JOptionPane.showOptionDialog(null, "Seleccione un usuario para eliminar:", 
                        "Eliminar Usuario", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
                        opciones, opciones[0]);

        if (elegido >= 0) {
            Usuario u = lista.get(elegido);
            if (u instanceof Administrador && u.getDni() == this.getDni()) {
                JOptionPane.showMessageDialog(null, "No podés eliminarte a vos mismo.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar a " + u.getNombre() + "?", 
                                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                lista.remove(u);
                JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.");
            }
        }
    }
    

    
}
