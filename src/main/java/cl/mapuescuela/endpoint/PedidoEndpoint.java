package cl.mapuescuela.endpoint;

import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.EstadoPedido;
import cl.mapuescuela.model.ModalidadEntrega;
import cl.mapuescuela.model.Cliente;
import cl.mapuescuela.model.Producto;
import cl.mapuescuela.service.PedidoService;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;

import java.util.ArrayList;
import java.util.List;

@WebService
public class PedidoEndpoint {

    private List<Pedido> pedidos = new ArrayList<>();
    private int pedidoCounter = 1;

    @WebMethod
    public Pedido crearPedido(Cliente cliente, List<Producto> productos,
                              ModalidadEntrega modalidad, String direccionEntrega) {
        // Validación de modalidad de entrega
        if (modalidad == ModalidadEntrega.DESPACHO &&
                (direccionEntrega == null || direccionEntrega.isBlank())) {
            throw new RuntimeException("La dirección de entrega es obligatoria para modalidad DESPACHO");
        }

        Pedido pedido = new Pedido(pedidoCounter++, cliente, productos, modalidad, direccionEntrega);
        pedidos.add(pedido);
        return pedido;
    }

    @WebMethod
    public Pedido consultarPedido(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new RuntimeException("Pedido no encontrado");
    }

    @WebMethod
    public Pedido cambiarEstado(int id, EstadoPedido nuevoEstado) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                PedidoService service = new PedidoService();
                boolean ok = service.cambiarEstado(p, nuevoEstado);
                if (!ok) {
                    throw new RuntimeException("Transición inválida desde " + p.getEstado() + " a " + nuevoEstado);
                }
                return p; // devolvemos el pedido actualizado
            }
        }
        throw new RuntimeException("Pedido no encontrado");
    }
}
