package GUI;

import BLL.Usuario;
import BLL.Administrador;
import BLL.Cliente;
import BLL.Autor;
import DLL.ControllerUsuario;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de login que recibe un ControllerUsuario existente
 * en lugar de crear uno nuevo.
 */
public class LoginFrame extends JFrame {
    private ControllerUsuario controller;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancelar;

    // ← Constructor que acepta el controller
    public LoginFrame(ControllerUsuario controller) {
        this.controller = controller;

        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        // Campos
        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        // Botones
        btnLogin    = new JButton("Login");
        btnCancelar = new JButton("Cancelar");
        add(btnLogin);
        add(btnCancelar);

        // Acción Login
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass  = new String(txtPassword.getPassword()).trim();

            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Ingrese email y contraseña.",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario u = controller.login(email, pass);
            if (u == null) return;  // ya mostró mensaje de error

            // Dependiendo del tipo, abrimos el menú correspondiente
            if (u instanceof Administrador adm) {
                adm.setController(controller);
                new MenuAdministradorFrame(controller, adm);
                dispose();
            }
            else if (u instanceof Cliente cli) {
                cli.setController(controller);
                new MenuClienteFrame(controller, cli);
                dispose();
            }
            else if (u instanceof Autor au) {
                au.setController(controller);
                new MenuAutorFrame(controller, au);
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this,
                    "Usuario de tipo no soportado: " 
                    + u.getClass().getSimpleName(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción Cancelar
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }
}
