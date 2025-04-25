
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// Crear un Administrador predeterminado
	
		Usuario.ActualizarBios();
		int opcion;
		do {
			opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Gestión de Usuarios",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, TipoInicio.values(),
					TipoInicio.values()[0]);
			switch (opcion) {
			case 0: // Crear Cliente
				Usuario.crearUsuario();
				break;
			case 1: // Iniciar sesion
				Usuario.iniciarSesion();
				break;
			case 2: // Salir
				JOptionPane.showMessageDialog(null, "Saliendo de libreria Yenny...");
				break;

			}
		} while (opcion != 2);


	}
}