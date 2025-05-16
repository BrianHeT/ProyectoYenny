package DLL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import BLL.Administrador;
import BLL.Autor;
import BLL.Cliente;
import BLL.Usuario;
import repository.Encriptador;
import repository.UsuarioRepository;

public class ControllerUsuario<T extends Usuario> implements UsuarioRepository,Encriptador{

    private static Connection con = Conexion.getInstance().getConnection();

    @Override
    public Usuario login() {
        String mail = JOptionPane.showInputDialog("Ingrese su correo:");
        String password = JOptionPane.showInputDialog("Ingrese su contraseña:");

        Usuario usuario = null;

        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT id_usuario, nombre, dni, password, tipo_usuario FROM usuario WHERE mail = ?"
            );
            stmt.setString(1, mail);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                int dni = rs.getInt("dni");
                String passwordEncriptadaBD = rs.getString("password");
                String tipoUsuario = rs.getString("tipo_usuario"); // 📌 Asegurar que la BD tiene esta columna

                // 📌 Validar la contraseña ANTES de instanciar al usuario
                if (!passwordEncriptadaBD.equals(encriptar(password))) {
                    JOptionPane.showMessageDialog(null, "Error: Contraseña incorrecta. Verifique sus credenciales.");
                    return null;
                }

                // 📌 Identificar el tipo de usuario correctamente
                switch (tipoUsuario.toLowerCase()) {
                    case "administrador":
                        usuario = new Administrador(nombre, mail, dni, passwordEncriptadaBD, "Apellido no especificado");
                        break;
                    case "cliente":
                        usuario = new Cliente(nombre, passwordEncriptadaBD, dni, mail, "Dirección no especificada");
                        break;
                    case "autor":
                        boolean independiente = rs.getBoolean("independiente");
                        String editorial = rs.getString("editorial");
                        usuario = new Autor(nombre, passwordEncriptadaBD, dni, mail, independiente, editorial);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Error: Tipo de usuario desconocido.");
                        return null;
                }

                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso. Bienvenido, " + nombre + "!");
            } else {
                JOptionPane.showMessageDialog(null, "Error: Usuario no encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
        }

        return usuario;
    }
    @Override
    public void agregarUsuario(Usuario usuario, String tipoUsuario, String datoAdicional1, String datoAdicional2) {
        String insertUsuarioSQL = "INSERT INTO usuario (nombre, mail, dni, password, tipo_usuario) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmtUsuario = con.prepareStatement(insertUsuarioSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtUsuario.setString(1, usuario.getNombre());
            stmtUsuario.setString(2, usuario.getMail());
            stmtUsuario.setInt(3, usuario.getDni());
            stmtUsuario.setString(4, usuario.getPassword());
            stmtUsuario.setString(5, tipoUsuario); // 📌 Agregar el tipo de usuario

            int filasUsuario = stmtUsuario.executeUpdate();
            if (filasUsuario > 0) {
                ResultSet generatedKeys = stmtUsuario.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idUsuario = generatedKeys.getInt(1);
                    asignarRol(idUsuario, tipoUsuario, datoAdicional1, datoAdicional2);
                    JOptionPane.showMessageDialog(null, "Usuario registrado y asignado como " + tipoUsuario + " correctamente.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void asignarRol(int idUsuario, String categoria, String datoAdicional1, String datoAdicional2) {
        String insertRolSQL = "";

        switch (categoria.toLowerCase()) {
            case "administrador":
                insertRolSQL = "INSERT INTO administrador (fk_usuario, apellido) VALUES (?, ?)";
                break;
            case "cliente":
                insertRolSQL = "INSERT INTO cliente (fk_usuario, direccion) VALUES (?, ?)";
                break;
            case "autor":
                boolean independiente = datoAdicional1.trim().equalsIgnoreCase("true");
                insertRolSQL = independiente 
                    ? "INSERT INTO autor (fk_usuario, independiente) VALUES (?, ?)"  // 📌 Solo dos parámetros si es independiente
                    : "INSERT INTO autor (fk_usuario, independiente, editorial) VALUES (?, ?, ?)";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Error: Rol no válido.");
                return;
        }

        try (PreparedStatement stmtRol = con.prepareStatement(insertRolSQL)) {
            stmtRol.setInt(1, idUsuario);
            
            if ("autor".equalsIgnoreCase(categoria)) {
                boolean independiente = datoAdicional1.trim().equalsIgnoreCase("true");
                stmtRol.setBoolean(2, independiente);

                if (!independiente) { // 📌 Solo asignar editorial si NO es independiente
                    stmtRol.setString(3, datoAdicional2);
                }
            } else { // 📌 Para Administrador y Cliente
                stmtRol.setString(2, datoAdicional1);
            }

            stmtRol.executeUpdate();
            JOptionPane.showMessageDialog(null, "Rol asignado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al asignar rol: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public LinkedList<Usuario> mostrarUsuarios() {
        LinkedList<Usuario> usuarios = new LinkedList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM usuario");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");
                String password = rs.getString("password");

                switch (tipo.toLowerCase()) {
                    case "alumno":
                        break;
                    case "profesor":
                        break;
                    default:
                        System.out.println("Tipo desconocido: " + tipo);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

	

	
}
