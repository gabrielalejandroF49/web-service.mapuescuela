package cl.mapuescuela.app.controller;

import cl.mapuescuela.model.Pedido;
import cl.mapuescuela.model.DetallePedido;
import cl.mapuescuela.model.Cliente;
import cl.mapuescuela.repository.PedidoRepository;
import cl.mapuescuela.repository.DetallePedidoRepository;
import cl.mapuescuela.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            DetallePedidoRepository detallePedidoRepository,
                            ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    // GET: listar todos los pedidos
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    // GET: obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: crear pedido
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        // Validar cliente
        if (pedido.getCliente() != null) {
            Optional<Cliente> cliente = clienteRepository.findById(pedido.getCliente().getId());
            if (cliente.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            pedido.setCliente(cliente.get());
        }

        // Setear relación inversa en los detalles
        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
            }
        }

        // Guardar pedido y detalles en cascada
        Pedido nuevoPedido = pedidoRepository.save(pedido);

        return ResponseEntity.ok(nuevoPedido);
    }


    // PUT: actualizar estado del pedido
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> updateEstado(@PathVariable Integer id, @RequestBody Pedido pedidoDetails) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setEstado(pedidoDetails.getEstado());
                    return ResponseEntity.ok(pedidoRepository.save(pedido));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: eliminar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable Integer id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedidoRepository.delete(pedido);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
