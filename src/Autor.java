
import javax.swing.JOptionPane;

public class Autor extends Usuario {
	private boolean independiente;
	private String editorial;


	public Autor(String nombre, String password, int dni, String mail, boolean independiente, String editorial) {
		super(nombre, password, dni, mail);
		this.independiente = independiente;
		this.editorial = editorial;
	}
	public Autor(String nombre, String password, int dni, String mail, boolean independiente) {
		super(nombre, password, dni, mail);
		this.independiente = independiente;
		this.editorial = editorial;
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
		boolean salir = false;
		while (!salir) {
			String menu = "Menú Autor:\n" + "1) Enviar Proyecto\n" + "2) Ver mis Proyectos\n" + "3) Salir";
			String opcion = JOptionPane.showInputDialog(menu);
			if (opcion == null)
				break;
			switch (opcion) {
			case "1":
				JOptionPane.showMessageDialog(null, "Enviando Proyecto... [Prototipo]");
				break;
			case "2":
				JOptionPane.showMessageDialog(null, "Mostrando Proyectos... [Prototipo]");
				break;
			case "3":
				salir = true;
				break;
			default:
				JOptionPane.showMessageDialog(null, "Opción no válida");
				break;
			}
		}
	}
}
