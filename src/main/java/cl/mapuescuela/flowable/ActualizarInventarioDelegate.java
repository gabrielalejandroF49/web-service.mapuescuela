package cl.mapuescuela.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import cl.mapuescuela.service.PedidoService;
import cl.mapuescuela.model.Pedido;

@Component("actualizarInventarioDelegate")
public class ActualizarInventarioDelegate implements JavaDelegate {

    private final PedidoService pedidoService;

    // Inyección de PedidoService
    public ActualizarInventarioDelegate(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener el ID del pedido desde las variables del proceso
        Integer pedidoId = (Integer) execution.getVariable("pedidoId");

        if (pedidoId != null) {
            Pedido pedido = pedidoService.buscarPedidoEnMemoriaPorId(pedidoId);

            if (pedido != null) {
                // Aquí iría la lógica real de actualización de inventario
                // Por ahora simulamos con un mensaje en consola
                System.out.println("✅ Inventario actualizado para el pedido " + pedidoId);

                // Podrías recorrer los productos del pedido y descontar stock
                pedido.getProductos().forEach(producto -> {
                    System.out.println(" - Producto: " + producto.getNombre() + " actualizado en inventario.");
                });

            } else {
                System.out.println("⚠️ Pedido con ID " + pedidoId + " no encontrado en memoria.");
            }
        } else {
            System.out.println("⚠️ No se recibió un pedidoId en las variables del proceso.");
        }
    }
}
