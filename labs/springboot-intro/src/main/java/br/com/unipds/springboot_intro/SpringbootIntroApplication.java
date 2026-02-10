package br.com.unipds.springboot_intro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringbootIntroApplication {

	public static void main(String[] args) {
		// Sobe o servidor Tomcat embutido na porta 8080
		SpringApplication.run(SpringbootIntroApplication.class, args);
	}
}
