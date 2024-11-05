package com.example.library_management.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.example.library_management.enums.BorrowingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "borrowings")
@Document(indexName = "borrowings")  // Thêm annotation cho Elasticsearch
public class Borrowing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mối quan hệ nhiều-một với Reader
    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    @JsonBackReference  // Để tránh đệ quy khi serialize JSON
    private Reader reader;

    // Mối quan hệ nhiều-một với Book
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @JsonBackReference
    private Book book;

    @Column(name = "borrow_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate borrowDate;

    @Column(name = "return_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate returnDate;

    @Column(name = "actual_return_date")
    @Field(type = FieldType.Date)
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Field(type = FieldType.Keyword)  // Enum thường được lưu dưới dạng keyword
    private BorrowingStatus status; // DANG_MUON, DA_TRA, QUA_HAN

    // Constructors
    public Borrowing() {}

    public Borrowing(Reader reader, Book book, LocalDate borrowDate, LocalDate returnDate, BorrowingStatus status) {
        this.reader = reader;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

	public void setBook(Book book) {
		this.book = book;
	}

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}

    public LocalDate getReturnDate() {
        return returnDate;
    }

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public BorrowingStatus getStatus() {
        return status;
    }

	public void setStatus(BorrowingStatus status) {
		this.status = status;
	}
}
