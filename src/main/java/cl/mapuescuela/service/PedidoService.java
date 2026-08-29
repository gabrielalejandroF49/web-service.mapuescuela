package cl.mapuescuela.service;

import cl.mapuescuela.model.EstadoPedido;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.Cliente;
import cl.mapuescuela.model.Producto;
import cl.mapuescuela.model.ModalidadEntrega;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PedidoService {

    // Contador y almacenamiento en memoria para pedidos creados desde Flowable
    private static final AtomicInteger pedidoCounter = new AtomicInteger(1);
    private final List<Pedido> pedidos = new ArrayList<>();

    // Control automático de transiciones (flujo normal)
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

    // Validación humana: se ejecuta desde ComprobanteEndpoint
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

    // ---------------------------
    // Método añadido para Flowable
    // ---------------------------
    /**
     * Crea un Pedido en memoria a partir de datos que provienen del proceso.
     * Se adapta al constructor existente de Pedido (id, Cliente, List<Producto>, ModalidadEntrega, direccion).
     *
     * @param cliente               objeto Cliente (puede ser construido por el delegate)
     * @param productos             lista de Producto (puede ser null)
     * @param modalidadEntregaStr   "RETIRO" o "DESPACHO" (case-insensitive). Si es inválido, se usa RETIRO.
     * @param direccionEntrega      dirección (puede ser null)
     * @return Pedido creado en memoria
     */
    public Pedido crearPedidoDesdeVariables(Cliente cliente, List<Producto> productos,
                                            String modalidadEntregaStr, String direccionEntrega) {

        ModalidadEntrega modalidad = ModalidadEntrega.RETIRO;
        if (modalidadEntregaStr != null && !modalidadEntregaStr.isBlank()) {
            try {
                modalidad = ModalidadEntrega.valueOf(modalidadEntregaStr.toUpperCase());
            } catch (Exception e) {
                // mantener RETIRO por defecto si el valor no coincide
            }
        }

        int id = pedidoCounter.getAndIncrement();

        // Asegurar que la lista de productos no sea null
        List<Producto> listaProductos = productos != null ? productos : new ArrayList<>();

        // Crear Pedido usando el constructor que ya existe en tu clase Pedido
        Pedido pedido = new Pedido(id, cliente, listaProductos, modalidad, direccionEntrega);

        // Estado ya se inicializa en el constructor de Pedido según tu clase,
        // pero por seguridad intentamos dejarlo consistente
        try {
            pedido.setEstado(EstadoPedido.PENDIENTE);
        } catch (Exception ignore) {
            // Si no existe el setter, no hacemos nada (tu Pedido ya inicializa el estado)
        }

        // Guardar en lista en memoria
        synchronized (pedidos) {
            pedidos.add(pedido);
        }
        return pedido;
    }

    // Métodos auxiliares (opcionales) para acceder a la lista en memoria si los necesitas
    public List<Pedido> listarPedidosEnMemoria() {
        synchronized (pedidos) {
            return new ArrayList<>(pedidos);
        }
    }

    public Pedido buscarPedidoEnMemoriaPorId(int id) {
        synchronized (pedidos) {
            return pedidos.stream().filter(p -> {
                try {
                    return p.getId() == id;
                } catch (Exception e) {
                    return false;
                }
            }).findFirst().orElse(null);
        }
    }
}
