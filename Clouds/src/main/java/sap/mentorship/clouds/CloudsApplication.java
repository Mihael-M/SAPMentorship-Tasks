package sap.mentorship.clouds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CloudsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudsApplication.class, args);
    }

}
