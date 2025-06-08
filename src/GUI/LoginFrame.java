package GUI;

import BLL.Administrador;
import BLL.Cliente;
import BLL.Autor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DLL.ControllerUsuario;
import BLL.Usuario;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancelar;
    private ControllerUsuario controller;
    

    public LoginFrame(ControllerUsuario controller) {
        this.controller = controller;

        setTitle("Iniciar Sesión");
        setSize(350, 200); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10)); 
        
        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        btnLogin = new JButton("Ingresar");
        btnCancelar = new JButton("Cancelar");

        add(btnLogin);
        add(btnCancelar);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "⚠️ Por favor, complete todos los campos.");
                    return;
                }

                Usuario usuarioLogueado = controller.login(email, password);
                if (usuarioLogueado != null) {
                    JOptionPane.showMessageDialog(null, "Bienvenido, " + usuarioLogueado.getNombre() + "!");
                    if (usuarioLogueado instanceof Administrador) {
                        new MenuAdministradorFrame((Administrador) usuarioLogueado);
                    } else if (usuarioLogueado instanceof Cliente) {
                        new MenuClienteFrame((Cliente) usuarioLogueado, controller);
                    } else if (usuarioLogueado instanceof Autor) {
                        new MenuAutorFrame((Autor) usuarioLogueado);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
                }
            }
        });



       
        btnCancelar.addActionListener(e -> dispose());

        setLocationRelativeTo(null); 
        setVisible(true);
    }
}
