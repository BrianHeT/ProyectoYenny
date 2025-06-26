package GUI;
import BLL.AutorIndependiente; import BLL.Libro; import DLL.ControllerUsuario;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.util.List;

public class MenuAutorIndependienteFrame extends JFrame {
    private final ControllerUsuario controller;
    private final AutorIndependiente autor;
    private final JTable tablaProyectos;
    private final DefaultTableModel modelo;

    public MenuAutorIndependienteFrame(ControllerUsuario controller,
                                       AutorIndependiente autor) {
        this.controller = controller;
        this.autor      = autor;
        this.autor.setController(controller);

        setTitle("Menú Autor Independiente");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Tabla
        modelo = new DefaultTableModel(
          new String[]{"ID","Título","Estado","Disponible"}, 0
        );
        tablaProyectos = new JTable(modelo);
        add(new JScrollPane(tablaProyectos), BorderLayout.CENTER);

        // Botones
        JPanel pnl = new JPanel(new GridLayout(0,1,5,5));
        JButton btnProp = new JButton("Proponer Proyecto");
        JButton btnVer  = new JButton("Ver Estado Proyectos");

        btnProp.addActionListener(e -> {
          Libro nuevo = dialogoCrearLibro();
          if (nuevo == null) return;
          boolean ok = autor.proponerProyecto(nuevo);
          JOptionPane.showMessageDialog(this,
            ok ? "✅ Proyecto enviado" : "❌ Error al enviar",
            "Proyectos",
            ok ? JOptionPane.INFORMATION_MESSAGE
               : JOptionPane.ERROR_MESSAGE);
          refrescarTablaProyectos();
        });
        btnVer.addActionListener(e -> refrescarTablaProyectos());

        pnl.add(btnProp);
        pnl.add(btnVer);
        add(pnl, BorderLayout.WEST);

        refrescarTablaProyectos();
        setVisible(true);
    }

    private Libro dialogoCrearLibro() {
        String t = JOptionPane.showInputDialog(this, "Título:"); if (t==null) return null;
        String s = JOptionPane.showInputDialog(this, "Sinopsis:"); if (s==null) return null;
        int p; try { p=Integer.parseInt(JOptionPane.showInputDialog(this,"Precio:")); }
        catch(Exception ex){ JOptionPane.showMessageDialog(this,"Precio inválido"); return null;}
        int st; try { st=Integer.parseInt(JOptionPane.showInputDialog(this,"Stock:")); }
        catch(Exception ex){ JOptionPane.showMessageDialog(this,"Stock inválido"); return null;}
        return new Libro(t, s, p, st, "PENDIENTE");
    }

    private void refrescarTablaProyectos() {
        modelo.setRowCount(0);
        List<Libro> lista = autor.verEstadoProyectos();
        for (Libro l : lista) {
            modelo.addRow(new Object[]{
              l.getId(), l.getTitulo(), l.getEstado(),
              "APROBADO".equals(l.getEstado())
            });
        }
    }
}
