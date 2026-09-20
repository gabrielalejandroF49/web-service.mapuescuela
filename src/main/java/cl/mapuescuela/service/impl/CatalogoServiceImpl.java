package cl.mapuescuela.service.impl;

import cl.mapuescuela.model.Producto;
import cl.mapuescuela.repository.ProductoRepository;
import cl.mapuescuela.service.CatalogoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogoServiceImpl implements CatalogoService {
    private final ProductoRepository productoRepository;

    public CatalogoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public boolean actualizarStock(Long id, int cantidad) {
        return productoRepository.findById(id).map(producto -> {
            int nuevoStock = producto.getStock() + cantidad;
            if (nuevoStock < 0) return false;
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
            return true;
        }).orElse(false);
    }
    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

}
