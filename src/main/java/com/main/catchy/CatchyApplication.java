package com.main.catchy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.main.catchy.services.StorageProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableFeignClients
public class CatchyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatchyApplication.class, args);
	}
	
}
