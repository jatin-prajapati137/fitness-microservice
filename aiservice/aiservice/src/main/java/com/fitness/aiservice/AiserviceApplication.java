package com.fitness.aiservice;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiserviceApplication.class, args);
	}

	@SpringBootApplication
	public class AiRecommendationServiceApplication {

		@Value("${spring.data.mongodb.uri}")
		private String mongoUri;

		@PostConstruct
		public void printMongo() {
			System.out.println("Mongo URI = " + mongoUri);
		}

		public static void main(String[] args) {
			SpringApplication.run(AiRecommendationServiceApplication.class, args);
		}
	}

}
