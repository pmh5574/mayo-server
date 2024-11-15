package com.mayo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;

@EnableJpaAuditing
@SpringBootApplication
public class MayoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MayoApplication.class, args);
    }

    @GetMapping("/health-check")
    public String health() {

        return "OK";
    }

}
