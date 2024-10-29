package org.example.t2_applicationtier_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class T2ApplicationTierSpringBootApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(T2ApplicationTierSpringBootApplication.class, args);
        int grpcPort = context.getEnvironment().getProperty("grpc.server.port", Integer.class, 9090);
        System.out.println("gRPC Server is running on port: " + grpcPort);
    }
}


