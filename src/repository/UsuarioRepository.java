package repository;

import java.util.List;

import BLL.Usuario;

public interface UsuarioRepository {
	int agregarUsuario(Usuario usuario, String categoria, String datoAdicional1, String datoAdicional2);
	 public Usuario login(String mail, String password);
}
