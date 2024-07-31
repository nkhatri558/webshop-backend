package com.webshop.service;

import com.webshop.model.Product;
import com.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 86400000) // Check every 24 hours
    public void checkStockLevels() {
        Iterable<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (product.getStock() < 5) {
                emailService.sendEmail("admin@example.com", "Low Stock Alert", "Product " + product.getName() + " is low on stock.");
            }
        }
    }
}
