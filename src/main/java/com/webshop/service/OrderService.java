package com.webshop.service;

import com.webshop.dto.CartItemDTO;
import com.webshop.dto.CustomerDTO;
import com.webshop.dto.OrderDTO;
import com.webshop.model.CartItem;
import com.webshop.model.Customer;
import com.webshop.model.Order;
import com.webshop.model.Product;
import com.webshop.repository.CustomerRepository;
import com.webshop.repository.OrderRepository;
import com.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        for (CartItem item : order.getItems()) {
            item.setOrder(order);
        }
        Customer customer = customerRepository.findByEmail(order.getCustomer().getEmail());
        Order newOrder;
        if (customer != null) {
            order.setCustomer(null);
            newOrder = orderRepository.save(order);
            newOrder.setCustomer(customer);
            newOrder = orderRepository.save(newOrder);
        }
        else {
            newOrder = orderRepository.save(order);
        }

        emailService.sendOrderConfirmation(newOrder.getCustomer().getEmail());
        return newOrder;
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(orderDetails.getStatus());
        for (CartItem item : order.getItems()) {
            item.setOrder(order);
        }
        if ("Shipped".equalsIgnoreCase(orderDetails.getStatus())) {
            emailService.sendShippingNotification(order.getCustomer().getEmail());
        }
        Order savedOrder = orderRepository.save(order);
        for (CartItem cartItem: savedOrder.getItems()) {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        return savedOrder;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<OrderDTO> getOrdersByCustomerEmail(String email) {
        List<Order> orders = orderRepository.getOrdersByCustomerEmail(email);
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setDate(order.getDate());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setItems(order.getItems().stream().map(item -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(item.getId());
            cartItemDTO.setName(item.getProduct().getName());
            cartItemDTO.setPrice(item.getProduct().getPrice());
            cartItemDTO.setQuantity(item.getQuantity());
            return cartItemDTO;
        }).collect(Collectors.toList()));
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(order.getCustomer().getId());
        customerDTO.setFirstName(order.getCustomer().getFirstName());
        customerDTO.setLastName(order.getCustomer().getLastName());
        customerDTO.setEmail(order.getCustomer().getEmail());
        customerDTO.setPhone(order.getCustomer().getPhone());
        customerDTO.setAddress1(order.getCustomer().getAddress1());
        customerDTO.setAddress2(order.getCustomer().getAddress2());
        customerDTO.setCity(order.getCustomer().getCity());
        customerDTO.setPostalCode(order.getCustomer().getPostalCode());
        orderDTO.setCustomer(customerDTO);
        return orderDTO;
    }
}
