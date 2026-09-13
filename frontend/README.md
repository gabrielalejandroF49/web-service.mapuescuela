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


## Mejoras posteriores a la retroalimentación de la Entrega 2

Se incorporaron mejoras específicas en las interfaces de usuario:

### Identidad visual
- Se incorporó el logotipo institucional de Mapuescuela en la navegación, portada y acceso del equipo.
- Se actualizó la paleta visual tomando como referencia el azul, celeste, verde y verde claro presentes en el logotipo.
- Se reforzó el lema institucional **“Educa-acción para el buen vivir”**.

### Control de acceso
- Las operaciones públicas, como consultar productos, generar un pedido y revisar su estado, continúan disponibles para el usuario.
- El panel que permite modificar productos o cambiar estados de pedidos ahora exige inicio de sesión.
- Las funciones sensibles validan que exista una sesión con rol `ADMIN` antes de ejecutarse.
- Se incorporó cierre de sesión.

### Credenciales de demostración local
- Usuario: `voluntario`
- Contraseña: `Mapu2026!`

> Importante: esta autenticación protege la interfaz del prototipo local. En un ambiente productivo, la autenticación y autorización deben validarse también del lado del backend. Nunca debe confiarse únicamente en controles implementados en JavaScript del navegador.

### Ejecución local
```bash
python -m http.server 8000
```

Luego abrir:
`http://localhost:8000`

