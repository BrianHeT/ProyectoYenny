package BLL;

public class ItemCarrito {

		private int id;
	    private Libro libro;
	    private int cantidad;
	    
	    
	    
	    public ItemCarrito(int id, Libro libro, int cantidad) {
			super();
			this.id = id;
			this.libro = libro;
			this.cantidad = cantidad;
		}

		public ItemCarrito(Libro libro, int cantidad) {
	        this.libro = libro;
	        this.cantidad = cantidad;
	    }
	    
	    public Libro getLibro() {
	        return libro;
	    }
	    
	    public int getCantidad() {
	        return cantidad;
	    }
	    
	    public void setCantidad(int cantidad) {
	        this.cantidad = cantidad;
	    }
	    
	    @Override
	    public String toString() {
	        return libro.toString() + " x " + cantidad;
	    }
	}



