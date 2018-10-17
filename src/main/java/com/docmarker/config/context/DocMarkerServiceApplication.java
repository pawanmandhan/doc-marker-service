package com.docmarker.config.context;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

@SpringBootApplication(exclude = JmxAutoConfiguration.class)
public class DocMarkerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocMarkerServiceApplication.class, args);
	}
}
