# 📦 Backend SOAP - Mapuescuela

Este proyecto implementa un servicio **SOAP** en **Jakarta EE 10 + Metro JAX-WS** para el caso Mapuescuela.  
El backend gestiona productos y está preparado para extenderse con pedidos y comprobantes de pago.

---

## 📂 Estructura del proyecto

- `Producto.java` → Entidad que representa un producto (id, nombre, descripción, categoría, precio, stock, estado).
- `CatalogoService.java` → Interfaz con operaciones de negocio sobre productos.
- `CatalogoServiceImpl.java` → Implementación del catálogo (listar, obtener, actualizar stock).
- `CatalogoEndpoint.java` → Endpoint SOAP que expone las operaciones del catálogo.
- `ServidorSOAP.java` → Clase principal que publica el servicio en `http://localhost:9090/ws/catalogo?wsdl`.

---

## 🔧 Operaciones SOAP disponibles

- `listarProductos()` → Devuelve todos los productos disponibles.
- `obtenerProducto(Long id)` → Devuelve el detalle de un producto específico.
- `actualizarStock(Long id, int cantidad)` → Actualiza el stock de un producto.

---

## 🚀 Cómo ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/gabrielalejandroF49/web-service.mapuescuela.git
