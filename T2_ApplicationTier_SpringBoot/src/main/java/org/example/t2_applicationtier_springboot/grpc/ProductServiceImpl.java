package org.example.t2_applicationtier_springboot.grpc;

import io.grpc.stub.StreamObserver;
import org.example.t2_applicationtier_springboot.model.Product;
import org.example.t2_applicationtier_springboot.rest.ProductRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceImpl extends org.example.t2_applicationtier_springboot.grpc.ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRestClient productRestClient;
    private final RestTemplate restTemplate;
    private static final String DATA_LAYER_URL = "http://localhost:5214/api/Products";

    @Autowired
    public ProductServiceImpl(RestTemplate restTemplate, ProductRestClient productRestClient) {
        this.restTemplate = restTemplate;
        this.productRestClient = productRestClient;
    }

    @Override
    public void addProduct(org.example.t2_applicationtier_springboot.grpc.ProductRequest request, StreamObserver<org.example.t2_applicationtier_springboot.grpc.ProductResponse> responseObserver) {


        Product product = new Product(request.getProductId(), request.getProductName(), request.getPrice());
        System.out.println("Product ID: " + product.getProductId());
        System.out.println("Product Name: " + product.getProductName());
        System.out.println("Price: " + product.getPrice());

        try {
            ResponseEntity<String> restResponse = restTemplate.postForEntity(DATA_LAYER_URL, product, String.class);

            org.example.t2_applicationtier_springboot.grpc.ProductResponse response = org.example.t2_applicationtier_springboot.grpc.ProductResponse.newBuilder()
                    .setSuccess(restResponse.getStatusCode().is2xxSuccessful())
                    .setMessage(restResponse.getBody() != null ? restResponse.getBody() : "Product added successfully")
                    .build();

            responseObserver.onNext(response);
        } catch (Exception e) {
            org.example.t2_applicationtier_springboot.grpc.ProductResponse errorResponse = org.example.t2_applicationtier_springboot.grpc.ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Failed to add product: " + e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getProducts(org.example.t2_applicationtier_springboot.grpc.Empty request, StreamObserver<org.example.t2_applicationtier_springboot.grpc.ProductListResponse> responseObserver) {
        try {
            // Assuming this retrieves a List<Product>
            var products = productRestClient.getProducts(); // Ensure this returns a list of Product objects
            org.example.t2_applicationtier_springboot.grpc.ProductListResponse.Builder responseBuilder = org.example.t2_applicationtier_springboot.grpc.ProductListResponse.newBuilder();

            // Iterate through each product
            for (Product product : products) {
                // Check if Product has these methods
                // Create the ProductResponse using the fields of the Product object
                org.example.t2_applicationtier_springboot.grpc.ProductResponse productResponse = org.example.t2_applicationtier_springboot.grpc.ProductResponse.newBuilder()
                        .setSuccess(true) // Indicate the operation was successful
                        .setMessage("Product retrieved successfully" + " ProductId= " + product.getProductId() + ", Product name= " + product.getProductName() + ", price= " + product.getPrice()) // A general message; adjust as needed
                        .build();

                // Add ProductResponse to the list
                responseBuilder.addProducts(productResponse);
            }

            responseObserver.onNext(responseBuilder.build());
        } catch (Exception e) {
            // Handle REST call failure
            org.example.t2_applicationtier_springboot.grpc.ProductListResponse errorResponse = org.example.t2_applicationtier_springboot.grpc.ProductListResponse.newBuilder()
                    .addProducts(org.example.t2_applicationtier_springboot.grpc.ProductResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Failed to retrieve products: " + e.getMessage())
                            .build())
                    .build();
            responseObserver.onNext(errorResponse);
        } finally {
            responseObserver.onCompleted();
        }
    }

}
