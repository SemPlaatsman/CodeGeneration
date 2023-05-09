package nl.inholland.codegeneration;

import nl.inholland.codegeneration.services.IBANGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;



@SpringBootApplication
@CrossOrigin("http://localhost:5173/")
public class CodeGenerationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeGenerationApplication.class, args);
	}
}
