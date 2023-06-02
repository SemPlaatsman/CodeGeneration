package nl.inholland.codegeneration;

import nl.inholland.codegeneration.services.IBANGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
// @CrossOrigin("http://localhost:5173/")
public class CodeGenerationApplication {

	// TODO: isDeleted constraints voor output
	public static void main(String[] args) {
		SpringApplication.run(CodeGenerationApplication.class, args);
//		IBANGenerator ibanGenerator = new IBANGenerator();
//		List<String> stack = new ArrayList<>();
//		for (int i = 0; ; i++) {
//			String iban = ibanGenerator.generate(null, null).toString();
//			if (stack.contains(iban)) {
//				break;
//			} else {
//				stack.add(iban);
//			}
//		}
//		System.out.println(stack.size());
	}
}
