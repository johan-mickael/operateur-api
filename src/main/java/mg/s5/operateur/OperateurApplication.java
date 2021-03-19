package mg.s5.operateur;

import mg.s5.service.OffreRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = OffreRepository.class)
public class OperateurApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperateurApplication.class, args);
	}

}
