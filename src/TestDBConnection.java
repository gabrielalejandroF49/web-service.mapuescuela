import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TestDBConnection {

    @Bean
    CommandLineRunner testRepo(ProductoRepository productoRepository) {
        return args -> {
            Producto p = new Producto();
            p.setNombre("Test Producto");
            p.setDescripcion("Producto de prueba");
            p.setCategoria("Test");
            p.setPrecio(1000);
            p.setStock(10);
            p.setEstado("ACTIVO");

            productoRepository.save(p);

            System.out.println("Producto guardado con ID: " + p.getId());
        };
    }
}
