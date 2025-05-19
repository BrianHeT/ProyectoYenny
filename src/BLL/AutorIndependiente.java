package BLL;
import java.util.LinkedList;

import javax.swing.JOptionPane;

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
}