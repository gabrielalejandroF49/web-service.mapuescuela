package cl.mapuescuela.endpoint;

import cl.mapuescuela.model.Categoria;
import cl.mapuescuela.model.Producto;
import cl.mapuescuela.repository.CategoriaRepository;
import cl.mapuescuela.service.CatalogoService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@WebService(serviceName = "CatalogoService", targetNamespace = "http://endpoint.mapuescuela.cl/")
public class CatalogoEndpoint {  private final CatalogoService service;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public CatalogoEndpoint(CatalogoService service) {
        this.service = service;
    }
    @WebMethod
    public List<Producto> listarProductos() {
        return service.listarProductos();
    }

    @WebMethod
    public Producto obtenerProducto(@WebParam(name = "id") Long id) {
        return service.obtenerProducto(id);
    }

    @WebMethod
    public boolean actualizarStock(@WebParam(name = "id") Long id,
                                   @WebParam(name = "cantidad") int cantidad) {
        return service.actualizarStock(id, cantidad);
    }
    @WebMethod
    public Producto crearProducto(
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "categoriaId") Long categoriaId,
            @WebParam(name = "precio") double precio,
            @WebParam(name = "stock") int stock,
            @WebParam(name = "descripcion") String descripcion,
            @WebParam(name = "estado") String estado) {

        Producto p = new Producto();
        p.setNombre(nombre);

        // Buscar la categoría en la BD
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        p.setCategoria(categoria);

        p.setPrecio(precio);
        p.setStock(stock);
        p.setDescripcion(descripcion);
        p.setEstado(estado);

        return service.crearProducto(p);
    }


}
