package cl.mapuescuela.endpoint;

import cl.mapuescuela.model.Comprobante;
import cl.mapuescuela.model.EstadoValidacion;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.EstadoPedido;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ComprobanteEndpoint {

    private List<Comprobante> comprobantes = new ArrayList<>();
    private Long comprobanteCounter = 1L;

    // Simulación de pedidos en memoria (en un proyecto real se inyectaría PedidoService o repositorio)
    private List<Pedido> pedidos = new ArrayList<>();

    @WebMethod
    public Comprobante adjuntarComprobante(Long pedidoId, String archivo) {
        if (archivo == null || archivo.isBlank()) {
            throw new RuntimeException("El archivo de comprobante no puede estar vacío");
        }

        // Verificar que el pedido exista
        Pedido pedido = buscarPedidoPorId(pedidoId);
        if (pedido == null) {
            throw new RuntimeException("Pedido asociado no encontrado");
        }

        Comprobante comprobante = new Comprobante(comprobanteCounter++, pedidoId, archivo);
        comprobantes.add(comprobante);

        // Al adjuntar comprobante, el pedido pasa a estado "Pago en revisión"
        pedido.setEstado(EstadoPedido.PAGO_REVISION);


        return comprobante;
    }

    @WebMethod
    public Comprobante validarComprobante(Long comprobanteId, boolean aprobado) {
        for (Comprobante c : comprobantes) {
            if (c.getId().equals(comprobanteId)) {
                c.setEstadoValidacion(aprobado ? EstadoValidacion.APROBADO : EstadoValidacion.RECHAZADO);

                // Actualizar estado del pedido asociado
                Pedido pedido = buscarPedidoPorId(c.getPedidoId());
                if (pedido == null) {
                    throw new RuntimeException("Pedido asociado no encontrado");
                }

                if (aprobado) {
                    pedido.setEstado(EstadoPedido.PAGO_APROBADO);
                } else {
                    pedido.setEstado(EstadoPedido.PAGO_RECHAZADO);
                }

                return c; // devolvemos el comprobante actualizado
            }
        }
        throw new RuntimeException("Comprobante no encontrado");
    }

    // Método auxiliar para buscar pedidos
    private Pedido buscarPedidoPorId(Long id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id.intValue()) {
                return p;
            }
        }
        return null;
    }
}
