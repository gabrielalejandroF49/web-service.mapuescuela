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
        // Variables recibidas desde el proceso BPMN
        String nombre = (String) execution.getVariable("clienteNombre");
        String correo = (String) execution.getVariable("clienteCorreo");
        String telefono = (String) execution.getVariable("clienteTelefono");
        String detallesJson = (String) execution.getVariable("detallesJson");
        String modalidadEntrega = (String) execution.getVariable("modalidadEntrega");
        String direccionEntrega = (String) execution.getVariable("direccionEntrega");

        // Construcción del cliente
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setEmail(correo);
        cliente.setTelefono(telefono);

        // Parseo de detalles desde JSON
        List<DetallePedido> detalles = parsearDetalles(detallesJson);

        // Conversión de detalles a productos
        List<Producto> productos = convertirDetallesAProductos(detalles);

        // Crear pedido en memoria usando el servicio
        Pedido pedido = pedidoService.crearPedidoDesdeVariables(cliente, productos, modalidadEntrega, direccionEntrega);

        // Guardar ID en variables del proceso
        execution.setVariable("pedidoId", pedido.getId());
    }

    // Intenta parsear el JSON de detalles a una lista de DetallePedido
    private List<DetallePedido> parsearDetalles(String detallesJson) {
        if (detallesJson == null || detallesJson.isBlank()) return null;
        try {
            return objectMapper.readValue(detallesJson, new TypeReference<List<DetallePedido>>() {});
        } catch (Exception ex1) {
            try {
                List<Map<String, Object>> raw = objectMapper.readValue(detallesJson, new TypeReference<List<Map<String, Object>>>() {});
                List<DetallePedido> detalles = new ArrayList<>();
                for (Map<String, Object> m : raw) {
                    DetallePedido d = new DetallePedido();

                    // Crear producto y asignarlo al detalle
                    if (m.get("productoId") != null || m.get("id") != null) {
                        Producto producto = new Producto();
                        Number idNum = (Number) (m.get("productoId") != null ? m.get("productoId") : m.get("id"));
                        producto.setId(idNum.longValue());
                        d.setProducto(producto);
                    }

                    if (m.get("cantidad") != null) d.setCantidad(((Number) m.get("cantidad")).intValue());
                    if (m.get("precioUnitario") != null) d.setPrecioUnitario(((Number) m.get("precioUnitario")).doubleValue());
                    if (m.get("precio") != null) d.setPrecioUnitario(((Number) m.get("precio")).doubleValue());

                    detalles.add(d);
                }
                return detalles;
            } catch (Exception ex2) {
                return null;
            }
        }
    }

    // Convierte DetallePedido a Producto usando solo los campos que existen
    private List<Producto> convertirDetallesAProductos(List<DetallePedido> detalles) {
        List<Producto> productos = new ArrayList<>();
        if (detalles == null) return productos;
        for (DetallePedido d : detalles) {
            Producto p = new Producto();
            if (d.getProducto() != null && d.getProducto().getId() != null) {
                p.setId(d.getProducto().getId());
            }
            if (d.getPrecioUnitario() != null) {
                p.setPrecio(d.getPrecioUnitario());
            }
            if (d.getCantidad() != null) {
                p.setStock(d.getCantidad());
            }
            productos.add(p);
        }
        return productos;
    }
}
