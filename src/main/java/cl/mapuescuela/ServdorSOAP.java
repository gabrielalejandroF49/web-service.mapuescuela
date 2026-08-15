package cl.mapuescuela;

import cl.mapuescuela.endpoint.CatalogoEndpoint;
import jakarta.xml.ws.Endpoint;

public class ServdorSOAP{
    public static void main(String[] args) {
        String url = "http://localhost:9090/ws/catalogo";
        Endpoint.publish(url, new CatalogoEndpoint());
        System.out.println("Servicio SOAP publicado en: " + url + "?wsdl");
    }
}
