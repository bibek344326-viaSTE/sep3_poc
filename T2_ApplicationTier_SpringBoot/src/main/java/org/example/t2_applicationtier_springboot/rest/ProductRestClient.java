package org.example.t2_applicationtier_springboot.rest;

import org.example.t2_applicationtier_springboot.grpc.ProductResponse;
import org.example.t2_applicationtier_springboot.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "productAPI", url = "http://localhost:5214/api/Products")
public interface ProductRestClient {
    @PostMapping
    boolean addProduct(@RequestParam String productId, @RequestParam String productName, @RequestParam double price);

    @GetMapping
    List<Product> getProducts();
}
