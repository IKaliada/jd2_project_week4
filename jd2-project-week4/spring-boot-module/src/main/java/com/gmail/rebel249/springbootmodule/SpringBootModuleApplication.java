package com.gmail.rebel249.springbootmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {
        "com.gmail.rebel249.repositorymodule",
        "com.gmail.rebel249.servicemodule",
        "com.gmail.rebel249.springbootmodule"},
        exclude = UserDetailsServiceAutoConfiguration.class
)
public class SpringBootModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }

}
