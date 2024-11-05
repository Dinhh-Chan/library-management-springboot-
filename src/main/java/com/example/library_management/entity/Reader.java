package com.example.library_management.entity;

import java.util.Set;

import com.example.library_management.enums.UserRole;

import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "readers")
@Document(indexName = "readers")  // Thêm annotation cho Elasticsearch
public class Reader {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_info")
    @Field(type = FieldType.Text)
    private String contactInfo;

    @Column(name = "quota", nullable = false)
    @Field(type = FieldType.Integer)
    private Integer quota;

    @Column(name = "username", nullable = false, unique = true)
    @Field(type = FieldType.Keyword)  // Username thường là unique và không cần phân tích
    private String username;

    @Column(name = "password", nullable = false)
    @Field(type = FieldType.Keyword)  // Mật khẩu nên được mã hóa, lưu dưới dạng keyword
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Field(type = FieldType.Keyword)
    private UserRole role; // ADMIN hoặc USER

    // Mối quan hệ một-nhiều với Borrowing
    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Để tránh đệ quy khi serialize JSON
    private Set<Borrowing> borrowings;

    // Constructors
    public Reader() {}

    public Reader(String contactInfo, Integer quota, String username, String password, UserRole role) {
        this.contactInfo = contactInfo;
        this.quota = quota;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public String getContactInfo() {
        return contactInfo;
    }

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

	public void setPassword(String password) {
		this.password = password;
	}

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Set<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(Set<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }
}
