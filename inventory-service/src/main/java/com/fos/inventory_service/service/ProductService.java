package com.fos.inventory_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fos.inventory_service.dto.RequestResponse;
import com.fos.inventory_service.model.Product;
import com.fos.inventory_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public RequestResponse createProduct(Product product){
        productRepository.save(product);

        return RequestResponse.builder()
            .productName(product.getName())
            .quantity(product.getQuantity())
            .build();
    }

    public RequestResponse getProduct(String productName){
        Optional<Product> product = productRepository.findByName(productName);

        if (product.isEmpty() || product.get().getQuantity() <= 0) {
            // Log detailed info when product is not found or has zero quantity
            log.warn("Product not found with name '{}' or insufficient quantity.", productName);
            return null;  // No response if product is not found or quantity is 0
        }

        // Return the product details with quantity available
        return RequestResponse.builder()
                .productName(product.get().getName())
                .quantity(product.get().getQuantity())
                .build();
    
    }

    public List<RequestResponse> getAllProducts(){
        return productRepository.findAll().stream().map(product -> 
            RequestResponse.builder()
                .productName(product.getName())
                .quantity(product.getQuantity())
                .build()
        ).toList();
    }

    public void decreaseQuantity(String productName, int quantity) {
        Optional<Product> productOptional = productRepository.findByName(productName);

        if (productOptional.isPresent()){
            Product product = productOptional.get();
            if (product.getQuantity() >= quantity){
                product.setQuantity(product.getQuantity() - quantity);
                productRepository.save(product);
            }else {
                log.warn("Insufficient stock for product: {} ", productName);
            }
        }else {
            log.warn("Product not found: {} ", productName);
        }
    }    
}
