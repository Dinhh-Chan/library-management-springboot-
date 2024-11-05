package com.example.library_management.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.library_management.entity.Reader;

@Repository
public interface ReaderElasticsearchRepository extends ElasticsearchRepository<Reader, Long> {
    List<Reader> findByUsername(String username);
    List<Reader> findByUsernameContaining(String keyword);
}
