package endpoint;

import cl.mapuescuela.model.Comprobante;
import cl.mapuescuela.model.EstadoValidacion;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ComprobanteEndpoint {

    private List<Comprobante> comprobantes = new ArrayList<>();
    private Long comprobanteCounter = 1L;

    @WebMethod
    public Comprobante adjuntarComprobante(Long pedidoId, String archivo) {
        Comprobante comprobante = new Comprobante(comprobanteCounter++, pedidoId, archivo);
        comprobantes.add(comprobante);
        return comprobante;
    }

    @WebMethod
    public String validarComprobante(Long comprobanteId, boolean aprobado) {
        for (Comprobante c : comprobantes) {
            if (c.getId().equals(comprobanteId)) {
                c.setEstadoValidacion(aprobado ? EstadoValidacion.APROBADO : EstadoValidacion.RECHAZADO);
                return "Comprobante " + comprobanteId + " validado como: " + c.getEstadoValidacion();
            }
        }
        return "Comprobante no encontrado";
    }
}
