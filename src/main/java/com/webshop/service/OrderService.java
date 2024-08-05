package com.webshop.service;

import com.webshop.model.CartItem;
import com.webshop.model.Order;
import com.webshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        for (CartItem item : order.getItems()) {
            item.setOrder(order);
        }
        Order newOrder = orderRepository.save(order);
        emailService.sendOrderConfirmation(order.getCustomer().getEmail());
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
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
