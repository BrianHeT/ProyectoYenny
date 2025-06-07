package GUI;

import BLL.Autor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuAutorFrame extends JFrame {
    private Autor autor;

    public MenuAutorFrame(Autor autor) {
        this.autor = autor;
        setTitle("Menú Autor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnEnviarProyecto = new JButton("Enviar Proyecto");
        btnEnviarProyecto.addActionListener((ActionEvent e) ->
            JOptionPane.showMessageDialog(this, "Enviando Proyecto... [Prototipo]")
            
        );

        JButton btnVerProyectos = new JButton("Ver Proyectos");
        btnVerProyectos.addActionListener((ActionEvent e) -> autor.verLibros());

        JButton btnVerEstado = new JButton("Ver Estado de Proyecto");
        btnVerEstado.addActionListener((ActionEvent e) ->
            JOptionPane.showMessageDialog(this, "Ver estado de proyecto... [Prototipo]")
        );

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener((ActionEvent e) -> dispose());

        panel.add(btnEnviarProyecto);
        panel.add(btnVerProyectos);
        panel.add(btnVerEstado);
        panel.add(btnSalir);

        add(panel);
        setVisible(true);
    }
}