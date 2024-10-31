package org.example.t2_applicationtier_springboot;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.t2_applicationtier_springboot.grpc.ProductServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
public class T2ApplicationTierSpringBootApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(T2ApplicationTierSpringBootApplication.class, args);

        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class);

        Server server = ServerBuilder.forPort(9090)
                .addService(productService)
                .build()
                .start();
        int grpcPort = context.getEnvironment().getProperty("grpc.server.port", Integer.class, 9090);
        System.out.println("gRPC Server is running on port: " + grpcPort);

        server.awaitTermination();
    }



}


