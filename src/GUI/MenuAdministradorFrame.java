package GUI;

import BLL.Administrador;
import BLL.Libro;
import DLL.ControllerUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MenuAdministradorFrame extends JFrame {
    private final Administrador admin;
    private final ControllerUsuario controller;
    private final JTable miTabla;
    private final DefaultTableModel model;
    private final JButton btnAprobar, btnRechazar;

    public MenuAdministradorFrame(ControllerUsuario controller,
                                  Administrador admin) {
        this.controller = controller;
        this.admin      = admin;
        this.admin.setController(controller);

        setTitle("Menú Administrador");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // 1) Tabla central
        model = new DefaultTableModel(new String[]{
            "ID","Título","Sinopsis","Precio","Stock","Estado"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        miTabla = new JTable(model);
        add(new JScrollPane(miTabla), BorderLayout.CENTER);

        // 2) Panel de botones al Oeste
        JPanel pnlMenu = new JPanel(new GridLayout(0,1,5,5));

        pnlMenu.add(new JButton(new AbstractAction("Ver Usuarios") {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.mostrarUsuarios();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Modificar Usuario") {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.mostrarUsuariosYModificar();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Eliminar Usuario") {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.eliminarUsuario();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Ver Proyectos Pendientes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                refrescarProyectosPendientes();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Ver Libros") {
            @Override
            public void actionPerformed(ActionEvent e) {
                refrescarTodosLibros();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Agregar Libro") {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean ok = admin.crearLibro();
                if (ok) JOptionPane.showMessageDialog(
                    MenuAdministradorFrame.this,
                    "📗 Libro creado con éxito",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Modificar Libro") {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.modificarLibro();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Eliminar Libro") {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.eliminarLibro();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Ver Compras") {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.verComprasConDetalle();
            }
        }));
        pnlMenu.add(new JButton(new AbstractAction("Salir") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));

        add(pnlMenu, BorderLayout.WEST);

        // 3) Panel Sur con aprobar/rechazar
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        btnAprobar  = new JButton("Aprobar Proyecto");
        btnRechazar = new JButton("Rechazar Proyecto");
        btnAprobar .addActionListener(e -> procesarProyecto(true));
        btnRechazar.addActionListener(e -> procesarProyecto(false));
        pnlAcciones.add(btnAprobar);
        pnlAcciones.add(btnRechazar);
        add(pnlAcciones, BorderLayout.SOUTH);

        // Carga inicial: proyectos pendientes
        refrescarProyectosPendientes();
        setVisible(true);
    }

    // Carga los proyectos con estado = PENDIENTE
    private void refrescarProyectosPendientes() {
        model.setRowCount(0);
        List<Libro> lista = admin.listarProyectosPendientes();
        for (Libro l : lista) {
            model.addRow(new Object[]{
                l.getId(), l.getTitulo(),
                l.getsipnosis(), l.getPrecio(),
                l.getStock(),     l.getEstado()
            });
        }
    }

    // Carga los libros aprobados (o todos, según tu método)
    private void refrescarLibros() {
        model.setRowCount(0);
        List<Libro> lista = null;
        try {
            lista = controller.obtenerLibrosEnVenta();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al cargar libros: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Libro l : lista) {
            model.addRow(new Object[]{
                l.getId(), l.getTitulo(),
                l.getsipnosis(), l.getPrecio(),
                l.getStock(),     l.getEstado()
            });
        }
    }

    /**
     * @param aprobar true = APROBADO, false = RECHAZADO
     */
    private void procesarProyecto(boolean aprobar) {
        int fila = miTabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un proyecto en la tabla.",
                "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idLibro = (int) model.getValueAt(fila, 0);
        boolean ok = admin.decidirProyecto(idLibro, aprobar);
        if (ok) {
            JOptionPane.showMessageDialog(
                this,
                aprobar
                  ? "✅ Proyecto aprobado."
                  : "❌ Proyecto rechazado.",
                "Resultado",
                aprobar
                  ? JOptionPane.INFORMATION_MESSAGE
                  : JOptionPane.WARNING_MESSAGE
            );
            refrescarProyectosPendientes();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar estado.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /** Recarga la tabla con TODOS los libros (sin filtrar) */
    private void refrescarTodosLibros() {
        model.setRowCount(0);
        // obtiene la lista completa
        List<Libro> lista = admin.listarTodosLibros();
        for (Libro l : lista) {
            model.addRow(new Object[]{
                l.getId(),
                l.getTitulo(),
                l.getsipnosis(),  
                l.getPrecio(),
                l.getStock(),
                l.getEstado()
            });
        }
    }

}
