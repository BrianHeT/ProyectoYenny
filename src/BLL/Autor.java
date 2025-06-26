package BLL;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DLL.ControllerUsuario;
import repository.TipoOpcionAutor;

public class Autor extends Usuario {
	private boolean independiente;
    private String editorial;
    private LinkedList<Libro> librosEnviados;


    public Autor(int id, String nombre, String password, int dni, String mail, boolean independiente, String editorial) {
        super(id, nombre, password, dni, mail); 
        this.independiente = independiente;
        this.editorial = editorial;
        this.librosEnviados = new LinkedList<>(); 
    }

    protected ControllerUsuario controller;
    public void setController(ControllerUsuario controller) {
        this.controller = controller;
    }

    // (opcional) un getter si lo necesitas
    public ControllerUsuario getController() {
        return controller;
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
	                    verMisVentas();
	                    
	                    break;
	                case 1:
	                    JOptionPane.showMessageDialog(null, "Mostrando estadísticas...");
	                    verMisPublicaciones();
	                    
	                    break;
	                case 2:
	                    break;
	                case 3:
	                    JOptionPane.showMessageDialog(null, "Cerrando sesión de autor...");
	                    break;
	            }
	        } while (opcion != 3);
	    }

	// BLL/Autor.java
	 public void verMisPublicaciones() {
	     List<Libro> lista = 
	       controller.obtenerLibrosYVentasPorAutor(getId());
	     if (lista.isEmpty()) {
	         JOptionPane.showMessageDialog(null,
	           "Aún no publicaste ningún libro.");
	         return;
	     }

	     String[] cols = { "ID", "Título", "Precio", "Stock", "Estado" };
	     DefaultTableModel mdl = new DefaultTableModel(cols, 0);
	     for (Libro lib : lista) {
	         mdl.addRow(new Object[]{
	           lib.getId(),
	           lib.getTitulo(),
	           lib.getPrecio(),
	           lib.getStock(),
	           lib.getEstado()
	         });
	     }
	     JTable tbl = new JTable(mdl);
	     tbl.setFillsViewportHeight(true);

	     JOptionPane.showMessageDialog(
	       null,
	       new JScrollPane(tbl),
	       "Mis Publicaciones",
	       JOptionPane.PLAIN_MESSAGE
	     );
	 }

	 public void verMisVentas() {
	     List<Libro> lista = 
	       controller.obtenerLibrosYVentasPorAutor(getId());
	     if (lista.isEmpty()) {
	         JOptionPane.showMessageDialog(null,
	           "Todavía no se vendió ningún libro tuyo.");
	         return;
	     }

	     String[] cols = { "ID", "Título", "Vendidas", "Recaudado" };
	     DefaultTableModel mdl = new DefaultTableModel(cols, 0);
	     for (Libro lib : lista) {
	         int vend = lib.getVentas();
	         double rec = vend * lib.getPrecio();
	         mdl.addRow(new Object[]{
	           lib.getId(),
	           lib.getTitulo(),
	           vend,
	           rec
	         });
	     }
	     JTable tbl = new JTable(mdl);
	     tbl.setFillsViewportHeight(true);

	     JOptionPane.showMessageDialog(
	       null,
	       new JScrollPane(tbl),
	       "Mis Ventas",
	       JOptionPane.PLAIN_MESSAGE
	     );
	 }

}
