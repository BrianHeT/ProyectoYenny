package repository;

import java.util.LinkedList;
import java.util.List;

import BLL.Usuario;

public interface UsuarioRepository {
	int agregarUsuario(Usuario usuario,
            String categoria,
            String datoAdicional1,
            String datoAdicional2);

Usuario login(String mail, String password);

// ← AGREGAMOS ESTE MÉTODO al contrato
LinkedList<Usuario> obtenerUsuarios();
}
