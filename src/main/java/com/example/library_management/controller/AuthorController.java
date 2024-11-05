package com.example.library_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library_management.dto.AuthorRequest;
import com.example.library_management.entity.Author;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.service.AuthorService;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    
    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    // Lấy tất cả tác giả
    @GetMapping
    public List<Author> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    // Lấy tác giả theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id){
        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo tác giả mới
    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorRequest authorRequest){
        Author author = authorRequest.toAuthor();
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(201).body(createdAuthor);
    }

    // Cập nhật tác giả
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody AuthorRequest authorRequest){
        try {
            Author authorDetails = authorRequest.toAuthor();
            Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
            return ResponseEntity.ok(updatedAuthor);
        } catch (ResourceNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa tác giả
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        try {
            authorService.deleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }
        /**
     * Tìm kiếm tác giả theo tên chính xác
     * GET /api/authors/search?name=John
     */
    @GetMapping("/search")
    public ResponseEntity<List<Author>> searchAuthorsByName(@RequestParam String name){
        List<Author> authors = authorService.findByName(name);
        return ResponseEntity.ok(authors);
    }
        /**
     * Tìm kiếm tác giả chứa từ khóa trong tên
     * GET /api/authors/search?keyword=John
     */
    @GetMapping("/search/keyword")
    public ResponseEntity<List<Author>> searchAuthorsByNameContaining(@RequestParam String keyword){
        List<Author> authors = authorService.findByNameContaining(keyword);
        return ResponseEntity.ok(authors);
    }
}
