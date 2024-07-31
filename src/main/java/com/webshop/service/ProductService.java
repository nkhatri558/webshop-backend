package com.webshop.service;

import com.webshop.model.Product;
import com.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product createProduct(String name, String description, String category, double price, int stock, MultipartFile image) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);
        product.setImage(image.getBytes());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, String name, String description, String category, double price, int stock, MultipartFile image) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);
        if (image != null && !image.isEmpty()) {
            product.setImage(image.getBytes());
        }
        return productRepository.save(product);
    }

    public Product updateStock(Long id, int stock) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(stock);
        return productRepository.save(product);
    }

}
