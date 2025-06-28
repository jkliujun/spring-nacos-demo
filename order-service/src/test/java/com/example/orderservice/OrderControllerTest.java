package com.example.orderservice;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GrpcUserClient grpcUserClient;
   
    @Test
    void helloEndpointReturnsExpectedMessage() throws Exception {
        mockMvc.perform(get("/api/orders/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from Order Service"));
    }
}