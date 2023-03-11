package com.arthurbarbosa.msclientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsclientesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsclientesApplication.class, args);
    }

}
