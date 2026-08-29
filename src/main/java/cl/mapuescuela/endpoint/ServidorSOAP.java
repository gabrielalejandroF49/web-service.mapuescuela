package cl.mapuescuela.endpoint;

import cl.mapuescuela.endpoint.CatalogoEndpoint;
import jakarta.xml.ws.Endpoint;

public class ServidorSOAP {
    public static void main(String[] args) {
        String url = "http://localhost:9090/ws/catalogo";
        Endpoint.publish(url, new CatalogoEndpoint());
        System.out.println("Servicio SOAP publicado en: " + url + "?wsdl");
    }
}
