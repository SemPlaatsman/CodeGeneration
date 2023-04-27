package nl.inholland.codegeneration;

import nl.inholland.codegeneration.services.IBANGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class CodeGenerationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeGenerationApplication.class, args);
	}
}
