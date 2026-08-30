package cl.mapuescuela.model;

import jakarta.persistence.*;

@Entity
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Pedido (FK)
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    private String archivo; // referencia al comprobante subido

    @Enumerated(EnumType.STRING)
    private EstadoValidacion estadoValidacion;

    public Comprobante() {
        this.estadoValidacion = EstadoValidacion.PENDIENTE; // estado inicial
    }

    public Comprobante(Long id, Pedido pedido, String archivo) {
        this.id = id;
        this.pedido = pedido;
        this.archivo = archivo;
        this.estadoValidacion = EstadoValidacion.PENDIENTE;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public String getArchivo() { return archivo; }
    public void setArchivo(String archivo) { this.archivo = archivo; }

    public EstadoValidacion getEstadoValidacion() { return estadoValidacion; }
    public void setEstadoValidacion(EstadoValidacion estadoValidacion) { this.estadoValidacion = estadoValidacion; }
}
