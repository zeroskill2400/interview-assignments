package org.zeroskill.postapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        basePackages = {"org.zeroskill.common", "org.zeroskill.postapi"}
)
@EnableFeignClients(basePackages = "org.zeroskill.common.client")
public class PostApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostApiApplication.class, args);
    }

}
