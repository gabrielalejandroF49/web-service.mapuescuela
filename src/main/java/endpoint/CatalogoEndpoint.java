package endpoint;

import cl.mapuescuela.model.Producto;
import cl.mapuescuela.service.CatalogoService;
import cl.mapuescuela.service.impl.CatalogoServiceImpl;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public class CatalogoEndpoint {

    private CatalogoService service = new CatalogoServiceImpl();

    @WebMethod
    public List<Producto> listarProductos() {
        return service.listarProductos();
    }

    @WebMethod
    public Producto obtenerProducto(Long id) {
        return service.obtenerProducto(id);
    }

    @WebMethod
    public boolean actualizarStock(Long id, int cantidad) {
        return service.actualizarStock(id, cantidad);
    }
}
