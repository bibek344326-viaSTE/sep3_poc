package org.example.applicationtier_springboot;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.applicationtier_springboot.applicationTier.ProductServiceGrpcImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ApplicationTierSpringBootApplication {

    public static void main(String[] args) throws InterruptedException, IOException {
        SpringApplication.run(ApplicationTierSpringBootApplication.class, args);

        Server server = ServerBuilder.forPort(8090) // Adjust the port as needed
                .addService(new ProductServiceGrpcImpl())
                .build()
                .start();

        System.out.println("Server started on port: " + server.getPort());
        server.awaitTermination();
        
    }

}
