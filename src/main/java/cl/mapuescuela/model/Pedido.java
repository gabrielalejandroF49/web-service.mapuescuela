package cl.mapuescuela.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // FK hacia Cliente
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
            name = "pedido_producto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    private ModalidadEntrega modalidadEntrega;

    private String direccionEntrega;

    // Constructor vacío (necesario para JPA y pruebas)
    public Pedido() {}

    // Constructor principal
    public Pedido(int id, Cliente cliente, List<Producto> productos,
                  ModalidadEntrega modalidadEntrega, String direccionEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.productos = productos;
        this.estado = EstadoPedido.PENDIENTE; // estado inicial
        this.modalidadEntrega = modalidadEntrega;
        this.direccionEntrega = direccionEntrega;
    }

    // Getters
    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<Producto> getProductos() { return productos; }
    public EstadoPedido getEstado() { return estado; }
    public ModalidadEntrega getModalidadEntrega() { return modalidadEntrega; }
    public String getDireccionEntrega() { return direccionEntrega; }

    // Setters (solo lo que puede cambiar)
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
}
