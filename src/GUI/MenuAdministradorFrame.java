package GUI;

import javax.swing.*;

import BLL.Administrador;
import BLL.GestionPagos;

import java.awt.*;
import java.awt.event.ActionEvent;
import DLL.ControllerUsuario;  // ajusta tu paquete real

public class MenuAdministradorFrame extends JFrame {
    private Administrador admin;
    private ControllerUsuario controller;

    // Ahora recibimos ambos: controller y admin
    public MenuAdministradorFrame(ControllerUsuario controller, Administrador admin) {
        this.controller = controller;
        this.admin      = admin;
        // Inyectamos el controller en el objeto admin
        this.admin.setController(controller);

        setTitle("Menú Administrador");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnVerUsuarios = new JButton("Ver Usuarios");
        btnVerUsuarios.addActionListener((ActionEvent e) ->
            admin.mostrarUsuarios()
        );

        JButton btnModificarUsuario = new JButton("Modificar Usuario");
        btnModificarUsuario.addActionListener((ActionEvent e) ->
            admin.mostrarUsuariosYModificar()
        );

        JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
        btnEliminarUsuario.addActionListener((ActionEvent e) ->
            admin.eliminarUsuario()
        );

        JButton btnVerLibros = new JButton("Ver libros");
        btnVerLibros.addActionListener((ActionEvent e) ->
            admin.verLibrosDisponibles()
        );

        JButton btnAgregarLibro = new JButton("Agregar Libro");
        btnAgregarLibro.addActionListener((ActionEvent e) ->
            admin.crearLibro()
        );

        JButton btnModificarLibro = new JButton("Modificar Libro");
        btnModificarLibro.addActionListener((ActionEvent e) ->
            admin.modificarLibro()
        );

        JButton btnEliminarLibro = new JButton("Eliminar Libro");
        btnEliminarLibro.addActionListener((ActionEvent e) ->
            admin.eliminarLibro()
        );

        JButton btnVerCompras = new JButton("Ver Compras");
        btnVerCompras.addActionListener(e ->
            admin.verComprasConDetalle()
        );

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener((ActionEvent e) -> dispose());

        panel.add(btnVerUsuarios);
        panel.add(btnModificarUsuario);
        panel.add(btnEliminarUsuario);
        panel.add(btnVerLibros);
        panel.add(btnAgregarLibro);
        panel.add(btnModificarLibro);
        panel.add(btnEliminarLibro);
        panel.add(btnVerCompras);
        panel.add(btnSalir);

        add(panel);
        setVisible(true);
    }
}
