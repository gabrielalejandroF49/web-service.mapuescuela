# Mapuescuela – integración Frontend ↔ SOAP ↔ SQL Server

## Cambios realizados
- El frontend SOAP apunta a `http://localhost:9090/ws/catalogo` y está habilitado.
- `CatalogoServiceImpl` dejó de usar una lista vacía en memoria y usa `ProductoRepository`.
- El endpoint SOAP se inicia dentro del contexto Spring, por lo que comparte JPA/SQL Server.
- Se agregó CORS al servidor SOAP para permitir el frontend local en `http://localhost:8000`.
- Al abrir el catálogo, `app.js` intenta `listarProductos()` y sincroniza los productos en LocalStorage; si el backend está caído, conserva el modo demo.
- Las credenciales de BD ya no quedan escritas en el repositorio; se leen desde variables de entorno.

## Prueba en Windows PowerShell
1. Abrir PowerShell en la raíz del proyecto.
2. Definir credenciales (usar la contraseña NUEVA después de rotar la que estuvo publicada):
   `$env:DB_USERNAME="mapuescueladmin"`
   `$env:DB_PASSWORD="TU_PASSWORD_NUEVA"`
3. Levantar backend:
   `mvn spring-boot:run`
4. Verificar en navegador:
   `http://localhost:9090/ws/catalogo?wsdl`
5. En otra terminal:
   `cd frontend`
   `python -m http.server 8000`
6. Abrir:
   `http://localhost:8000`
7. F12 > Consola debe mostrar `SOAP conectado: N producto(s) cargado(s).`

## Importante
La contraseña que estaba en `application.properties` apareció en una copia del repositorio. Debe rotarse en Azure SQL y no volver a subirse a Git.
