package GUI;

import javax.swing.*;
import java.awt.*;
import DLL.ControllerUsuario;

public class MainFrame extends JFrame {
    public MainFrame(ControllerUsuario controller) {
        setTitle("Menú Principal");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegister = new JButton("Registrar");
        JButton btnExit = new JButton("Salir");

        add(btnLogin);
        add(btnRegister);
        add(btnExit);

        btnLogin.addActionListener(e -> {
            new LoginFrame(controller);
            dispose();
        });

        btnRegister.addActionListener(e -> {
            new RegisterFrame(controller); // Implement RegisterFrame
            dispose();
        });

        btnExit.addActionListener(e -> System.exit(0));

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
