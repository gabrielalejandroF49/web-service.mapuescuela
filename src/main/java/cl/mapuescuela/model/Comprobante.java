package cl.mapuescuela.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Pedido (FK)
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @NotNull(message = "El pedido asociado es obligatorio")
    private Pedido pedido;

    @NotBlank(message = "El archivo del comprobante es obligatorio")
    private String archivo; // referencia al comprobante subido

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado de validación es obligatorio")
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

