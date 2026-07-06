package gr.aueb.cf.eshop_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EShopAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopAppApplication.class, args);
	}

}
