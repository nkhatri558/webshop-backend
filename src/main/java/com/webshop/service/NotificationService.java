package com.webshop.service;

import com.webshop.model.Product;
import com.webshop.repository.ProductRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 3600000) // Check every 1 hour
    public void checkStockLevels() {
        Iterable<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (product.getStock() < 5) {
                emailService.sendEmail("nkhatri558@gmail.com", "Low Stock Alert", "Product " + product.getName() + " is low on stock.");
            }
        }
    }
}
