package org.cloud.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan

public class CloudDriverMainApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {

		SpringApplication.run(CloudDriverMainApplication.class, args);
	}

}
