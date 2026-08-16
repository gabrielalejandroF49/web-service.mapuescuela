package cl.mapuescuela.model;

import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<Producto> productos;
    private EstadoPedido estado;
    private ModalidadEntrega modalidadEntrega;
    private String direccionEntrega;

    public Pedido(int id, Cliente cliente, List<Producto> productos,
                  ModalidadEntrega modalidadEntrega, String direccionEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.productos = productos;
        this.estado = EstadoPedido.PENDIENTE; // estado inicial
        this.modalidadEntrega = modalidadEntrega;
        this.direccionEntrega = direccionEntrega;
    }

    // Getters y setters
    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<Producto> getProductos() { return productos; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public ModalidadEntrega getModalidadEntrega() { return modalidadEntrega; }
    public String getDireccionEntrega() { return direccionEntrega; }
}
