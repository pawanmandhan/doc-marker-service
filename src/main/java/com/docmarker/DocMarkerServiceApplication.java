package com.docmarker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

//@ComponentScan(basePackages = "com.docmarker.")
@SpringBootApplication(scanBasePackages = "com.docmarker.repository", exclude = JmxAutoConfiguration.class)
public class DocMarkerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocMarkerServiceApplication.class, args);
	}
}
