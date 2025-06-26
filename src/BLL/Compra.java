package BLL;

import java.util.ArrayList;
import java.util.List;

public class Compra {
    private final int idCompra;
    private final int idCliente;
    private final String nombreCliente;
    private final double totalCabecera;
    private final List<DetalleCompra> detalles = new ArrayList<>();

    public Compra(int idCompra,
                  int idCliente,
                  String nombreCliente,
                  double totalCabecera) {
        this.idCompra       = idCompra;
        this.idCliente      = idCliente;
        this.nombreCliente  = nombreCliente;
        this.totalCabecera  = totalCabecera;
    }

    public int getIdCompra()       { return idCompra; }
    public int getIdCliente()      { return idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    /** Total almacenado en la tabla transaccion */
    public double getTotalCabecera() { return totalCabecera; }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }
}
