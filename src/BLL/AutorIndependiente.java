package BLL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import DLL.ControllerUsuario;

public class AutorIndependiente extends Autor {
    private LinkedList<Libro> librosEnviados;
    // ✅ Corrección: Agregar `id` como primer parámetro y pasarlo a `super`
    public AutorIndependiente(int id, String nombre, String password, int dni, String mail, boolean independiente, String editorial) {
        super(id, nombre, password, dni, mail, independiente, editorial); // 📌 Ahora `id` se pasa correctamente
        this.librosEnviados = new LinkedList<>();
    }


    // ✅ Métodos GET y SET
    public LinkedList<Libro> getLibrosEnviados() {
        return librosEnviados;
    }

    public void setLibrosEnviados(LinkedList<Libro> librosEnviados) {
        this.librosEnviados = librosEnviados;
    }

    public void agregarLibro(Libro libro) {
        librosEnviados.add(libro);
    }
    public boolean proponerProyecto(Libro libro) {
        try {
            // this.getId() viene heredado de Usuario y debe estar seteado tras el login
            return controller.agregarProyectoLibro(this.getId(), libro);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Libro> verEstadoProyectos() {
        try {
            return controller.obtenerProyectosDeAutor(this.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}