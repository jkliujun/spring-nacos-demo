package com.example.orderservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /*@Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url:http://user-service}")
    private String userServiceUrl;*/

    @Autowired
    private GrpcUserClient grpcUserClient;

/*
    @GetMapping("/{id}")
    public String getOrderById(@PathVariable String id) {
        String userInfo = restTemplate.getForObject(userServiceUrl + "/api/users/" + id, String.class);
        return "Order ID: " + id + ", User Info: [" + userInfo + "]";
    }*/
    @GetMapping("/{id}")
    public String getOrderById(@PathVariable String id) {
        String userInfo = grpcUserClient.getUser(id); 
        return "Order ID: " + id + ", User Info: [" + userInfo + "]";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Order Service";
    }
}
