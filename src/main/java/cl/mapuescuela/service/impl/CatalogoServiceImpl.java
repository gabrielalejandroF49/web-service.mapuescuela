package cl.mapuescuela.service.impl;

import cl.mapuescuela.model.Producto;
import cl.mapuescuela.service.CatalogoService;
import java.util.ArrayList;
import java.util.List;

public class CatalogoServiceImpl implements CatalogoService {
    private List<Producto> productos = new ArrayList<>();

    @Override
    public List<Producto> listarProductos() {
        return productos;
    }

    @Override
    public Producto obtenerProducto(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean actualizarStock(Long id, int cantidad) {
        Producto p = obtenerProducto(id);
        if (p != null && p.getStock() >= cantidad) {
            p.setStock(p.getStock() - cantidad);
            return true;
        }
        return false;
    }
}
