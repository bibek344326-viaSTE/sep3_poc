package org.example.applicationtier_springboot.applicationTier;

import com.example.applicationtier.Product;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ProductServiceGrpcImpl extends com.example.applicationtier.ProductServiceGrpc.ProductServiceImplBase {
    private RestTemplate restTemplate;
    
    

    @Override
    public void addProduct(Product.ProductRequest request, StreamObserver<Product.ProductResponse> responseObserver) {
        // Logic to save product in the database
        // For now, we're just echoing the product back in the response
        Product.ProductResponse response = Product.ProductResponse.newBuilder()
                .setProductId(request.getProductId())
                .setProductName(request.getProductName())
                .setPrice(request.getPrice())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println(response.toString());
    }

    @Override
    public void getProductInfo(Product.ProductRequest request, StreamObserver<Product.ProductResponse> responseObserver) {
        String productId = request.getProductId();
        String restUrl = "http://localhost:5000/api/products/" + productId;

        ResponseEntity<Product> responseEntity = restTemplate.getForEntity(restUrl, Product.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Product product = responseEntity.getBody();
            Product.ProductResponse response = Product.ProductResponse.newBuilder()
                    .setProductId(product.toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Product not found"));
        }
    }
}
