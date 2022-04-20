package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@EnableSwagger2
@SpringBootApplication
public class TehnickiZadatakApplication {

	public static void main(String[] args) {
		SpringApplication.run(TehnickiZadatakApplication.class, args);
	}

}
