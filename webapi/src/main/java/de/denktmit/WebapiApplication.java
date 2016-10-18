package de.denktmit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class WebapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebapiApplication.class, args);
    }
}
