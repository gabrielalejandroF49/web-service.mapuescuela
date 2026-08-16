package cl.mapuescuela.service;

import cl.mapuescuela.model.EstadoPedido;
import cl.mapuescuela.model.Pedido;

public class PedidoService {

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
}
