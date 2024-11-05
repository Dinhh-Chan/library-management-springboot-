package com.example.library_management.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.library_management.entity.Category;

@Repository
public interface CategoryElasticsearchRepository extends ElasticsearchRepository<Category, Long> {
    List<Category> findByCategoryName(String categoryName);
    List<Category> findByCategoryNameContaining(String keyword);
}
