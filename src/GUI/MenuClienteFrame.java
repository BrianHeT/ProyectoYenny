package GUI;

import DLL.ControllerUsuario;
import BLL.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuClienteFrame extends JFrame {
    private ControllerUsuario controller;
    private Cliente cliente;
    private JLabel lblSaldo;
    private JLabel lblClienteInfo;

    public MenuClienteFrame(ControllerUsuario controller, Cliente cliente) {
        this.controller = controller;
        this.cliente    = cliente;
        // Inyectamos el controller en la instancia Cliente
        this.cliente.setController(controller);

        setTitle("Menú Cliente");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        lblClienteInfo = new JLabel(
            "Cliente: " + cliente.getNombre() 
          + " ("      + cliente.getMail() + ")"
        );
        lblClienteInfo.setHorizontalAlignment(SwingConstants.CENTER);

        lblSaldo = new JLabel("Saldo actual: $" + cliente.getSaldo());
        lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnIngresarSaldo = new JButton("Ingresar Saldo");
        btnIngresarSaldo.addActionListener((ActionEvent e) -> {
            cliente.agregarSaldo();
            controller.actualizarSaldoCliente(
                cliente.getMail(), 
                cliente.getSaldo()
            );
            actualizarSaldo();
        });

        JButton btnVerCatalogo = new JButton("Ver Catálogo");
        btnVerCatalogo.addActionListener((ActionEvent e) ->
            cliente.mostrarCatalogoYAgregarAlCarrito()
        );

        JButton btnVerCarrito = new JButton("Ver Carrito");
        btnVerCarrito.addActionListener((ActionEvent e) ->
            cliente.verLibrosDelCarrito()
        );

        JButton btnRealizarCompra = new JButton("Realizar Compra");
        btnRealizarCompra.addActionListener((ActionEvent e) -> {
            cliente.realizarCompra();
            actualizarSaldo();
        });

        JButton btnMisCompras = new JButton("Mis Compras");
        btnMisCompras.addActionListener((ActionEvent e) ->
            cliente.verMisCompras()
        );

        JButton btnPromociones = new JButton("Promociones");
        btnPromociones.addActionListener((ActionEvent e) ->
            JOptionPane.showMessageDialog(
                this, 
                "No hay promociones disponibles actualmente."
            )
        );

        JButton btnEstadoEnvio = new JButton("Estado de Envío");
        btnEstadoEnvio.addActionListener((ActionEvent e) ->
            JOptionPane.showMessageDialog(
                this, 
                "Ver Estado de Envío... [Prototipo]"
            )
        );

        JButton btnCambiosDevoluciones = new JButton("Cambios y Devoluciones");
        btnCambiosDevoluciones.addActionListener((ActionEvent e) ->
            JOptionPane.showMessageDialog(
                this, 
                "Cambios y Devoluciones... [Prototipo]"
            )
        );

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> dispose());

        panel.add(lblClienteInfo);
        panel.add(lblSaldo);
        panel.add(btnIngresarSaldo);
        panel.add(btnVerCatalogo);
        panel.add(btnVerCarrito);
        panel.add(btnRealizarCompra);
        panel.add(btnMisCompras);
        panel.add(btnPromociones);
        panel.add(btnEstadoEnvio);
        panel.add(btnCambiosDevoluciones);
        panel.add(btnSalir);

        add(panel);
        setVisible(true);
    }

    private void actualizarSaldo() {
        lblSaldo.setText("Saldo actual: $" + cliente.getSaldo());
    }
}
