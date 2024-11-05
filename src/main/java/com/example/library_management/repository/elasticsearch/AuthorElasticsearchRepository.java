package com.example.library_management.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.library_management.entity.Author;

@Repository
public interface AuthorElasticsearchRepository extends ElasticsearchRepository<Author, Long> {
    List<Author> findByName(String name);
    List<Author> findByNameContaining(String keyword);
}
