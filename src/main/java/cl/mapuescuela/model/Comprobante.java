package cl.mapuescuela.model;

public class Comprobante {
    private Long id;
    private Long pedidoId;
    private String archivo; // referencia al comprobante subido
    private EstadoValidacion estadoValidacion;

    public Comprobante(Long id, Long pedidoId, String archivo) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.archivo = archivo;
        this.estadoValidacion = EstadoValidacion.PENDIENTE; // estado inicial
    }

    // Getters y setters
    public Long getId() { return id; }
    public Long getPedidoId() { return pedidoId; }
    public String getArchivo() { return archivo; }
    public EstadoValidacion getEstadoValidacion() { return estadoValidacion; }
    public void setEstadoValidacion(EstadoValidacion estadoValidacion) { this.estadoValidacion = estadoValidacion; }
}
