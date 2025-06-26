package GUI;

import BLL.Cliente;
import BLL.Libro;
import DLL.ControllerUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuClienteFrame extends JFrame {
	private final ControllerUsuario controller;
	private final Cliente cliente;
	private final JLabel lblSaldo;
	private final JLabel lblClienteInfo;

	private final DefaultTableModel modelLibros = new DefaultTableModel(
			new String[] { "ID", "Título", "sipnosis", "Precio", "Stock" }, 0) {
		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	};
	private final JTable tablaLibros = new JTable(modelLibros);

	public MenuClienteFrame(ControllerUsuario controller, Cliente cliente) {
		this.controller = controller;
		this.cliente = cliente;
		this.cliente.setController(controller);

		setTitle("Menú Cliente");
		setSize(850, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Panel lateral con botones
		JPanel panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));
		lblClienteInfo = new JLabel("Cliente: " + cliente.getNombre() + " (" + cliente.getMail() + ")");
		lblClienteInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaldo = new JLabel("Saldo actual: $" + cliente.getSaldo());
		lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnIngresarSaldo = new JButton("Ingresar Saldo");
		btnIngresarSaldo.addActionListener(e -> {
			cliente.agregarSaldo();
			controller.actualizarSaldoCliente(cliente.getMail(), cliente.getSaldo());
			actualizarSaldo();
		});

		JButton btnVerCatalogo = new JButton("Ver Catálogo");
		btnVerCatalogo.addActionListener(e -> refrescarTablaLibros());

		JButton btnAgregar = new JButton("Agregar al Carrito");
		btnAgregar.addActionListener(e -> {
			int fila = tablaLibros.getSelectedRow();
			if (fila < 0) {
				JOptionPane.showMessageDialog(this, "Seleccione un libro para agregar.");
				return;
			}
			int idLibro = (int) modelLibros.getValueAt(fila, 0);
			int stock = (int) modelLibros.getValueAt(fila, 4);

			String input = JOptionPane.showInputDialog(this,
					"¿Cuántas unidades desea agregar? (Stock disponible: " + stock + ")", "Agregar al Carrito",
					JOptionPane.QUESTION_MESSAGE);

			if (input == null)
				return; // cancelado
			int cantidad;
			try {
				cantidad = Integer.parseInt(input);
				if (cantidad <= 0 || cantidad > stock) {
					JOptionPane.showMessageDialog(this, "Cantidad inválida o mayor al stock disponible.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			cliente.agregarLibroAlCarrito(idLibro, cantidad);
		});

		JButton btnVerCarrito = new JButton("Ver Carrito");
		btnVerCarrito.addActionListener(e -> cliente.verLibrosDelCarrito());

		JButton btnRealizarCompra = new JButton("Realizar Compra");
		btnRealizarCompra.addActionListener(e -> {
			cliente.realizarCompra();
			actualizarSaldo();
			refrescarTablaLibros();
		});

		JButton btnMisCompras = new JButton("Mis Compras");
		btnMisCompras.addActionListener(e -> cliente.verMisCompras());

		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(e -> dispose());

		// Agrega todos los botones al panel lateral
		panelBotones.add(lblClienteInfo);
		panelBotones.add(lblSaldo);
		panelBotones.add(btnIngresarSaldo);
		panelBotones.add(btnVerCatalogo);
		panelBotones.add(btnAgregar);
		panelBotones.add(btnVerCarrito);
		panelBotones.add(btnRealizarCompra);
		panelBotones.add(btnMisCompras);
		panelBotones.add(btnSalir);

		// Agrega componentes al frame
		add(panelBotones, BorderLayout.WEST);
		add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

		refrescarTablaLibros();
		setVisible(true);
	}

	private void actualizarSaldo() {
		lblSaldo.setText("Saldo actual: $" + cliente.getSaldo());
	}

	private void refrescarTablaLibros() {
		modelLibros.setRowCount(0);
		List<Libro> lista = cliente.listarLibrosParaVenta();
		for (Libro l : lista) {
			modelLibros.addRow(new Object[] { l.getId(), l.getTitulo(), l.getsipnosis(), l.getPrecio(), l.getStock() });
		}
	}
}
