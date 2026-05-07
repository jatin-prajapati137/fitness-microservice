package com.fitness.activityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;


@SpringBootApplication
public class ActivityserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivityserviceApplication.class, args);
	}

//	@SpringBootApplication
//	public class AiRecommendationServiceApplication {
//
//		@Value("${spring.data.mongodb.uri}")
//		private String mongoUri;
//
//		@PostConstruct
//		public void printMongo() {
//			System.out.println("Mongo URI = " + mongoUri);
//		}
//
//		public static void main(String[] args) {
//			SpringApplication.run(AiRecommendationServiceApplication.class, args);
//		}
//	}


}
