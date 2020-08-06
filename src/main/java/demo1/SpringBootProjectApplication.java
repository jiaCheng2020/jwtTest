package demo1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("demo1.dao")
@SpringBootApplication
public class SpringBootProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProjectApplication.class, args);
    }

}
