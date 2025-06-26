package GUI;

import javax.swing.SwingUtilities;
import DLL.ControllerUsuario;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControllerUsuario controller = new ControllerUsuario();
            new MenuPrincipalFrame(controller);
        });
    }
}
