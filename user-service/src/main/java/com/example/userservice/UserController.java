package com.example.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceGrpcImpl userService;

    @Value("${user.greeting:Hello from User Service}")
    private String greeting;


    @GetMapping("/hello")
    public String hello() {
        return greeting;
    }

    /*@GetMapping("/{id}")
    public String getUserById(@PathVariable String id) {
        return "User ID: " + id;
    }*/

    @GetMapping("/{id}")
    public String getUser(@PathVariable String id) {
        return "User ID: " + id;
    }


}
