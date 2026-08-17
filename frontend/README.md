# Mapuescuela Frontend — MVP

Frontend estático preparado para la Evaluación de Integración de Plataformas.

## Tecnologías
- HTML5
- CSS3
- JavaScript puro
- localStorage para modo demostración
- Adaptador preparado para integración SOAP

## Pantallas
- `index.html`: catálogo público, buscador, filtro y detalle.
- `carrito.html`: carrito, cantidades y total.
- `checkout.html`: datos del cliente, retiro/despacho y comprobante.
- `estado.html`: consulta del estado del pedido.
- `admin.html`: gestión básica de pedidos y productos.
- `soap-client.js`: punto de integración con el backend SOAP.

## Cómo ejecutarlo

### Opción simple
Haz doble clic en `index.html`.

### Opción recomendada en VS Code
1. Instala la extensión **Live Server**.
2. Abre esta carpeta.
3. Haz clic derecho en `index.html`.
4. Selecciona **Open with Live Server**.

## Flujo de demostración
1. Abre `index.html`.
2. Agrega productos.
3. Ve al carrito.
4. Finaliza la compra.
5. Copia el código `MAP-XXXXXX`.
6. Abre `admin.html`.
7. Cambia el estado del pedido:
   - Pendiente de pago
   - Pago en revisión
   - Pago aprobado
   - En preparación
   - Listo para retiro / Enviado
   - Finalizado
8. Revisa el estado desde `estado.html`.

Cuando se aprueba un pago, el frontend de demostración descuenta el stock.

## Integración SOAP

El archivo `soap-client.js` contiene:

```js
const SOAP_CONFIG = {
  enabled: false,
  endpoint: "http://localhost:8080/ws",
  namespace: "http://mapuescuela.cl/ws"
};
```

Cuando el backend esté listo:

1. Cambiar `enabled` a `true`.
2. Poner el endpoint real.
3. Poner el namespace real del WSDL.
4. Ajustar cada operación al XML que defina el backend.

El frontend queda desacoplado para que primero pueda demostrarse visualmente y luego conectarse al servicio SOAP.

## Importante
Este proyecto es un **MVP académico**, no una tienda completa. La administración no incluye autenticación porque el objetivo aquí es demostrar la interacción del usuario, los pedidos y la futura integración con Flowable/servicios.


## Fotografías
Esta versión utiliza fotografías reales obtenidas desde Unsplash y cargadas mediante URL.
Por eso, para visualizar las imágenes del catálogo el computador debe tener conexión a Internet.
El funcionamiento del carrito y del resto del MVP continúa siendo local.

Fotografías utilizadas bajo la licencia de Unsplash.


## Comentarios de código

Se agregaron comentarios breves en las secciones principales de `app.js`,
`soap-client.js` y las páginas HTML. El objetivo es explicar las decisiones
más importantes sin llenar el código de comentarios innecesarios.
