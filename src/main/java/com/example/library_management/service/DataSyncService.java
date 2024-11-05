package com.example.library_management.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library_management.entity.Author;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.Borrowing;
import com.example.library_management.entity.Category;
import com.example.library_management.entity.Inventory;
import com.example.library_management.entity.Reader;
import com.example.library_management.repository.elasticsearch.AuthorElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.BookElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.BorrowingElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.CategoryElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.InventoryElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.ReaderElasticsearchRepository;
import com.example.library_management.repository.jpa.AuthorRepository;
import com.example.library_management.repository.jpa.BookRepository;
import com.example.library_management.repository.jpa.BorrowingRepository;
import com.example.library_management.repository.jpa.CategoryRepository;
import com.example.library_management.repository.jpa.InventoryRepository;
import com.example.library_management.repository.jpa.ReaderRepository;
import jakarta.transaction.Transactional;

import jakarta.annotation.PostConstruct;

@Service
public class DataSyncService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookElasticsearchRepository bookElasticsearchRepository;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorElasticsearchRepository authorElasticsearchRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryElasticsearchRepository categoryElasticsearchRepository;

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private InventoryElasticsearchRepository inventoryElasticsearchRepository;

    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private ReaderElasticsearchRepository readerElasticsearchRepository;

    @Autowired
    private BorrowingRepository borrowingRepository;
    @Autowired
    private BorrowingElasticsearchRepository borrowingElasticsearchRepository;

    @PostConstruct
    public void syncData() {
        syncBooks();
        syncAuthors();
        syncCategories();
        syncInventory();
        syncReaders();
        syncBorrowings();
    }

    @Transactional
    private void syncBooks() {
        List<Book> books = bookRepository.findAll();
        books.forEach(book -> Hibernate.initialize(book.getAuthors()));  // Khởi tạo quan hệ authors
        bookElasticsearchRepository.saveAll(books);
    }

    @Transactional
    private void syncAuthors() {
        List<Author> authors = authorRepository.findAll();
        authorElasticsearchRepository.saveAll(authors);
    }

    @Transactional
    private void syncCategories() {
        List<Category> categories = categoryRepository.findAll();
        categoryElasticsearchRepository.saveAll(categories);
    }

    @Transactional
    private void syncInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        inventoryElasticsearchRepository.saveAll(inventories);
    }

    @Transactional
    private void syncReaders() {
        List<Reader> readers = readerRepository.findAll();
        readers.forEach(reader -> Hibernate.initialize(reader.getBorrowings()));  // Khởi tạo quan hệ borrowings
        readerElasticsearchRepository.saveAll(readers);
    }

    @Transactional
    private void syncBorrowings() {
        List<Borrowing> borrowings = borrowingRepository.findAll();
        borrowingElasticsearchRepository.saveAll(borrowings);
    }
}
