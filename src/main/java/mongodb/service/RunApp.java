package mongodb.service;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
//@EnableHystrix
public class RunApp {
	
	public static void main( String args [] ) throws Exception {
	        SpringApplication.run(RunApp.class, args);
	}
	
	@PostConstruct
	public static void startMongoTimer() throws ServletException, IOException {
		
		//Create a Mongo Timer object, which implements all of the runnable logic
		//MongoTimer mt = new MongoTimer();
		
		//kick off the timed mongo query events
		//mt.runMongoQuery();
	}
	

	
}
