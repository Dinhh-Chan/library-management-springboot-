package com.example.library_management.repository.elasticsearch;

import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.library_management.entity.Book;

@Repository
public interface BookElasticsearchRepository extends ElasticsearchRepository<Book, Long> {
    List<Book> findByTitle(String title);
    @Query("{\"bool\": {\"must\": [{\"nested\": {\"path\": \"authors\", \"query\": {\"match\": {\"authors.name\": \"?0\"}}}}]}}")
    List<Book> findByAuthorName(String authorName);  
    List<Book> findByTitleContaining(String keyword);
    @Query("{\"bool\": {\"must\": [{\"nested\": {\"path\": \"authors\", \"query\": {\"match\": {\"authors.name\": \"?0\"}}}}]}}")
    List<Book> findByAuthorNameContaining(String keyword);
}
