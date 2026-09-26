package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.Category;
import com.ims.inventorymanagement.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/name/{name}")
    public Optional<Category> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }
}