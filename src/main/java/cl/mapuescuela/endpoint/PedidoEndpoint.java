package cl.mapuescuela.endpoint;

import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.EstadoPedido;
import cl.mapuescuela.model.ModalidadEntrega;
import cl.mapuescuela.model.Cliente;
import cl.mapuescuela.model.Producto;
import cl.mapuescuela.service.PedidoService;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;

import java.util.List;

@WebService
public class PedidoEndpoint {

    // Servicio de pedidos (por ahora en memoria, después se conecta a BD)
    private final PedidoService service = new PedidoService();

    // Crear un nuevo pedido
    @WebMethod
    public Pedido crearPedido(Cliente cliente, List<Producto> productos,
                              ModalidadEntrega modalidad, String direccionEntrega) {
        if (modalidad == ModalidadEntrega.DESPACHO &&
                (direccionEntrega == null || direccionEntrega.isBlank())) {
            throw new RuntimeException("La dirección de entrega es obligatoria para modalidad DESPACHO");
        }
        return service.crearPedidoDesdeVariables(cliente, productos,
                modalidad.name(), direccionEntrega);
    }

    // Consultar un pedido por ID
    @WebMethod
    public Pedido consultarPedido(int id) {
        Pedido pedido = service.buscarPedidoEnMemoriaPorId(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado");
        }
        return pedido;
    }

    // Cambiar el estado de un pedido
    @WebMethod
    public Pedido cambiarEstado(int id, EstadoPedido nuevoEstado) {
        Pedido pedido = service.buscarPedidoEnMemoriaPorId(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado");
        }
        boolean ok = service.cambiarEstado(pedido, nuevoEstado);
        if (!ok) {
            throw new RuntimeException("Transición inválida desde " + pedido.getEstado() + " a " + nuevoEstado);
        }
        return pedido;
    }
}
