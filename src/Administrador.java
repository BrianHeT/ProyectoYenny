
import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

public class Administrador extends Usuario {

    public Administrador(String nombre, String mail, int dni, String password) {
        super(nombre, password, dni, mail); // Pasa los argumentos al constructor de Usuario
    }

    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }

    @Override
    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            String menu = "Menú Administrador:\n"
                        + "1) Ver todos los usuarios\n"
                        + "2) Salir";
            String opcion = JOptionPane.showInputDialog(menu);
            if (opcion == null) break; // Salir si se cancela
            switch (opcion) {
                case "1":
                    mostrarUsuarios(); // Llama al método para mostrar todos los usuarios
                    break;
                case "2":
                    salir = true; // Salir del menú
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
}
