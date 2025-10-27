package pe.edu.upc.groups_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class GroupsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupsServiceApplication.class, args);
	}

}
