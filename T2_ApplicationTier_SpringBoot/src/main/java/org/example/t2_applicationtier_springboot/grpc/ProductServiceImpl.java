package org.example.t2_applicationtier_springboot.grpc;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.example.t2_applicationtier_springboot.model.Product;
import org.example.t2_applicationtier_springboot.rest.ProductRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
     private final ProductRestClient productRestClient;
    @Autowired
    private final RestTemplate restTemplate;
    private final ManagedChannel channel;
    private final ProductServiceGrpc.ProductServiceBlockingStub blockingStub;
    private static final String DATA_LAYER_URL = "http://localhost:9090/api/products";

@Autowired
    public ProductServiceImpl(RestTemplate restTemplate, ProductRestClient productRestClient, ManagedChannel grpcChannel) {
        this.restTemplate = restTemplate;
        this.productRestClient = productRestClient;
        this.channel = grpcChannel;
        this.blockingStub = ProductServiceGrpc.newBlockingStub(grpcChannel);
    }




   @Override
    public void addProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        // Create a Product model from the request
        Product product = new Product(request.getProductId(), request.getProductName(), request.getPrice());

        // Forward the request to the Data Layer using REST API
        try {
            ResponseEntity<String> restResponse = restTemplate.postForEntity(DATA_LAYER_URL, product, String.class);

            // Build gRPC response based on REST API response
            ProductResponse response = ProductResponse.newBuilder()
                    .setSuccess(restResponse.getStatusCode().is2xxSuccessful())
                    .setMessage(restResponse.getBody() != null ? restResponse.getBody() : "Product added successfully")
                    .build();

            responseObserver.onNext(response);
        } catch (Exception e) {
            // Handle REST call failure
            ProductResponse errorResponse = ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Failed to add product: " + e.getMessage())
                    .build();
            responseObserver.onNext(errorResponse);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getProducts(Empty request, StreamObserver<ProductListResponse> responseObserver) {
        try {
            var products = productRestClient.getProducts();
            ProductListResponse.Builder responseBuilder = ProductListResponse.newBuilder();

            products.forEach(p -> responseBuilder.addProducts(
                    ProductResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage(p.getMessage())
                            .build()
            ));

            responseObserver.onNext(responseBuilder.build());
        } catch (Exception e) {
            // Handle REST call failure
            ProductListResponse errorResponse = ProductListResponse.newBuilder()
                    .addProducts(ProductResponse.newBuilder()
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
