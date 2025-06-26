// BLL/DetalleCompra.java
package BLL;

public class DetalleCompra {
	private final int idLibro;
	private final String titulo;
	private final int cantidad;
	private final double precioUnitario;

	public DetalleCompra(int idLibro, String titulo, int cantidad, double precioUnitario) {
		this.idLibro = idLibro;
		this.titulo = titulo;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}

	public int getIdLibro() {
		return idLibro;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public double getSubtotal() {
		return cantidad * precioUnitario;
	}
}
