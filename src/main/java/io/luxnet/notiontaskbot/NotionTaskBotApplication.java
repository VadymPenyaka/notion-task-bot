package io.luxnet.notiontaskbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotionTaskBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotionTaskBotApplication.class, args);
    }

}
