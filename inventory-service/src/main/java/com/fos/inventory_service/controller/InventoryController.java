package com.fos.inventory_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fos.inventory_service.dto.RequestResponse;
import com.fos.inventory_service.model.Product;
import com.fos.inventory_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<RequestResponse> createProduct(@RequestBody Product product){
        if (product != null){
            return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/products")
    public ResponseEntity<List<RequestResponse>> getProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.FOUND);
    }

    @GetMapping("/{productName}")
    public ResponseEntity<RequestResponse> getProduct(@PathVariable("productName") String productName) {
        RequestResponse response = productService.getProduct(productName);

        if (response == null || response.getQuantity() <= 0) {
            // Product not found or no quantity available
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // HTTP 404
        }

        // Product found and available
        return new ResponseEntity<>(response, HttpStatus.OK); // HTTP 200
    }

    @PutMapping("/{productName}/decrease")
    public ResponseEntity<Void> decreasePoductQuantity(
                    @PathVariable("productName") String productName,
                    @RequestParam("quantity") int quantity){
    productService.decreaseQuantity(productName, quantity);
    return ResponseEntity.ok().build();
    }

}
