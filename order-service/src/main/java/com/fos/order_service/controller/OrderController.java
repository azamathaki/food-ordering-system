package com.fos.order_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fos.order_service.model.Order;
import com.fos.order_service.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.FOUND);
    }

    @PostMapping("/place")
    public ResponseEntity<String> postOrder(@RequestBody List<Order> orders){
        orderService.placeOrder(orders);
        return new ResponseEntity<>("Your orders are placed successfully!", HttpStatus.ACCEPTED);
    }

}
