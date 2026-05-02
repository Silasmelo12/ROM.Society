package concept.com.example.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients

public class ClubRomConceptApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClubRomConceptApplication.class, args);
	}
}
