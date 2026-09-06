package cl.mapuescuela.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import cl.mapuescuela.service.PedidoService;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.EstadoPedido;

@Component("notificarRechazoDelegate")
public class NotificarRechazoDelegate implements JavaDelegate {

    private final PedidoService pedidoService;

    // Inyección de PedidoService
    public NotificarRechazoDelegate(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener el ID del pedido desde las variables del proceso
        Integer pedidoId = (Integer) execution.getVariable("pedidoId");
        String clienteCorreo = (String) execution.getVariable("clienteCorreo");

        if (pedidoId != null) {
            Pedido pedido = pedidoService.buscarPedidoEnMemoriaPorId(pedidoId);

            if (pedido != null) {
                // Cambiar estado a PAGO_RECHAZADO
                boolean cambiado = pedidoService.cambiarEstado(pedido, EstadoPedido.PAGO_RECHAZADO);

                if (cambiado) {
                    System.out.println("⚠️ Pedido " + pedidoId + " marcado como RECHAZADO.");
                } else {
                    System.out.println("⚠️ No se pudo marcar el pedido " + pedidoId + " como RECHAZADO.");
                }
            } else {
                System.out.println("⚠️ Pedido con ID " + pedidoId + " no encontrado en memoria.");
            }
        }

        // Simulación de notificación al cliente
        if (clienteCorreo != null) {
            System.out.println("📧 Notificación enviada al cliente: " + clienteCorreo + " informando rechazo del pago.");
        } else {
            System.out.println("⚠️ No se recibió correo del cliente en las variables del proceso.");
        }
    }
}
