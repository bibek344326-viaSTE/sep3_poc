package org.example.t2_applicationtier_springboot.grpc;

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
    private ProductRestClient productRestClient;
    private RestTemplate   restTemplate;
    private final String DATA_LAYER_URL = "http://localhost:9090/api/products";

    @Override
    public void addProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {

        // Create a Product model from the request
        Product product = new Product(request.getProductId(), request.getProductName(), request.getPrice());

        // Forward the request to the Data Layer using REST API
        ResponseEntity<String> response1 = restTemplate.postForEntity(DATA_LAYER_URL, product, String.class);

        boolean success = productRestClient.addProduct(request.getProductId(), request.getProductName(), request.getPrice());
        ProductResponse response = ProductResponse.newBuilder()
                .setSuccess(success)
                .setMessage(success ? "Product added successfully" : "Failed to add product")
                .build();

        // Prepare the gRPC response
        ProductResponse productResponse1 = ProductResponse.newBuilder()
            .setMessage(response1.getBody())
            .build();

        responseObserver.onNext(response);
        responseObserver.onNext(productResponse1);
        responseObserver.onCompleted();
    }

    @Override
    public void getProducts(Empty request, StreamObserver<ProductListResponse> responseObserver) {
        var products = productRestClient.getProducts();
        ProductListResponse.Builder responseBuilder = ProductListResponse.newBuilder();
        products.forEach(p -> responseBuilder.addProducts(
                ProductResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage(p.getMessage())
                        .build()
        ));
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
