package cl.mapuescuela.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"cl.mapuescuela"})
@EnableJpaRepositories(basePackages = "cl.mapuescuela.repository")
@EntityScan(basePackages = "cl.mapuescuela.model")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
