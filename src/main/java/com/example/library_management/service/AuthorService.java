package com.example.library_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library_management.entity.Author;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.elasticsearch.AuthorElasticsearchRepository;
import com.example.library_management.repository.jpa.AuthorRepository;

@Service
public class AuthorService {
    
    private final AuthorRepository authorRepository;
    private final AuthorElasticsearchRepository authorElasticsearchRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository,
                         AuthorElasticsearchRepository authorElasticsearchRepository){
        this.authorRepository = authorRepository;
        this.authorElasticsearchRepository = authorElasticsearchRepository;
    }

    // Lấy tất cả tác giả
    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    // Lấy tác giả theo ID
    public Optional<Author> getAuthorById(Long id){
        return authorRepository.findById(id);
    }

    // Tạo tác giả mới
    public Author createAuthor(Author author){
        Author savedAuthor = authorRepository.save(author);
        authorElasticsearchRepository.save(savedAuthor);
        return savedAuthor;
    }

    // Cập nhật tác giả
    public Author updateAuthor(Long id, Author authorDetails){
        Author updatedAuthor = authorRepository.findById(id).map(author -> {
            author.setName(authorDetails.getName());
            author.setBio(authorDetails.getBio());
            // Cập nhật các thuộc tính khác nếu cần
            return authorRepository.save(author);
        }).orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        
        // Cập nhật trong Elasticsearch
        authorElasticsearchRepository.save(updatedAuthor);
        return updatedAuthor;
    }

    // Xóa tác giả
    public void deleteAuthor(Long id){
        authorRepository.deleteById(id);
        authorElasticsearchRepository.deleteById(id);
    }

    // Tìm tác giả theo tên
    public List<Author> findByName(String name) {
        return authorElasticsearchRepository.findByNameContaining(name);
    }

    // Tìm tác giả chứa từ khóa trong tên
    public List<Author> searchByNameKeyword(String keyword) {
        return authorElasticsearchRepository.findByNameContaining(keyword);
    }
}
