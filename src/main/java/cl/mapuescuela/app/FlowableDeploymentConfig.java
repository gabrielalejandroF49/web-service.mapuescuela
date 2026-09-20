package cl.mapuescuela.app;

import org.flowable.engine.RepositoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowableDeploymentConfig {

    @Bean
    CommandLineRunner init(RepositoryService repositoryService) {
        return args -> {
            repositoryService.createDeployment()
                    .addClasspathResource("processes/proceso_Venta_TOBE.bpmn20.xml")
                    .deploy();
        };
    }
}
