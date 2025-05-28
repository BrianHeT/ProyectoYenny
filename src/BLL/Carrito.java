package BLL;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Carrito {
	private int id;
	private List<ItemCarrito> items;

	public Carrito(int id) {
		super();
		this.id = id;
		this.items = items;
	}



	public Carrito() {
		items = new ArrayList<>();
	}

	public List<ItemCarrito> getItems() {
		return items;
	}

	// Agregar un libro al carrito. Si el libro ya existe, suma la cantidad.
	public void agregarItem(Libro libro, int cantidad) {
		for (ItemCarrito item : items) {
			if (item.getLibro().getTitulo().equalsIgnoreCase(libro.getTitulo())) {
				item.setCantidad(item.getCantidad() + cantidad);
				return;
			}
		}
		items.add(new ItemCarrito(libro, cantidad));
	}

	// Calcular el total del carrito
	public double calcularTotal() {
		double total = 0;
		for (ItemCarrito item : items) {
			total += item.getCantidad() * item.getLibro().getPrecio();
		}
		return total;
	}

	// Limpiar el carrito
	public void vaciar() {
		items.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ItemCarrito item : items) {
			sb.append(item.toString()).append("\n");
		}
		sb.append("Total: $").append(calcularTotal());
		return sb.toString();
	}
	
	

	
}
