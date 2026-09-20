package cl.mapuescuela.service;

import cl.mapuescuela.model.Producto;
import java.util.List;

public interface CatalogoService {
    List<Producto> listarProductos();
    Producto obtenerProducto(Long id);
    boolean actualizarStock(Long id, int cantidad);
    Producto crearProducto(Producto producto);

}
