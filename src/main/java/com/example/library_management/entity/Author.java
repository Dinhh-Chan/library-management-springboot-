package com.example.library_management.entity;

import java.util.Set;

import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name ="authors")
@Document(indexName = "authors")  // Thêm annotation cho Elasticsearch
public class Author {
    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "name", nullable= false )
    @Field(type = FieldType.Text)
    private String name; 

    @Column(name="bio", columnDefinition= "TEXT")
    @Field(type = FieldType.Text)
    private String bio ;

    @ManyToMany(mappedBy= "authors",fetch = FetchType.EAGER)
    @JsonBackReference  // Để tránh đệ quy khi serialize JSON
    private Set<Book> books;

    // Constructors
    public Author(){}

    public Author(String name, String bio){
        this.name = name;
        this.bio = bio;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }   
}
