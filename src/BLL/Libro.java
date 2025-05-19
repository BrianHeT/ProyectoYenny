package BLL;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Libro {
	
	private int id;
    private String titulo;
    private String sipnosis;
    private double precio;
    private int stock;
    private String estado; // nuevo campo

    private static LinkedList<Libro> listaLibros = new LinkedList<>();

    
    
    public Libro(int id, String titulo, String sipnosis, double precio, int stock, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.sipnosis = sipnosis;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }
    
  
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSipnosis() {
		return sipnosis;
	}


	public void setSipnosis(String sipnosis) {
		this.sipnosis = sipnosis;
	}


	public static void setListaLibros(LinkedList<Libro> listaLibros) {
		Libro.listaLibros = listaLibros;
	}


	public Libro(String titulo, String sipnosis, double precio, int stock, String estado) {
        this.titulo = titulo;
        this.sipnosis = sipnosis;
        this.precio = precio;
        this.stock = stock; // 📌 Asegúrate de que stock se asigna correctamente
        this.estado = estado;
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

    public String getsipnosis() {
        return sipnosis;
    }

    public void setsipnosis(String sipnosis) {
        this.sipnosis = sipnosis;
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
   



}
