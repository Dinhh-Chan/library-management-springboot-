package com.example.library_management.entity;

import java.util.Set;
import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Entity
@Table(name = "books")
@Document(indexName = "books")
public class Book {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Field(type = FieldType.Text)
    private String title;

    @Column(name = "quantity", nullable = false)
    @Field(type = FieldType.Integer)
    private Integer quantity;

    @Column(name = "link_file", nullable = false)
    @Field(type = FieldType.Keyword)
    private String link_file;

    @ManyToMany 
    @JoinTable(
        name = "books_categories", 
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @ManyToMany 
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Field(type = FieldType.Nested) // Đánh dấu authors là một nested object cho Elasticsearch
    private Set<Author> authors;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Inventory inventory;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Borrowing> borrowings;

    // Constructors
    public Book() {}

    public Book(String title, Integer quantity, String link_file){
        this.title = title;
        this.quantity = quantity;
        this.link_file = link_file;
    }

    // Getters và Setters
    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public String getFile(){
        return link_file;
    }

    public void setFile(String link_file){
        this.link_file = link_file;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        if (inventory != null) {
            inventory.setBook(this);
        }
    }

    public Set<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(Set<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }
}
