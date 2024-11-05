package com.example.library_management.service;

import java.util.List;

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

    private void syncBooks() {
        List<Book> books = bookRepository.findAll();
        bookElasticsearchRepository.saveAll(books);
    }

    private void syncAuthors() {
        List<Author> authors = authorRepository.findAll();
        authorElasticsearchRepository.saveAll(authors);
    }

    private void syncCategories() {
        List<Category> categories = categoryRepository.findAll();
        categoryElasticsearchRepository.saveAll(categories);
    }

    private void syncInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        inventoryElasticsearchRepository.saveAll(inventories);
    }

    private void syncReaders() {
        List<Reader> readers = readerRepository.findAll();
        readerElasticsearchRepository.saveAll(readers);
    }

    private void syncBorrowings() {
        List<Borrowing> borrowings = borrowingRepository.findAll();
        borrowingElasticsearchRepository.saveAll(borrowings);
    }
}
