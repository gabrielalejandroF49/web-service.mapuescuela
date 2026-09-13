package cl.mapuescuela.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import cl.mapuescuela.service.PedidoService;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.DetallePedido;
import cl.mapuescuela.model.Producto;

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
                System.out.println("✅ Inventario actualizado para el pedido " + pedidoId);

                // Recorrer los detalles del pedido y actualizar stock
                for (DetallePedido detalle : pedido.getDetalles()) {
                    Producto producto = detalle.getProducto();
                    if (producto != null) {
                        System.out.println(" - Producto: " + producto.getNombre() +
                                " | Cantidad: " + detalle.getCantidad());
                        // Aquí iría la lógica real de descontar stock:
                        // producto.setStock(producto.getStock() - detalle.getCantidad());
                    }
                }

            } else {
                System.out.println("⚠️ Pedido con ID " + pedidoId + " no encontrado en memoria.");
            }
        } else {
            System.out.println("⚠️ No se recibió un pedidoId en las variables del proceso.");
        }
    }
}
