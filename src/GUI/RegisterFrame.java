package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DLL.ControllerUsuario;
import BLL.Administrador;
import BLL.Autor;
import BLL.Cliente;
import BLL.Usuario;

public class RegisterFrame extends JFrame {
    private JTextField txtNombre, txtEmail, txtDni;
    private JPasswordField txtPassword;
    private JButton btnRegistrar, btnCancelar;
    private ControllerUsuario controller;

    public RegisterFrame(ControllerUsuario controller) {
        this.controller = controller;

        setTitle("Registro de Usuario");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("DNI:"));
        txtDni = new JTextField();
        add(txtDni);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");

        add(btnRegistrar);
        add(btnCancelar);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText().trim();
                String email = txtEmail.getText().trim();
                String dniStr = txtDni.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                password = controller.encriptar(password);

                if (nombre.isEmpty() || email.isEmpty() || dniStr.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "⚠️ Todos los campos son obligatorios.");
                    return;
                }

                if (!email.contains("@")) {
                    JOptionPane.showMessageDialog(null, "⚠️ Email inválido. Debe contener '@'.");
                    return;
                }

                int dni;
                try {
                    dni = Integer.parseInt(dniStr);
                    if (dniStr.length() != 8) {
                        JOptionPane.showMessageDialog(null, "⚠️ El DNI debe tener exactamente 8 dígitos.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "⚠️ DNI inválido. Debe contener solo números.");
                    return;
                }

                String[] opciones = {"Administrador", "Cliente", "Autor"};
                int seleccion = JOptionPane.showOptionDialog(null, "Seleccione el tipo de usuario", "Registro",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

                if (seleccion < 0) {
                    JOptionPane.showMessageDialog(null, "Registro cancelado.");
                    return;
                }

                String tipoUsuario = opciones[seleccion];

                Usuario nuevoUsuario = null;
                String datoAdicional1 = "";
                String datoAdicional2 = "";

                switch (tipoUsuario.toLowerCase()) {
                    case "administrador":
                        datoAdicional1 = JOptionPane.showInputDialog("Ingrese su apellido:");
                        nuevoUsuario = new Administrador(0, nombre, email, dni, password, datoAdicional1);
                        break;
                    case "cliente":
                        datoAdicional1 = JOptionPane.showInputDialog("Ingrese su dirección:");
                        nuevoUsuario = new Cliente(0, nombre, password, dni, email, datoAdicional1);
                        break;
                    case "autor":
                        boolean independiente = JOptionPane.showConfirmDialog(null, "¿Es autor independiente?", "Autor Independiente",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                        datoAdicional1 = String.valueOf(independiente);
                        datoAdicional2 = independiente ? "Independiente" : JOptionPane.showInputDialog("Ingrese el nombre de la editorial:");
                        nuevoUsuario = new Autor(0, nombre, password, dni, email, independiente, datoAdicional2);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Error: Tipo de usuario inválido.");
                        return;
                }

                int idUsuario = controller.agregarUsuario(nuevoUsuario, tipoUsuario, datoAdicional1, datoAdicional2);
                if (idUsuario > 0) {
                    JOptionPane.showMessageDialog(null, "✅ Usuario registrado correctamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Error al registrar usuario.");
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }
}