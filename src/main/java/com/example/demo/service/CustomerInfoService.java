package com.example.demo.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CustomerInfoService {
    public CustomerInfoResponse getCustomerInfo(CustomerInfoRequest request) {
        return new CustomerInfoResponse("NeoCorp", "123 Main St, Springfield", "Jennifer Smith");
    }

    @Data
    @AllArgsConstructor
    public static class CustomerInfoResponse {
        public String name;
        public String address;
        public String contactPerson;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomerInfoRequest {
        public String name;
    }
} 