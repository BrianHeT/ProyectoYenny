package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DLL.ControllerUsuario;

public class MenuPrincipalFrame extends JFrame {
    private ControllerUsuario controller;

    public MenuPrincipalFrame(ControllerUsuario controller) {
        this.controller = controller;

        setTitle("Gestión de Usuarios");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10)); // 📌 Espaciado entre elementos

        JButton btnRegistro = new JButton("Registrar Usuario");
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnSalir = new JButton("Salir");

        add(btnRegistro);
        add(btnLogin);
        add(btnSalir);

     
        btnRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame(controller); // 📌 Abre la ventana de registro
            }
        });

        
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(controller); // 📌 Abre la ventana de login
            }
        });

        
        btnSalir.addActionListener(e -> System.exit(0));

        setLocationRelativeTo(null); 
        setVisible(true);
    }
}
