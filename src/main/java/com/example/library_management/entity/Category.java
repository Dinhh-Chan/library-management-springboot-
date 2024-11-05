package com.example.library_management.entity;

import java.util.Set;

import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "categories")
@Document(indexName = "categories")  // Thêm annotation cho Elasticsearch
public class Category {
    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name ="category_name", nullable= false, unique=true)
    @Field(type = FieldType.Text)
    private String categoryName;

    @ManyToMany(mappedBy= "categories")
    @JsonBackReference  // Để tránh đệ quy khi serialize JSON
    private Set<Book> books; 

    // Constructors
    public Category(){}

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    // Getters và Setters
    public Long getId(){
        return id;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public Set<Book> getBooks(){
        return books;
    }

    public void setBooks(Set<Book> books){
        this.books = books;
    }
}
