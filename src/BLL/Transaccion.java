package BLL;
	import java.util.List;

public class Transaccion {
	
	private int id;
	private Cliente cliente;
	    private double total;
	    private List<ItemCarrito> items; // Lista de libros comprados en esta transacción

	    public Transaccion(Cliente cliente, double total, List<ItemCarrito> items) {
	        this.cliente = cliente;
	        this.total = total;
	        this.items = items;
	    }
	    
	    

	    public Transaccion(int id, Cliente cliente, double total, List<ItemCarrito> items) {
			super();
			this.id = id;
			this.cliente = cliente;
			this.total = total;
			this.items = items;
		}



		public Cliente getCliente() {
	        return cliente;
	    }

	    public double getTotal() {
	        return total;
	    }

	    public int getId() {
			return id;
		}



		public void setId(int id) {
			this.id = id;
		}



		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}



		public void setTotal(double total) {
			this.total = total;
		}



		public void setItems(List<ItemCarrito> items) {
			this.items = items;
		}



		public List<ItemCarrito> getItems() {
	        return items;
	    }

	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Transacción:\n")
	          .append("Cliente: ").append(cliente.getNombre()).append("\n")
	          .append("Total: $").append(total).append("\n")
	          .append("Libros comprados:\n");
	        for (ItemCarrito item : items) {
	            sb.append("- ").append(item.toString()).append("\n");
	        }
	        return sb.toString();
	    }
	}

