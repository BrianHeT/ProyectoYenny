package BLL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import DLL.ControllerUsuario;
import repository.TipoOpcionCliente;

public class Cliente extends Usuario {
    private String direccion;
    private double saldo; 
    private final Carrito carrito;
    private ControllerUsuario controller;
    private int idCliente;

    public ControllerUsuario getController() {
        return controller;
    }

    public void setController(ControllerUsuario controller) {
        this.controller = controller;
    }


    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    public int getIdCliente() {
        return idCliente;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
        if (controller != null && getId() > 0) {
            controller.actualizarSaldoCliente(getMail(), this.saldo);
        }
    }

    public Cliente(int id,
                   String nombre,
                   String password,
                   int dni,
                   String mail,
                   String direccion) {
        super(id, nombre, password, dni, mail);
        this.direccion = direccion;
        this.saldo     = 0.0;
        this.carrito   = new Carrito(id);
    }

    public Cliente(int id,
                   String nombre,
                   String password,
                   int dni,
                   String mail,
                   String direccion,
                   double saldo) {
        super(id, nombre, password, dni, mail);
        this.direccion = direccion;
        this.saldo     = saldo;
        this.carrito   = new Carrito();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

   
    public Carrito getCarrito() {
		return carrito;
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
        LinkedList<Libro> todos;
        try {
            todos = controller.obtenerLibros();       // tu SELECT * FROM libro
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error al obtener libros: " + e.getMessage());
            return new LinkedList<>();
        }

        // Filtramos sólo aprobados
        LinkedList<Libro> aprobados = new LinkedList<>();
        for (Libro l : todos) {
            if ("APROBADO".equalsIgnoreCase(l.getEstado())) {
                aprobados.add(l);
            }
        }
        if (aprobados.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "📭 No hay libros aprobados para la venta.");
        } else {
            // mostramos la tabla con aprobados
            String[] cols = { "Título", "Sinopsis", "Precio", "Stock" };
            Object[][] data = new Object[aprobados.size()][4];
            for (int i = 0; i < aprobados.size(); i++) {
                Libro lib = aprobados.get(i);
                data[i][0] = lib.getTitulo();
                data[i][1] = lib.getSipnosis();
                data[i][2] = lib.getPrecio();
                data[i][3] = lib.getStock();
            }
            JTable table = new JTable(data, cols);
            JOptionPane.showMessageDialog(
                null, new JScrollPane(table),
                "Catálogo de Libros", JOptionPane.INFORMATION_MESSAGE
            );
        }
        return aprobados;
    }

    public void agregarLibroAlCarrito(int idLibro, int cantidad) {
        try {
            List<Libro> disponibles = controller.obtenerLibrosDisponibles(); // solo aprobados
            for (Libro lib : disponibles) {
                if (lib.getId() == idLibro) {
                    carrito.agregarItem(lib, cantidad);
                    JOptionPane.showMessageDialog(null,
                        "✅ \"" + lib.getTitulo() + "\" x" + cantidad + " agregado al carrito.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null,
                "❌ Libro no encontrado o no disponible.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error: " + e.getMessage());
        }
    }



    public void realizarCompra() {
        if (carrito.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "❌ Tu carrito está vacío.", "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        double total = carrito.calcularTotal();
        if (saldo < total) {
            JOptionPane.showMessageDialog(null,
                "❌ Saldo insuficiente.\nSaldo: $"+saldo
              +"\nTotal compra: $"+total,
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // bajamos el saldo y registramos transacción
        saldo -= total;
        controller.actualizarSaldoCliente(getMail(), saldo);

        // construyo objeto Transaccion para registrar
        Transaccion tx = new Transaccion(this, total, 
                          new ArrayList<>(carrito.getItems()));
        controller.registrarTransaccion(tx);

        carrito.vaciar();
        JOptionPane.showMessageDialog(null,
            "✅ Compra exitosa.\nNuevo saldo: $" + saldo,
            "Compra", JOptionPane.INFORMATION_MESSAGE);
    }

    public void verLibrosDelCarrito() {
        List<ItemCarrito> items = carrito.getItems();
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Tu carrito está vacío.", "Carrito",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder contenido = new StringBuilder("📚 Libros en tu carrito:\n\n");
        for (ItemCarrito item : items) {
            Libro libro = item.getLibro();
            int qty = item.getCantidad();
            double precio = libro.getPrecio();
            double subtotal = qty * precio;

            contenido.append("• ")
                     .append(libro.getTitulo())
                     .append(" — x").append(qty)
                     .append(" @ $").append(precio)
                     .append(" = $").append(subtotal)
                     .append("\n");
        }
        contenido.append("\nTotal: $").append(carrito.calcularTotal());

        JOptionPane.showMessageDialog(null,
            contenido.toString(), "🛒 Carrito", JOptionPane.INFORMATION_MESSAGE);
    }


    public void verMisCompras() {
        try {
            List<Transaccion> txs = controller.obtenerTransaccionesPorCliente(getId());
            if (txs.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Aún no has realizado compras.",
                    "Mis Compras", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("🧾 Historial de Compras:\n\n");
            for (Transaccion tx : txs) {
                sb.append("🛍️ Transacción #").append(tx.getId())
                  .append(" — Total: $").append(tx.getTotal()).append("\n");

                for (ItemCarrito it : tx.getItems()) {
                    Libro libro = it.getLibro();
                    sb.append("   • ").append(libro.getTitulo())
                      .append(" x").append(it.getCantidad())
                      .append(" @ $").append(libro.getPrecio())
                      .append(" = $").append(it.getCantidad() * libro.getPrecio())
                      .append("\n");
                }
                sb.append("\n");
            }

            JOptionPane.showMessageDialog(null,
                sb.toString(), "Mis Compras", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error al cargar historial: " + e.getMessage());
        }
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
    public List<Libro> listarLibrosParaVenta() {
        try {
            return controller.obtenerLibrosDisponibles();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

  
 // En Cliente.java
    public void agregarLibroAlCarrito(Libro libro, int cantidad) {
        carrito.agregarItem(libro, cantidad);
        JOptionPane.showMessageDialog(null,
            "✅ \"" + libro.getTitulo() + "\" x" + cantidad + " agregado al carrito.");
    }

	
}