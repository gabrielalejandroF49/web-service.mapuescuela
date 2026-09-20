package cl.mapuescuela.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false) // FK hacia Cliente
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @NotEmpty(message = "El pedido debe tener al menos un detalle")
    private List<DetallePedido> detalles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidad_entrega")
    @NotNull(message = "La modalidad de entrega es obligatoria")
    private ModalidadEntrega modalidadEntrega;

    @Column(name = "direccion")
    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String direccionEntrega;

    // Constructor vacío
    public Pedido() {}

    // Constructor principal
    public Pedido(int id, Cliente cliente, List<DetallePedido> detalles,
                  ModalidadEntrega modalidadEntrega, String direccionEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.modalidadEntrega = modalidadEntrega;
        this.direccionEntrega = direccionEntrega;
    }

    // Getters y Setters
    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public EstadoPedido getEstado() { return estado; }
    public ModalidadEntrega getModalidadEntrega() { return modalidadEntrega; }
    public String getDireccionEntrega() { return direccionEntrega; }

    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}

