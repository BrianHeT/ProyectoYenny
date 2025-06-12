package GUI;

import javax.swing.JOptionPane;

import DLL.ControllerUsuario;

public class Main {
    public static void main(String[] args) {
        ControllerUsuario controller = new ControllerUsuario();
        new MainFrame(controller); // Show login window
        
        JOptionPane.showMessageDialog(null, "Soy hector");
    }
}