package BLL;

import javax.swing.*;

import DLL.ControllerUsuario;

import java.util.ArrayList;
import java.util.List;

public class GestionPagos {
	private int id;
    private static GestionPagos instancia; // Instancia única (singleton)
    private List<Transaccion> transacciones; // Lista para registrar transacciones realizadas

    // Constructor privado para evitar múltiples instancias
    private GestionPagos() {
        this.transacciones = new ArrayList<>();
    }
    

    public GestionPagos(int id) {
		super();
		this.id = id;
		this.transacciones  = new ArrayList<>();
	}


	public static GestionPagos getInstance() {
        if (instancia == null) {
            instancia = new GestionPagos();
        }
        return instancia;
    }

    // Método para registrar el pago
	public void realizarPago(Cliente cliente) {
	    double total = cliente.getCarrito().calcularTotal(); // Obtener total de la compra
	    if (total > 0) {
	        // Aquí es donde usas cliente.getCarrito().getItems()
	        Transaccion transaccion = new Transaccion(cliente, total, cliente.getCarrito().getItems()); 

	        ControllerUsuario controller = new ControllerUsuario();
	        controller.registrarTransaccion(transaccion); // Guardar la transacción en la BD

	        cliente.getCarrito().vaciar(); // Vaciar el carrito después de la compra
	        JOptionPane.showMessageDialog(null, "Pago realizado con éxito. Total: $" + total);
	    } else {
	        JOptionPane.showMessageDialog(null, "El carrito está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

    // Método exclusivo para el administrador: mostrar todas las transacciones
    public void mostrarTransacciones() {
        if (transacciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay transacciones registradas.", "Transacciones", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder reporte = new StringBuilder("Transacciones Registradas:\n");
            for (Transaccion t : transacciones) {
                reporte.append(t.toString()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, reporte.toString(), "Transacciones Registradas", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
