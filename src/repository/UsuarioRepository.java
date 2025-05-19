package repository;

import java.util.List;

import BLL.Usuario;

public interface UsuarioRepository {
	void agregarUsuario(Usuario usuario, String categoria, String datoAdicional1, String datoAdicional2);
	 public Usuario login();
}
