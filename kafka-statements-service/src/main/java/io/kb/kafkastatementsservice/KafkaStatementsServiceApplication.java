package io.kb.kafkastatementsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KafkaStatementsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStatementsServiceApplication.class, args);
	}

}
