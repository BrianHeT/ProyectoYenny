package BLL;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Carrito {
    private int id;
    private List<ItemCarrito> items;

    /** Constructor con id de cliente */
    public Carrito(int id) {
        this.id    = id;
        this.items = new ArrayList<>();
    }

    public Carrito() {
        this.items = new ArrayList<>();
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    /** Agrega una cantidad del libro (si ya existe, suma) */
    public void agregarItem(Libro libro, int cantidad) {
        for (ItemCarrito item : items) {
            if (item.getLibro().getId() == libro.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(libro, cantidad));
    }

    public double calcularTotal() {
        return items.stream()
            .mapToDouble(i -> i.getCantidad() * i.getLibro().getPrecio())
            .sum();
    }

    public void vaciar() {
        items.clear();
    }

    @Override
    public String toString() {
        if (items.isEmpty()) return "Tu carrito está vacío.";
        StringBuilder sb = new StringBuilder();
        for (ItemCarrito item : items) {
            sb.append(item).append("\n");
        }
        sb.append("Total: $").append(calcularTotal());
        return sb.toString();
    }
}
