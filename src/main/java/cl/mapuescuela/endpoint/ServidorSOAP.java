package cl.mapuescuela.endpoint;

/**
 * El servidor SOAP ahora se inicia junto con Spring Boot mediante SoapServerConfig,
 * para que CatalogoEndpoint pueda usar ProductoRepository y la base de datos real.
 */
public final class ServidorSOAP {
    private ServidorSOAP() {}
}
