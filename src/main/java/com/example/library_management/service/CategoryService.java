package com.example.library_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library_management.entity.Category;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.elasticsearch.CategoryElasticsearchRepository;
import com.example.library_management.repository.jpa.CategoryRepository;

@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final CategoryElasticsearchRepository categoryElasticsearchRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,
                           CategoryElasticsearchRepository categoryElasticsearchRepository){
        this.categoryRepository = categoryRepository;
        this.categoryElasticsearchRepository = categoryElasticsearchRepository;
    }

    // Lấy tất cả danh mục
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    // Lấy danh mục theo ID
    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    // Tạo danh mục mới
    public Category createCategory(Category category){
        Category savedCategory = categoryRepository.save(category);
        categoryElasticsearchRepository.save(savedCategory);
        return savedCategory;
    }

    // Cập nhật danh mục
    public Category updateCategory(Long id, Category categoryDetails){
        Category updatedCategory = categoryRepository.findById(id).map(category -> {
            category.setCategoryName(categoryDetails.getCategoryName());
            // Cập nhật các thuộc tính khác nếu cần
            return categoryRepository.save(category);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        
        // Cập nhật trong Elasticsearch
        categoryElasticsearchRepository.save(updatedCategory);
        return updatedCategory;
    }

    // Xóa danh mục
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
        categoryElasticsearchRepository.deleteById(id);
    }

    // Tìm danh mục theo tên
    public List<Category> findByCategoryName(String categoryName) {
        return categoryElasticsearchRepository.findByCategoryName(categoryName);
    }

    // Tìm danh mục chứa từ khóa trong tên
    public List<Category> findByCategoryNameContaining(String keyword) {
        return categoryElasticsearchRepository.findByCategoryNameContaining(keyword);
    }
}
