package cl.mapuescuela.service;

import cl.mapuescuela.model.EstadoPedido;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.Cliente;
import cl.mapuescuela.model.Producto;
import cl.mapuescuela.model.DetallePedido;
import cl.mapuescuela.model.ModalidadEntrega;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PedidoService {

    // Contador para asignar IDs a los pedidos creados desde Flowable
    private static final AtomicInteger pedidoCounter = new AtomicInteger(1);

    // Lista en memoria para pruebas (esto después se reemplaza por la BD)
    private final List<Pedido> pedidos = new ArrayList<>();

    // Cambia el estado de un pedido según las transiciones válidas
    public boolean cambiarEstado(Pedido pedido, EstadoPedido nuevoEstado) {
        EstadoPedido actual = pedido.getEstado();

        switch (actual) {
            case PENDIENTE:
                if (nuevoEstado == EstadoPedido.PAGO_REVISION) {
                    pedido.setEstado(nuevoEstado);
                    return true;
                }
                break;

            case PAGO_APROBADO:
                if (nuevoEstado == EstadoPedido.EN_PREPARACION) {
                    pedido.setEstado(nuevoEstado);
                    return true;
                }
                break;

            case EN_PREPARACION:
                if (nuevoEstado == EstadoPedido.LISTO_RETIRO || nuevoEstado == EstadoPedido.ENVIADO) {
                    pedido.setEstado(nuevoEstado);
                    return true;
                }
                break;

            case LISTO_RETIRO:
            case ENVIADO:
                if (nuevoEstado == EstadoPedido.FINALIZADO) {
                    pedido.setEstado(nuevoEstado);
                    return true;
                }
                break;

            case PAGO_RECHAZADO:
                if (nuevoEstado == EstadoPedido.CANCELADO) {
                    pedido.setEstado(nuevoEstado);
                    return true;
                }
                break;
        }

        return false; // transición inválida
    }

    // Validación manual del pago (se usa desde ComprobanteEndpoint)
    public boolean validarPago(Pedido pedido, boolean aprobado) {
        if (pedido.getEstado() == EstadoPedido.PAGO_REVISION) {
            if (aprobado) {
                pedido.setEstado(EstadoPedido.PAGO_APROBADO);
            } else {
                pedido.setEstado(EstadoPedido.PAGO_RECHAZADO);
            }
            return true;
        }
        return false; // no se puede validar si no está en revisión
    }

    /**
     * Crea un Pedido en memoria a partir de datos que vienen del proceso BPMN.
     * Por ahora se guarda en la lista local, después se reemplaza por persistencia en BD.
     */
    public Pedido crearPedidoDesdeVariables(Cliente cliente, List<Producto> productos,
                                            String modalidadEntregaStr, String direccionEntrega) {

        // Modalidad por defecto: RETIRO
        ModalidadEntrega modalidad = ModalidadEntrega.RETIRO;
        if (modalidadEntregaStr != null && !modalidadEntregaStr.isBlank()) {
            try {
                modalidad = ModalidadEntrega.valueOf(modalidadEntregaStr.toUpperCase());
            } catch (Exception e) {
                // si el valor no coincide, se mantiene RETIRO
            }
        }

        int id = pedidoCounter.getAndIncrement();

        // Si la lista de productos viene null, la inicializamos vacía
        List<Producto> listaProductos = productos != null ? productos : new ArrayList<>();

        // Convertir productos en detalles
        List<DetallePedido> detalles = new ArrayList<>();
        for (Producto p : listaProductos) {
            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(p);
            detalle.setCantidad(1); // cantidad por defecto
            detalle.setPrecioUnitario(p.getPrecio()); // si tu Producto tiene precio
            detalles.add(detalle);
        }

        // Crear el pedido con los datos recibidos
        Pedido pedido = new Pedido(id, cliente, detalles, modalidad, direccionEntrega);

        // Estado inicial siempre PENDIENTE
        pedido.setEstado(EstadoPedido.PENDIENTE);

        // Guardar en la lista temporal
        synchronized (pedidos) {
            pedidos.add(pedido);
        }
        return pedido;
    }

    // Devuelve todos los pedidos guardados en memoria (solo para pruebas)
    public List<Pedido> listarPedidosEnMemoria() {
        synchronized (pedidos) {
            return new ArrayList<>(pedidos);
        }
    }

    // Busca un pedido por ID en la lista temporal
    public Pedido buscarPedidoEnMemoriaPorId(int id) {
        synchronized (pedidos) {
            return pedidos.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
    }
}
