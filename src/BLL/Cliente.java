package BLL;

import java.util.LinkedList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import DLL.ControllerUsuario;
import repository.TipoOpcionCliente;

public class Cliente extends Usuario {
    private int id;
    private String direccion;
    private double saldo; 
    private Carrito Carrito;
    private ControllerUsuario controller;

    public ControllerUsuario getController() {
        return controller;
    }

    public void setController(ControllerUsuario controller) {
        this.controller = controller;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        if (controller != null && this.id > 0 && saldo >= 0) {
        	controller.actualizarSaldoCliente(this.getMail(), this.saldo);
        }
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
        if (controller != null && id > 0 && this.saldo >= 0) {
        	controller.actualizarSaldoCliente(this.getMail(), this.saldo);
        }
    }
    public Cliente(int id, String nombre, String password, int dni, String mail, String direccion, double saldo) {
        super(id, nombre, password, dni, mail);
        this.direccion = direccion;
        this.saldo = saldo; 
        this.Carrito = new Carrito();
    }
    public Cliente(int id, String nombre, String password, int dni, String mail, String direccion) {
        super(id, nombre, password, dni, mail);
        this.direccion = direccion;
        this.saldo = 0.0; 
        this.Carrito = new Carrito();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Carrito getCarrito() {
        return Carrito;
    }

    public void setCarrito(Carrito carrito) {
        Carrito = carrito;
    }

    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }

