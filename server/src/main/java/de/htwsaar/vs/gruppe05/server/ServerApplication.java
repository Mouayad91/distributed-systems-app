package de.htwsaar.vs.gruppe05.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.htwsaar.vs.gruppe05.server.helper.DatabaseHelper;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Calendar API", version = "1.0", description = "API to manage a basic Calendar-Application"))
@SecurityScheme(name = "Bearer Authentication", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class ServerApplication implements CommandLineRunner{

	@Autowired
	DatabaseHelper db;


	public static void main(String[] args) {


		SpringApplication.run(ServerApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		db.init();
	}
}

