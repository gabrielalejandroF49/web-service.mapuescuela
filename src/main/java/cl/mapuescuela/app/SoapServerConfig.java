package cl.mapuescuela.app;

import cl.mapuescuela.endpoint.CatalogoEndpoint;
import cl.mapuescuela.service.CatalogoService;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import jakarta.annotation.PreDestroy;
import jakarta.xml.ws.Endpoint;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration
public class SoapServerConfig {
    private HttpServer httpServer;
    private Endpoint endpoint;

    public SoapServerConfig(CatalogoService catalogoService) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(9090), 0);
        var context = httpServer.createContext("/ws/catalogo");
        context.getFilters().add(new CorsFilter());

        endpoint = Endpoint.create(new CatalogoEndpoint(catalogoService));
        endpoint.publish(context);
        httpServer.start();
        System.out.println("SOAP catálogo: http://localhost:9090/ws/catalogo?wsdl");
    }

    @PreDestroy
    public void stop() {
        if (endpoint != null) endpoint.stop();
        if (httpServer != null) httpServer.stop(0);
    }

    static class CorsFilter extends Filter {
        @Override public String description() { return "CORS para frontend local"; }
        @Override public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
            var h = exchange.getResponseHeaders();
            h.set("Access-Control-Allow-Origin", "*");
            h.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            h.set("Access-Control-Allow-Headers", "Content-Type, SOAPAction");
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            }
            chain.doFilter(exchange);
        }
    }
}
