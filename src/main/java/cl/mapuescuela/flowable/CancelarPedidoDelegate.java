package cl.mapuescuela.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import cl.mapuescuela.service.PedidoService;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.EstadoPedido;

@Component("cancelarPedidoDelegate")
public class CancelarPedidoDelegate implements JavaDelegate {

    private final PedidoService pedidoService;

    // Inyección de PedidoService
    public CancelarPedidoDelegate(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener el ID del pedido desde las variables del proceso
        Integer pedidoId = (Integer) execution.getVariable("pedidoId");

        if (pedidoId != null) {
            Pedido pedido = pedidoService.buscarPedidoEnMemoriaPorId(pedidoId);

            if (pedido != null) {
                // Cambiar estado a CANCELADO
                boolean cambiado = pedidoService.cambiarEstado(pedido, EstadoPedido.CANCELADO);

                if (cambiado) {
                    System.out.println("✅ Pedido " + pedidoId + " cancelado correctamente.");
                } else {
                    System.out.println("⚠️ No se pudo cancelar el pedido " + pedidoId + " (transición inválida).");
                }
            } else {
                System.out.println("⚠️ Pedido con ID " + pedidoId + " no encontrado en memoria.");
            }
        } else {
            System.out.println("⚠️ No se recibió un pedidoId en las variables del proceso.");
        }
    }
}
