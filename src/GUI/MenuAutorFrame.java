package GUI;

import DLL.ControllerUsuario;
import BLL.Autor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuAutorFrame extends JFrame {
    private final Autor autor;

    public MenuAutorFrame(ControllerUsuario controller, Autor autor) {
        this.controller = controller;
        this.autor      = autor;
        // Inyectamos el controller en la instancia Autor
        this.autor.setController(controller);

        setTitle("Menú Autor");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Enviar proyecto (mantengo tu prototipo)
        JButton btnEnviarProyecto = new JButton("Enviar Proyecto");
        btnEnviarProyecto.addActionListener((ActionEvent e) ->
            JOptionPane.showMessageDialog(this, "Enviando Proyecto... [Prototipo]")
        );

        // Ver libros publicados (Mis Publicaciones)
        JButton btnVerPublicaciones = new JButton("Mis Publicaciones");
        btnVerPublicaciones.addActionListener((ActionEvent e) ->
            autor.verMisPublicaciones()
        );

        // Ver librerías vendidas (Mis Ventas)
        JButton btnVerVentas = new JButton("Mis Ventas");
        btnVerVentas.addActionListener((ActionEvent e) ->
            autor.verMisVentas()
        );

        // (Opcional) Ver estado de proyecto
        JButton btnVerEstado = new JButton("Ver Estado de Proyecto");
        btnVerEstado.addActionListener((ActionEvent e) ->
            JOptionPane.showMessageDialog(
                this,
                "Ver estado de proyecto... [Prototipo]"
            )
        );

        // Salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener((ActionEvent e) -> dispose());

        panel.add(btnEnviarProyecto);
        panel.add(btnVerPublicaciones);
        panel.add(btnVerVentas);
        panel.add(btnVerEstado);
        panel.add(btnSalir);

        add(panel);
        setVisible(true);
    }
}
