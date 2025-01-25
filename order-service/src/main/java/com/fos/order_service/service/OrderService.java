package com.fos.order_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fos.order_service.dto.InventoryResponse;
import com.fos.order_service.model.Order;
import com.fos.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }


    public void placeOrder(List<Order> orders){

        //check orders if in stock or not if in save if not save
        WebClient webClient = webClientBuilder.build();

        for (Order order: orders){

            InventoryResponse response = webClient.get()
                        .uri("http://INVENTORY-SERVICE/api/inventory/{productName}", order.getName())
                        .retrieve()
                        .bodyToMono(InventoryResponse.class)
                        .block();

            if (response != null && response.getQuantity() > order.getQuantity()){
                orderRepository.save(order);

                webClient.put()
                        .uri("http://INVENTORY-SERVICE/api/inventory/{productName}/decrease?quantity={quantity}",
                        order.getName(), order.getQuantity())
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                        
                log.info("Order placed successfully for product: {}", order.getName());
            }
            log.warn("Product '{}' is out of stock or insufficient quantity!", order.getName());
        }
    }
}