    private void checkController() {
        if (controller == null) {
            JOptionPane.showMessageDialog(null, "Error: El controller no está asignado. Contacte al administrador.", "Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("ControllerUsuario no asignado en Cliente");
        }
    }
    public int getIdCliente(int idCliente) {
        this.id = idCliente;
    
    	return this.id;}
    
    public void setIdCliente(int idCliente) {
        this.id = idCliente;
    }

    @Override
    public void mostrarMenu() {       
        TipoOpcionCliente opcion;
        do {
            opcion  = (TipoOpcionCliente) JOptionPane.showInputDialog(
                null,
                "Seleccione una opción",
                "Gestión de Usuarios",
                JOptionPane.QUESTION_MESSAGE,
                null,
                TipoOpcionCliente.values(),
                TipoOpcionCliente.values()[0]
            );
            if (opcion == null) {
                JOptionPane.showMessageDialog(null, "Operación cancelada.");
                return;
            }
            switch (opcion) {
                case IngresarSaldo:
                    agregarSaldo();
                    break;
                case VerSaldo:
                    JOptionPane.showMessageDialog(null, "Tu saldo actual es: $" + getSaldo(), "saldo disponible", JOptionPane.INFORMATION_MESSAGE );
                    break;
                case VerCatalogo:
                    JOptionPane.showMessageDialog(null, "Mostrando Catálogo...");
                    verLibrosDisponibles();
                    LinkedList<Libro> libros = null;
                    try {
                        checkController();
                        libros = controller.obtenerLibros();
                    } catch (Exception e) {
                        return;
                    }
                    if (libros.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay libros disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String[] opcionesLibros = new String[libros.size()];
                    for (int i = 0; i < libros.size(); i++) {
                        opcionesLibros[i] = libros.get(i).getId() + " - " + libros.get(i).getTitulo();
                    }
                    JComboBox<String> comboLibros = new JComboBox<>(opcionesLibros);
                    JOptionPane.showMessageDialog(null, comboLibros, "Seleccione un libro", JOptionPane.QUESTION_MESSAGE);
                    int indexSeleccionado = comboLibros.getSelectedIndex();
                    if (indexSeleccionado == -1) {
                        JOptionPane.showMessageDialog(null, "No se seleccionó ningún libro.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Libro libroSeleccionado = libros.get(indexSeleccionado);
                    String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad:");
                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        agregarLibroAlCarrito(libroSeleccionado.getId(), cantidad);
                        JOptionPane.showMessageDialog(null, "Libro agregado: " + libroSeleccionado.getTitulo() + " x " + cantidad);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case Promociones:
                    JOptionPane.showMessageDialog(null, "No hay promociones disponibles actualmente.");
                    break;
                case RealizarCompra:
                    realizarCompra();
                    break;
                case VerCarrito:
                    verLibrosDelCarrito();
                    break;
                case EstadoEnvio:
                    JOptionPane.showMessageDialog(null, "Ver Estado de Envío... [Prototipo]");
                    break;
                case MisCompras:
                    JOptionPane.showMessageDialog(null, "Ver Mis Compras... ");
                    verMisCompras();
                    break;
                case CambiosyDevoluciones:
                    JOptionPane.showMessageDialog(null, "Cambios y Devoluciones... [Prototipo]");
                    break;
                case Salir:
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema...");
                    break;
                default:
                    break;
            }
        } while (opcion != TipoOpcionCliente.Salir);
    }

    public LinkedList<Libro> verLibrosDisponibles() {
        try {
            checkController();
        } catch (Exception e) {
            return new LinkedList<>();
        }
        LinkedList<Libro> libros = controller.obtenerLibros();
        if (libros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "📭 No hay libros registrados en la base de datos.");
        }
        String[] columnNames = { "Título", "Sipnosis", "Precio", "Stock", "Estado" };
        Object[][] data = new Object[libros.size()][5];
        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            data[i][0] = libro.getTitulo();
            data[i][1] = libro.getsipnosis();
            data[i][2] = libro.getPrecio();
            data[i][3] = libro.getStock();
            data[i][4] = libro.getEstado();
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(null, scrollPane, "Libros Disponibles", JOptionPane.INFORMATION_MESSAGE);
        return libros;
    }

    public void agregarLibroAlCarrito(int idLibro, int cantidad) {
        try {
            checkController();
        } catch (Exception e) {
            return;
        }
        LinkedList<Libro> libros = controller.obtenerLibros();
        for (Libro libro : libros) {
            if (libro.getId() == idLibro) {
                Carrito.agregarItem(libro, cantidad);
                JOptionPane.showMessageDialog(null, "Libro agregado al carrito: " + libro.getTitulo());
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Libro no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void realizarCompra() {
        if (Carrito.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío. No puedes realizar una compra.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double total = Carrito.calcularTotal();
        if (saldo < total) {
            JOptionPane.showMessageDialog(null, "No tenés saldo suficiente.\nSaldo disponible: $" + saldo + "\nTotal de la compra: $" + total, "Saldo insuficiente", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            checkController();
        } catch (Exception e) {
            return;
        }
        saldo -= total;
        controller.actualizarSaldoCliente(this.getMail(), this.saldo);

        for (ItemCarrito item : Carrito.getItems()) {
            Libro libro = item.getLibro();
            int nuevasVentas = libro.getVentas() + item.getCantidad();
            libro.setVentas(nuevasVentas);
        }
        Carrito.vaciar();
        JOptionPane.showMessageDialog(null, "Compra realizada con éxito.\nSe descontaron $" + total + " de tu saldo.\nSaldo restante: $" + saldo, "Compra exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    public void verLibrosDelCarrito() {
        if (Carrito.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío.", "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder contenidoCarrito = new StringBuilder("Libros en tu carrito:\n");
        for (ItemCarrito item : Carrito.getItems()) {
            contenidoCarrito.append("- ").append(item.getLibro().getTitulo())
                .append(" x ").append(item.getCantidad())
                .append(" ($").append(item.getLibro().getPrecio()).append(" c/u)\n");
        }
        contenidoCarrito.append("\nTotal: $").append(Carrito.calcularTotal());
        JOptionPane.showMessageDialog(null, contenidoCarrito.toString(), "Carrito de Compras", JOptionPane.INFORMATION_MESSAGE);
    }

    public void verMisCompras() {
        try {
            checkController();
        } catch (Exception e) {
            return;
        }
        LinkedList<Transaccion> transacciones = controller.obtenerTransaccionesPorCliente(this.getId());
        if (transacciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aún no has realizado compras.", "Mis Compras", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder historialCompras = new StringBuilder("Historial de Compras:\n");
        for (Transaccion transaccion : transacciones) {
            historialCompras.append("- Compra por $").append(transaccion.getTotal()).append("\n");
        }
        JOptionPane.showMessageDialog(null, historialCompras.toString(), "Mis Compras", JOptionPane.INFORMATION_MESSAGE);
    }

    public void agregarSaldo() {
        String input = JOptionPane.showInputDialog(null, "Ingrese el monto a agregar:");
        if (input != null) {
            try {
                double monto = Double.parseDouble(input);
                if (monto > 0) {
                    try {
                        checkController();
                    } catch (Exception e) {
                        return;
                    }
                    this.saldo += monto;
                    controller.actualizarSaldoCliente(this.getMail(), this.saldo); 
                    JOptionPane.showMessageDialog(null, "Saldo actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un monto válido.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido.");
            }
        }
    }

    public boolean descontarSaldo(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            try {
                checkController();
            } catch (Exception e) {
                return false;
            }
              controller.actualizarSaldoCliente(this.getMail(), this.saldo);

            return true;
        }
        return false;
    }

    public void mostrarCatalogoYAgregarAlCarrito() {
        LinkedList<Libro> libros = verLibrosDisponibles();
        if (libros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay libros disponibles.");
            return;
        }
        String[] opcionesLibros = new String[libros.size()];
        for (int i = 0; i < libros.size(); i++) {
            opcionesLibros[i] = libros.get(i).getId() + " - " + libros.get(i).getTitulo();
        }
        JComboBox<String> comboLibros = new JComboBox<>(opcionesLibros);
        JOptionPane.showMessageDialog(null, comboLibros, "Seleccione un libro", JOptionPane.QUESTION_MESSAGE);
        int indexSeleccionado = comboLibros.getSelectedIndex();
        if (indexSeleccionado == -1) {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún libro.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Libro libroSeleccionado = libros.get(indexSeleccionado);
        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad:");
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            agregarLibroAlCarrito(libroSeleccionado.getId(), cantidad);
            JOptionPane.showMessageDialog(null, "Libro agregado: " + libroSeleccionado.getTitulo() + " x " + cantidad);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

	
}