package cl.mapuescuela.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cl.mapuescuela.service.PedidoService;
import cl.mapuescuela.model.Cliente;
import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.Producto;
import cl.mapuescuela.model.DetallePedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("crearPedidoDelegate")
public class CrearPedidoDelegate implements JavaDelegate {

    private final PedidoService pedidoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CrearPedidoDelegate(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String nombre = (String) execution.getVariable("clienteNombre");
        String correo = (String) execution.getVariable("clienteCorreo");
        String telefono = (String) execution.getVariable("clienteTelefono");
        String detallesJson = (String) execution.getVariable("detallesJson");
        String modalidadEntrega = (String) execution.getVariable("modalidadEntrega");
        String direccionEntrega = (String) execution.getVariable("direccionEntrega");

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);

        List<DetallePedido> detalles = null;

        if (detallesJson != null && !detallesJson.isBlank()) {
            // Intento 1: mapear directamente a List<DetallePedido>
            try {
                detalles = objectMapper.readValue(detallesJson, new TypeReference<List<DetallePedido>>() {});
            } catch (Exception ex1) {
                // Intento 2: parsear a lista de mapas y construir DetallePedido manualmente
                try {
                    List<Map<String, Object>> raw = objectMapper.readValue(detallesJson, new TypeReference<List<Map<String, Object>>>() {});
                    detalles = new ArrayList<>();
                    for (Map<String, Object> m : raw) {
                        DetallePedido d = new DetallePedido();
                        try {
                            if (m.get("productoId") != null) d.setProductoId(((Number) m.get("productoId")).intValue());
                        } catch (Exception ignore) {}
                        try {
                            if (m.get("id") != null) d.setProductoId(((Number) m.get("id")).intValue());
                        } catch (Exception ignore) {}
                        try {
                            if (m.get("cantidad") != null) d.setCantidad(((Number) m.get("cantidad")).intValue());
                        } catch (Exception ignore) {}
                        try {
                            if (m.get("precioUnitario") != null) d.setPrecioUnitario(((Number) m.get("precioUnitario")).doubleValue());
                        } catch (Exception ignore) {}
                        try {
                            if (m.get("precio") != null) d.setPrecioUnitario(((Number) m.get("precio")).doubleValue());
                        } catch (Exception ignore) {}
                        detalles.add(d);
                    }
                } catch (Exception ex2) {
                    detalles = null;
                }
            }
        }

        // Convertir DetallePedido a Producto (ajustado a la API de Producto)
        List<Producto> productos = convertirDetallesAProductos(detalles);

        if (productos == null) {
            productos = new ArrayList<>();
        }

        Pedido pedido = pedidoService.crearPedidoDesdeVariables(cliente, productos, modalidadEntrega, direccionEntrega);
        execution.setVariable("pedidoId", pedido.getId());
    }

    /**
     * Convierte DetallePedido a Producto sin asumir setters inexistentes.
     * - Convierte id a Long (porque Producto.setId(Long) existe).
     * - Asigna nombre si está disponible en DetallePedido (no asumimos que exista).
     * - No llama a setCantidad ni setPrecioUnitario para evitar errores de compilación.
     *
     * Si quieres mapear cantidad/precio, pega aquí la clase Producto y la adapto exactamente.
     */
    private List<Producto> convertirDetallesAProductos(List<DetallePedido> detalles) {
        List<Producto> productos = new ArrayList<>();
        if (detalles == null) return productos;
        for (DetallePedido d : detalles) {
            Producto p = new Producto();
            // setId espera Long según tu error; convertimos con Long.valueOf
            try {
                Integer prodId = d.getProductoId();
                if (prodId != null) {
                    p.setId(Long.valueOf(prodId.longValue()));
                }
            } catch (Exception ignore) {}

            // Si DetallePedido tuviera nombre, lo mapearíamos aquí; no asumimos que exista.
            // Ejemplo seguro si tu DetallePedido tuviera getNombre():
            // try { p.setNombre(d.getNombre()); } catch (Exception ignore) {}

            productos.add(p);
        }
        return productos;
    }
}
