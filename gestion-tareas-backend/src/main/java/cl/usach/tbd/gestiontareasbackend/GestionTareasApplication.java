package cl.usach.tbd.gestiontareasbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestionTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionTareasApplication.class, args);
	}

}
