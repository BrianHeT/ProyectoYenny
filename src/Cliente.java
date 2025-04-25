
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
}
