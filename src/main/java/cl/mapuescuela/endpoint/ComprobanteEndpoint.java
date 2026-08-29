package cl.mapuescuela.endpoint;

import cl.mapuescuela.model.Comprobante;
import cl.mapuescuela.model.EstadoValidacion;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.EstadoPedido;
import cl.mapuescuela.service.PedidoService;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ComprobanteEndpoint {

    // Lista de comprobantes en memoria (temporal, después se conecta a BD)
    private final List<Comprobante> comprobantes = new ArrayList<>();
    private Long comprobanteCounter = 1L;

    // Servicio de pedidos (para buscar y actualizar estados)
    private final PedidoService pedidoService = new PedidoService();

    // Adjuntar comprobante a un pedido
    @WebMethod
    public Comprobante adjuntarComprobante(Long pedidoId, String archivo) {
        if (archivo == null || archivo.isBlank()) {
            throw new RuntimeException("El archivo de comprobante no puede estar vacío");
        }

        Pedido pedido = pedidoService.buscarPedidoEnMemoriaPorId(pedidoId.intValue());
        if (pedido == null) {
            throw new RuntimeException("Pedido asociado no encontrado");
        }

        Comprobante comprobante = new Comprobante(comprobanteCounter++, pedidoId, archivo);
        comprobantes.add(comprobante);

        // Al adjuntar comprobante, el pedido pasa a estado "Pago en revisión"
        pedido.setEstado(EstadoPedido.PAGO_REVISION);

        return comprobante;
    }

    // Validar comprobante (aprobado o rechazado)
    @WebMethod
    public Comprobante validarComprobante(Long comprobanteId, boolean aprobado) {
        for (Comprobante c : comprobantes) {
            if (c.getId().equals(comprobanteId)) {
                c.setEstadoValidacion(aprobado ? EstadoValidacion.APROBADO : EstadoValidacion.RECHAZADO);

                Pedido pedido = pedidoService.buscarPedidoEnMemoriaPorId(c.getPedidoId().intValue());
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
}
