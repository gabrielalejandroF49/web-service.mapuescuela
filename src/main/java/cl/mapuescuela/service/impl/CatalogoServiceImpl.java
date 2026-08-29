package cl.mapuescuela.service.impl;

import cl.mapuescuela.model.Producto;
import cl.mapuescuela.service.CatalogoService;

import java.util.ArrayList;
import java.util.List;

public class CatalogoServiceImpl implements CatalogoService {

    // Lista en memoria para pruebas (después se reemplaza por BD)
    private final List<Producto> productos = new ArrayList<>();

    @Override
    public List<Producto> listarProductos() {
        return new ArrayList<>(productos); // devolvemos copia para evitar modificaciones externas
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
        if (p != null) {
            int nuevoStock = p.getStock() + cantidad;
            // cantidad positiva = reposición, negativa = venta
            if (nuevoStock >= 0) {
                p.setStock(nuevoStock);
                return true;
            }
        }
        return false; // producto no encontrado o stock inválido
    }
}
