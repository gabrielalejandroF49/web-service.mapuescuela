package cl.mapuescuela.endpoint;

import cl.mapuescuela.model.Producto;
import cl.mapuescuela.service.CatalogoService;
import cl.mapuescuela.service.impl.CatalogoServiceImpl;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public class CatalogoEndpoint {

    // Servicio del catálogo (por ahora en memoria, después se conecta a BD)
    private final CatalogoService service = new CatalogoServiceImpl();

    // Devuelve todos los productos disponibles
    @WebMethod
    public List<Producto> listarProductos() {
        return service.listarProductos();
    }

    // Busca un producto por su ID
    @WebMethod
    public Producto obtenerProducto(Long id) {
        return service.obtenerProducto(id);
    }

    // Actualiza el stock de un producto (positivo = aumenta, negativo = disminuye)
    @WebMethod
    public boolean actualizarStock(Long id, int cantidad) {
        return service.actualizarStock(id, cantidad);
    }
}
