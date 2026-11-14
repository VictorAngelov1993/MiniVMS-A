package springbootapp.minivms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MiniVmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniVmsApplication.class, args);
    }

}
