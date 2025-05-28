package GUI;

import javax.swing.JOptionPane;
import DLL.ControllerUsuario;
import BLL.Administrador;
import BLL.Autor;
import BLL.Cliente;
import BLL.Usuario;

public class Main {
    public static void main(String[] args) {
        ControllerUsuario controller = new ControllerUsuario();

        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Gestión de Usuarios",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"Registrar Usuario", "Iniciar Sesión", "Salir"}, "Registrar Usuario");

            switch (opcion) {
                case 0: // Registrar usuario
                    Usuario.registrarUsuario(controller);
                    break;
                case 1: // Iniciar sesión
                    Usuario usuarioLogueado = controller.login();
                    if (usuarioLogueado != null) {
                        JOptionPane.showMessageDialog(null, "Bienvenido, " + usuarioLogueado.getNombre() + "!");

                        // 📌 Llevar al usuario al menú correcto según su tipo
                        if (usuarioLogueado instanceof Administrador) {
                            ((Administrador) usuarioLogueado).mostrarMenu();
                        } else if (usuarioLogueado instanceof Cliente) {
                            ((Cliente) usuarioLogueado).mostrarMenu();
                        } else if (usuarioLogueado instanceof Autor) {
                            ((Autor) usuarioLogueado).mostrarMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Tipo de usuario desconocido.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al iniciar sesión. Verifica tus credenciales.");
                    }
                    break;
                case 2: // Salir
                    JOptionPane.showMessageDialog(null, "Saliendo de librería Yenny...");
                    break;
            }
        } while (opcion != 2);
    }
}