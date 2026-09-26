package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.Product;
import com.ims.inventorymanagement.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @GetMapping("/sku/{sku}")
    public Optional<Product> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku);
    }

}