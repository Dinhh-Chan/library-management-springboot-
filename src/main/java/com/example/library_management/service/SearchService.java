package com.example.library_management.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library_management.entity.Author;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.Borrowing;
import com.example.library_management.entity.Category;
import com.example.library_management.entity.Reader;
import com.example.library_management.enums.BorrowingStatus;
import com.example.library_management.repository.elasticsearch.AuthorElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.BookElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.BorrowingElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.CategoryElasticsearchRepository;
import com.example.library_management.repository.elasticsearch.ReaderElasticsearchRepository;

@Service
public class SearchService {

    @Autowired
    private BookElasticsearchRepository bookElasticsearchRepository;

    @Autowired
    private AuthorElasticsearchRepository authorElasticsearchRepository;

    @Autowired
    private CategoryElasticsearchRepository categoryElasticsearchRepository;

    @Autowired
    private ReaderElasticsearchRepository readerElasticsearchRepository;

    @Autowired
    private BorrowingElasticsearchRepository borrowingElasticsearchRepository;

    // Tìm kiếm sách theo tiêu đề
    public List<Book> searchBooksByTitle(String title){
        return bookElasticsearchRepository.findByTitleContaining(title);
    }

    // Tìm kiếm sách theo tên tác giả
    public List<Book> searchBooksByAuthorName(String authorName){
        return bookElasticsearchRepository.findByAuthorNameContaining(authorName);
    }

    // Tìm kiếm tác giả theo tên
    public List<Author> searchAuthorsByName(String name){
        return authorElasticsearchRepository.findByNameContaining(name);
    }

    // Tìm kiếm danh mục theo tên
    public List<Category> searchCategoriesByName(String categoryName){
        return categoryElasticsearchRepository.findByCategoryNameContaining(categoryName);
    }

    // Tìm kiếm người đọc theo username
    public List<Reader> searchReadersByUsername(String username){
        return readerElasticsearchRepository.findByUsernameContaining(username);
    }

    // Tìm kiếm giao dịch mượn theo trạng thái
    public List<Borrowing> searchBorrowingsByStatus(BorrowingStatus status){
        return borrowingElasticsearchRepository.findByStatus(status);
    }

    // Tìm kiếm giao dịch mượn theo ngày mượn
    public List<Borrowing> searchBorrowingsByBorrowDateRange(LocalDate startDate, LocalDate endDate){
        return borrowingElasticsearchRepository.findByBorrowDateBetween(startDate, endDate);
    }

}
