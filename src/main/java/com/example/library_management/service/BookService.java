package com.example.library_management.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library_management.entity.Book;
import com.example.library_management.entity.Category;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.elasticsearch.BookElasticsearchRepository;
import com.example.library_management.repository.jpa.BookRepository;
import com.example.library_management.repository.jpa.CategoryRepository;

@Service
public class BookService {
    
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookElasticsearchRepository bookElasticsearchRepository;

    @Autowired
    public BookService(BookRepository bookRepository,
                       CategoryRepository categoryRepository,
                       BookElasticsearchRepository bookElasticsearchRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.bookElasticsearchRepository = bookElasticsearchRepository;
    }

    // Lấy tất cả sách
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Lấy sách theo ID
    public Optional<Book> getBookById(Long id){
        return bookRepository.findById(id);
    }

    public Optional<Book> getFileById(Long id){
        return bookRepository.findById(id);
    }

    // Tạo sách mới
    @Transactional
    public Book saveBook(Book book, Set<Long> categoryIds){
        Set<Category> categories = categoryRepository.findAllById(categoryIds)
                                        .stream().collect(Collectors.toSet());
        book.setCategories(categories);
        Book savedBook = bookRepository.save(book);
        bookElasticsearchRepository.save(savedBook);
        return savedBook;
    }

    // Cập nhật sách
    @Transactional
    public Book updateBook(Long id, Book bookDetails, Set<Long> categoryIds){
        Book updatedBook = bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setQuantity(bookDetails.getQuantity());
            book.setFile(bookDetails.getFile());
            Set<Category> categories = categoryRepository.findAllById(categoryIds)
                                            .stream().collect(Collectors.toSet());
            book.setCategories(categories);
            book.setAuthors(bookDetails.getAuthors());
            // Cập nhật các thuộc tính khác nếu cần
            return bookRepository.save(book);
        }).orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        
        // Cập nhật trong Elasticsearch
        bookElasticsearchRepository.save(updatedBook);
        return updatedBook;
    }

    // Xóa sách
    public void deleteBook(Long id){
        bookRepository.deleteById(id);
        bookElasticsearchRepository.deleteById(id);
    }

    // Tìm sách theo tiêu đề
    public List<Book> findByTitle(String title) {
        return bookElasticsearchRepository.findByTitleContaining(title);
    }

    // Tìm sách theo tên tác giả
    public List<Book> findByAuthorName(String authorName) {
        return bookElasticsearchRepository.findByAuthorNameContaining(authorName);
    }

    // Tìm sách chứa từ khóa trong tiêu đề
    public List<Book> searchByTitleKeyword(String keyword) {
        return bookElasticsearchRepository.findByTitleContaining(keyword);
    }

    // Tìm sách chứa từ khóa trong tên tác giả
    public List<Book> searchByAuthorKeyword(String keyword) {
        return bookElasticsearchRepository.findByAuthorNameContaining(keyword);
    }
}
