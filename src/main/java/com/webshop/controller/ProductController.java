package com.webshop.controller;

import com.webshop.model.Product;
import com.webshop.service.ProductService;
import org.codehaus.plexus.resource.loader.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestParam("name") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam("category") String category,
                                 @RequestParam("price") double price,
                                 @RequestParam("stock") int stock,
                                 @RequestParam("image") MultipartFile image) throws IOException {
        return productService.createProduct(name, description, category, price, stock, image);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestParam("name") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam("category") String category,
                                 @RequestParam("price") double price,
                                 @RequestParam("stock") int stock,
                                 @RequestParam("image") MultipartFile image) throws IOException, ResourceNotFoundException {
        return productService.updateProduct(id, name, description, category, price, stock, image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
