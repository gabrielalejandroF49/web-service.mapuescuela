package cl.mapuescuela.app.controller; // asegúrate que el archivo esté en la carpeta correcta

import org.flowable.engine.RuntimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProcesoController {

    private final RuntimeService runtimeService;

    public ProcesoController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @PostMapping("/iniciar-proceso")
    public String iniciarProceso() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("pedidoId", 1);
        variables.put("clienteCorreo", "cliente@ejemplo.com");
        variables.put("modalidadEntrega", "RETIRO");
        variables.put("pagoAprobado", false);

        runtimeService.startProcessInstanceByKey("proceso_Venta_TOBE", variables);

        System.out.println("Instancia del proceso creada con variables: " + variables);

        return "Proceso de venta iniciado correctamente.";
    }

}
