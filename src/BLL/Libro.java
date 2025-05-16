package BLL;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Libro {
	
	private int id;
    private String titulo;
    private String sinopsis;
    private double precio;
    private int stock;
    private String estado; // nuevo campo

    private static LinkedList<Libro> listaLibros = new LinkedList<>();

    
    
    
    
    public Libro(int id, String titulo, String sinopsis, double precio, String estado) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.sinopsis = sinopsis;
		this.precio = precio;
		this.estado = "Pendiente";
        listaLibros.add(this);

	}

	public Libro(String titulo, String sinopsis, double precio) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.estado = "Pendiente"; // Inicialmente todos los libros son "Pendientes"
        listaLibros.add(this);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public static LinkedList<Libro> getListaLibros() {
        return listaLibros;
    }
    public static void mostrarLibros() {
        LinkedList<Libro> listaLibros = Libro.getListaLibros(); // Obtén la lista de libros
        if (listaLibros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay libros registrados.");
            return;
        }

        String lista = "Lista de Libros Registrados:\n";
        for (Libro libro : listaLibros) { 
            lista += "Título: '" + libro.getTitulo() +
                     "'\nSinopsis: " + libro.getSinopsis() +
                     "\nPrecio: " + libro.getPrecio() +
                     "--------------\n";
        }
        JOptionPane.showMessageDialog(null, lista, "Libros Registrados", JOptionPane.INFORMATION_MESSAGE);
    }


    // Método para agregar libros - este método podría ir en otra clase, como Administrador
    public static void agregarLibro() {
        String titulo = Usuario.validarCaracteres("Ingrese el título del libro:");
        String sinopsis = JOptionPane.showInputDialog("Ingrese la sinopsis:");

        double precio = 0;
        boolean valido = false;
        do {
            String precioString = JOptionPane.showInputDialog("Ingrese el precio:");
            try {
                precio = Double.parseDouble(precioString);
                valido = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un precio válido.");
            }
        } while (!valido);

        // Creando el nuevo libro
        Libro nuevoLibro = new Libro(titulo, sinopsis, precio);
        JOptionPane.showMessageDialog(null, "Libro agregado correctamente.");
    }

    // Método para modificar un libro
    public static void modificarLibro() {
        LinkedList<Libro> libros = Libro.getListaLibros();
        if (libros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay libros disponibles para modificar.");
            return;
        }

        String lista = "Seleccione el índice del libro a modificar:\n";
        for (int i = 0; i < libros.size(); i++) {
            lista += i + ") " + libros.get(i).getTitulo() + "\n";
        }

        String opcionStr = JOptionPane.showInputDialog(lista);
        int opcion;
        try {
            opcion = Integer.parseInt(opcionStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Índice inválido.");
            return;
        }

        if (opcion < 0 || opcion >= libros.size()) {
            JOptionPane.showMessageDialog(null, "Índice fuera de rango.");
            return;
        }

        Libro libroSeleccionado = libros.get(opcion);

        // Modificar título, sinopsis y precio
        String nuevoTitulo = JOptionPane.showInputDialog("Nuevo título:", libroSeleccionado.getTitulo());
        String nuevaSinopsis = JOptionPane.showInputDialog("Nueva sinopsis:", libroSeleccionado.getSinopsis());
        String nuevoPrecioStr = JOptionPane.showInputDialog("Nuevo precio:", libroSeleccionado.getPrecio());

        try {
            double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
            libroSeleccionado.setTitulo(nuevoTitulo);
            libroSeleccionado.setSinopsis(nuevaSinopsis);
            libroSeleccionado.setPrecio(nuevoPrecio);

            JOptionPane.showMessageDialog(null, "Libro modificado con éxito.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Precio inválido.");
        }
    }

    
    public static void eliminarLibro() {
        LinkedList<Libro> libros = Libro.getListaLibros();
        if (libros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay libros disponibles para eliminar.");
            return;
        }

        String lista = "Seleccione el índice del libro a eliminar:\n";
        for (int i = 0; i < libros.size(); i++) {
            lista += i + ") " + libros.get(i).getTitulo() + "\n";
        }

        String opcionStr = JOptionPane.showInputDialog(lista);
        int opcion;
        try {
            opcion = Integer.parseInt(opcionStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Índice inválido.");
            return;
        }

        if (opcion < 0 || opcion >= libros.size()) {
            JOptionPane.showMessageDialog(null, "Índice fuera de rango.");
            return;
        }

        libros.remove(opcion);
        JOptionPane.showMessageDialog(null, "Libro eliminado con éxito.");
    }
}
