package hello.hello.yju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YjuApplication {

     public static void main(String[] args) {
        SpringApplication.run(YjuApplication.class, args);
    }

}
