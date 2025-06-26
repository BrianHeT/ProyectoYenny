package GUI;

import BLL.Usuario;
import BLL.Administrador;
import BLL.Cliente;
import BLL.Autor;
import BLL.AutorIndependiente;
import DLL.ControllerUsuario;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de login que recibe un ControllerUsuario existente en lugar de crear
 * uno nuevo.
 */
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

		btnLogin.addActionListener(e -> {
		    String email    = txtEmail.getText().trim();
		    String password = new String(txtPassword.getPassword()).trim();

		    if (email.isEmpty() || password.isEmpty()) {
		        JOptionPane.showMessageDialog(
		            this,
		            "⚠️ Por favor, complete todos los campos.",
		            "Login",
		            JOptionPane.WARNING_MESSAGE
		        );
		        return;
		    }

		    Usuario usuarioLogueado = controller.login(email, password);
		    if (usuarioLogueado == null) {
		        JOptionPane.showMessageDialog(
		            this,
		            "Credenciales incorrectas.",
		            "Login",
		            JOptionPane.ERROR_MESSAGE
		        );
		        return;
		    }

		    dispose(); // Cierra el LoginFrame

		    // 1) Administrador
		    if (usuarioLogueado instanceof Administrador) {
		        MenuAdministradorFrame adminFrame = new MenuAdministradorFrame(
		            controller,
		            (Administrador) usuarioLogueado
		        );
		        adminFrame.setVisible(true);

		    // 2) Autor Independiente (debe ir antes del chequeo de Autor)
		    } else if (usuarioLogueado instanceof AutorIndependiente) {
		        AutorIndependiente autorIndependiente =
		            (AutorIndependiente) usuarioLogueado;
		        autorIndependiente.setController(controller);
		        MenuAutorIndependienteFrame m = new MenuAutorIndependienteFrame(
		            controller,
		            autorIndependiente
		        );
		        m.setVisible(true);

		    // 3) Autor “normal”
		    } else if (usuarioLogueado instanceof Autor) {
		        MenuAutorFrame auFrame = new MenuAutorFrame(
		            controller,
		            (Autor) usuarioLogueado
		        );
		        auFrame.setVisible(true);

		    // 4) Cliente
		    } else if (usuarioLogueado instanceof Cliente) {
		        MenuClienteFrame cliFrame = new MenuClienteFrame(
		            controller,
		            (Cliente) usuarioLogueado
		        );
		        cliFrame.setVisible(true);

		    // Por si aparece un rol inesperado
		    } else {
		        JOptionPane.showMessageDialog(
		            this,
		            "Usuario con rol desconocido: " + usuarioLogueado.getClass().getSimpleName(),
		            "Login",
		            JOptionPane.ERROR_MESSAGE
		        );
		    }
		});


		btnCancelar.addActionListener(e -> dispose());

		setLocationRelativeTo(null);
		setVisible(true);
	}
}
