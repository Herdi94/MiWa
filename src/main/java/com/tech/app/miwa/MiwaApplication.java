package com.tech.app.miwa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "MiWa API",
				version = "1.0.0",
				description = "Documentation REST API MiWa App"
		)
)
@SpringBootApplication
public class MiwaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiwaApplication.class, args);
	}

}
