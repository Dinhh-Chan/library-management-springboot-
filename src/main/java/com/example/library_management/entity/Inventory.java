package com.example.library_management.entity;

import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name ="inventory")
@Document(indexName = "inventory")  // Thêm annotation cho Elasticsearch
public class Inventory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="book_id", nullable=false, unique=true)
    @JsonManagedReference  // Để tránh đệ quy khi serialize JSON
    private Book book;

    @Column(name="total_stock", nullable= false)
    @Field(type = FieldType.Integer)
    private Integer totalStock;

    @Column(name="available_stock", nullable=false)
    @Field(type = FieldType.Integer)
    private Integer availableStock;

    // Constructors
    public Inventory(){};

    public Inventory(Book book, Integer totalStock, Integer availableStock) {
        this.book = book;
        this.totalStock = totalStock;
        this.availableStock = availableStock;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}
}
