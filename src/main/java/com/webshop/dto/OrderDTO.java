package com.webshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String date;
    private String status;
    private List<CartItemDTO> items;
    private CustomerDTO customer;
    // Getters and Setters
}
