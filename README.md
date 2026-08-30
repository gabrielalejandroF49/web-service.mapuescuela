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


   # 📦 SOAP de backend - Mapuescuela (Actualizacion)

Este proyecto implementa un servicio SOAP en **Jakarta EE 10 + Metro JAX-WS** para el caso Mapuescuela.  
El backend gestiona productos y está preparado para extenderse con pedidos y comprobantes de pago.

---

## 📂 Estructura del proyecto

- `Producto.java` → Entidad que representa un producto (id, nombre, descripción, categoría, precio, stock, estado).
- `CatalogoService.java` → Interfaz con operaciones de negocio sobre productos.
- `CatalogoServiceImpl.java` → Implementación del catálogo (listar, obtener, actualizar stock).
- `CatalogoEndpoint.java` → Endpoint SOAP que expone las operaciones del catálogo.
- `ServidorSOAP.java` → Clase principal que publica el servicio en `http://localhost:9090/ws/catalogo?wsdl`.

---

## 🔧 Operaciones SOAP disponibles (Catálogo)

- `listarProductos()` → Devuelve todos los productos disponibles.
- `obtenerProducto(Long id)` → Devuelve el detalle de un producto específico.
- `actualizarStock(Long id, int cantidad)` → Actualiza el stock de un producto.

---

## 🚀 Cómo ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/gabrielalejandroF49/web-service.mapuescuela.git

   ---

# 🖥️ Frontend - Mapuescuela

Como parte de la solución propuesta para Mapuescuela, se desarrolló un frontend web que permite representar y probar las principales interacciones que tendrá el usuario con la plataforma.

Actualmente el frontend funciona de manera local y se encuentra preparado para su posterior integración con el backend y los servicios SOAP del proyecto.

## 📂 Estructura del frontend

- `index.html` → Página principal y catálogo de productos.
- `carrito.html` → Carrito de compras y resumen de productos seleccionados.
- `checkout.html` → Registro de datos del cliente y generación del pedido.
- `estado.html` → Consulta del estado de un pedido mediante su código.
- `admin.html` → Panel básico para gestión de productos y pedidos.
- `estilos.css` → Diseño visual y adaptación de las distintas vistas.
- `app.js` → Lógica principal y funcionamiento del frontend.
- `soap-client.js` → Archivo preparado para la futura comunicación con el servicio SOAP.

## 🔧 Funcionalidades implementadas

- Visualización del catálogo de productos.
- Búsqueda y filtrado por categoría.
- Visualización del detalle de productos.
- Carrito de compras.
- Control de cantidades según stock disponible.
- Cálculo del total de la compra.
- Registro de datos del cliente.
- Selección entre retiro y despacho.
- Generación de pedidos.
- Generación de código de seguimiento.
- Consulta del estado del pedido.
- Panel básico de administración.
- Gestión de productos y estados de pedidos.

## 💻 Tecnologías utilizadas

- HTML5
- CSS3
- JavaScript
- LocalStorage para almacenamiento temporal durante las pruebas locales.

## 🔄 Estado actual

El frontend se encuentra funcional en ambiente local.

Durante esta primera etapa se utiliza `LocalStorage` para realizar pruebas del catálogo, carrito, pedidos y administración sin depender del funcionamiento del backend.

La integración con el Web Service SOAP se realizará progresivamente utilizando las operaciones disponibles en el backend.

## 🔗 Integración prevista con SOAP

Las operaciones actuales del backend que podrán ser utilizadas por el frontend son:

- `listarProductos()` → Obtener los productos disponibles.
- `obtenerProducto(Long id)` → Consultar el detalle de un producto.
- `actualizarStock(Long id, int cantidad)` → Actualizar el stock después de una operación de venta.

Las demás funcionalidades serán integradas a medida que se incorporen nuevas operaciones al backend.

## 🚀 Ejecución local del frontend

Desde la carpeta donde se encuentra el frontend ejecutar:

```bash
python -m http.server 8000

Cambios recientes

Se agregó soporte de persistencia con Spring Data JPA.

Modelos convertidos en entidades:

Producto

Cliente

Pedido

Comprobante

DetallePedido

Configuración lista para trabajar con SQL Server mediante el driver mssql-jdbc.

Relaciones entre entidades definidas con anotaciones JPA (@ManyToOne, @ManyToMany, @Enumerated).

Configuración de base de datos
En application.properties se debe definir la conexión a SQL Server:

spring.datasource.url=jdbc:sqlserver://<host>:1433;databaseName=<nombreDB>
spring.datasource.username=<usuario>
spring.datasource.password=<contraseña>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect


